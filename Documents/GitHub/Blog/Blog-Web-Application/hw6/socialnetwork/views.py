from django.shortcuts import render, redirect,get_object_or_404
from django.urls import reverse
from django.http import JsonResponse
import json
from django.utils.dateparse import parse_datetime

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout
from socialnetwork.forms import LoginForm,RegistrationForm,PostForm,UpdateForm,CommentForm
from socialnetwork.models import Post,Profile,Comment
from django.utils import timezone
from django.http import HttpResponse,Http404
from django.core.exceptions import ObjectDoesNotExist
from django.core import serializers


# Create your views here.
def login_action(request):
    context = {}

    # Just display the registration form if this is a GET request.
    if request.method == 'GET':
        context['form'] = LoginForm()
        return render(request, 'socialnetwork/login.html', context)

    # Creates a bound form from the request POST parameters and makes the 
    # form available in the request context dictionary.
    form = LoginForm(request.POST)
    context['form'] = form

    #Validates the form.
    if not form.is_valid():
        return render(request, 'socialnetwork/login.html', context)

    new_user = authenticate(username=form.cleaned_data['username'],
                            password=form.cleaned_data['password'])

    login(request, new_user)
    return redirect(reverse('globalstream'))

def register_action(request):
    context = {}
    if request.method == 'GET':
        context['form'] = RegistrationForm()
        return render(request,'socialnetwork/register.html',context)
    
    # Creates a bound form from the request POST parameters and makes the 
    # form available in the request context dictionary.
    form = RegistrationForm(request.POST)
    context['form'] = form

    # Validates the form.
    if not form.is_valid():
        return render(request, 'socialnetwork/register.html', context)

    # At this point, the form data is valid.  Register and login the user.
    new_user = User.objects.create_user(username=form.cleaned_data['username'], 
                                        password=form.cleaned_data['password1'],
                                        email=form.cleaned_data['email'],
                                        first_name=form.cleaned_data['first_name'],
                                        last_name=form.cleaned_data['last_name'])
    new_user.save()

    new_user = authenticate(username=form.cleaned_data['username'],
                            password=form.cleaned_data['password1'])
    profile = Profile(instance=new_user)
    profile.save()
    login(request, new_user)
    return redirect(reverse('login'))


@login_required  
def global_stream(request):
    #display the post in reverse order by the date posted
    posts = Post.objects.order_by('date_posted').reverse()
    comments = Comment.objects.order_by('date_commented').reverse()
    
    if request.method == 'GET':
        context = { 'postform': PostForm(),'commentform':CommentForm(),'posts':posts,'comments':comments}
        return render(request,'socialnetwork/globalstream.html',context)
    #if a post is created
    else:
        post = Post(content = request.POST['post_text'],author = request.user)
        create_post = PostForm(request.POST)
        if not create_post.is_valid():
            context = { 'postform': create_post,'commentform':CommentForm(),'posts':posts,'message':"Post not valid!",'comments':comments }
            return render(request, 'socialnetwork/globalstream.html', context)
        else:
            post.save()
            context = { 'postform': create_post,'commentform':CommentForm(),'posts':posts,'comments':comments}
            return render(request, 'socialnetwork/globalstream.html', context)
        

def add_comment(request,post_id):
    if request.method != 'POST':
        raise Http404

    if not 'comment' in request.POST or not request.POST['comment']:
        message = 'You must enter an comment to add.'
        json_error = '{ "error": "'+message+'" }'
        return HttpResponse(json_error, content_type='application/json')
    
    post = Post.objects.get(id=post_id)
    new_comment = Comment(content=request.POST['comment'],author = request.user,post = post)
    new_comment.save()    
    comment = {'first_name':new_comment.author.first_name,'last_name':new_comment.author.last_name,'id':new_comment.id,
              'date_commented':new_comment.date_commented,'content':new_comment.content,"author_id":new_comment.author.id}
    response_text = comment
    #print(response_text)
#    return HttpResponse(response_text, content_type='application/json')
    return JsonResponse(response_text)


def refreshGlobal(request): 
    if request.method !='GET':
        raise Http404
        
    last_refresh = request.GET['last_refresh']
    time = parse_datetime(last_refresh)
    print(time)
    postAuthors = list()
    for post in Post.objects.order_by('date_posted').filter(date_posted__gt=time):
        postAuthors.append(post.author.first_name+" "+post.author.last_name)
#    newposts = serializers.serialize('json',Post.objects.order_by('date_posted').filter(date_posted__gt=time))  
    newposts = list(Post.objects.order_by('date_posted').filter(date_posted__gt=time).all().values())
#    newcomments = serializers.serialize('json',Comment.objects.order_by('date_commented').filter(date_commented__gt=time))
    newcomments = list(Comment.objects.order_by('date_commented').filter(date_commented__gt=time).all().values())
    if not postAuthors:
        postAuthors = '[]'
        
    authorIDs = list()
    commentauthors = list()
    postIDs = list()
    for comment in Comment.objects.order_by('date_commented').filter(date_commented__gt=time):
        authorIDs.append(comment.author.id)
        commentauthors.append(comment.author.first_name+" "+comment.author.last_name)
        postIDs.append(comment.post.id) 
    if not commentauthors:
        commentauthors = '[]'
    if not authorIDs:
        authorIDs = '[]'
    if not postIDs:
        postIDs = '[]'
    contents = {"posts":newposts,"comments":newcomments,'postauthors':postAuthors,"authorIDs":authorIDs,
                "commentauthors":commentauthors,"postIDs":postIDs}
#    return HttpResponse(json.dumps(contents), content_type='application/json')
    return JsonResponse(contents, safe=False)
#    response_text = json.dumps(contents)
    
        
def refreshFollower(request):
    if request.method !='GET':
        raise Http404
        
    last_refresh = request.GET['last_refresh']
    time = parse_datetime(last_refresh)
    print(time)
    profile = get_object_or_404(Profile,user=request.user)
    
    posts = Post.objects.order_by('date_posted').filter(date_posted__gt=time)
    newposts = list()
    for post in posts:
        if post.author in profile.follower.all():
            newposts.append(post)
    newcomments = list()
    comments = Comment.objects.order_by('date_commented').filter(date_commented__gt=time)
    for comment in comments:
        if comment.post in newposts:
            newcomments.append(comment)  
    authorIDs = list()
    authors = list()
    postIDs = list()
    for comment in newcomments:
        authorIDs.append(comment.author.id)
        authors.append(comment.author.first_name+" "+comment.author.last_name)
        postIDs.append(comment.post.id)
    if not authors:
        authors = '[]'
    if not authorIDs:
        authorIDs = '[]'
    if not postIDs:
        postIDs = '[]'
    contents = {"posts":newposts,"comments":newcomments,"authorIDs":authorIDs,"commentauthors":authors,"postIDs":postIDs}
#    response_text = json.dumps(contents)
#    return JsonResponse(contents)
    return HttpResponse(json.dumps(contents), content_type='application/json')

    
    
@login_required      
def follower_stream(request):
    context={}
    profile = get_object_or_404(Profile,user=request.user)
    posts = list()
    #add posts to the list of the users who are followed by the current logged user
    for post in Post.objects.order_by('date_posted').reverse():
        if post.author in profile.follower.all():
            posts.append(post)
    if not posts:
        context['message'] = "You haven't follow anyone!"
    comments = list()
    for comment in Comment.objects.order_by('date_commented').reverse():
        if comment.post in posts:
            comments.append(comment)
    context['posts']= posts
    context['comments'] = comments
    return render(request,'socialnetwork/followerstream.html',context)
    
@login_required       
def public_profile(request,id):
    context = {}
    current_user = User.objects.get(id=id)
    profile = get_object_or_404(Profile, user = current_user)
    # when it comes to the logged user's own page
    if current_user==request.user:
        #if the udpate button is clicked
        if request.method=='POST':
            #pass request.FILES to pass the original picture
            updateForm = UpdateForm(request.POST,request.FILES,instance=profile)
            #if the form is valid
            if updateForm.is_valid():
                updateForm.save()
                message = 'Your profile has been updated'
                context['message'] = message
        #otherwise, just return the original profile
        else:
            updateForm = UpdateForm(instance=profile)

        context['follower'] = profile.follower.all()
        context['form'] = updateForm
        context['profile'] = profile
        return render(request,'socialnetwork/myprofile.html',context) 
    # when it comes to the other users' page
    else:
        logged_user = request.user;
        logged_profile = get_object_or_404(Profile,user = logged_user)
        context['profile'] = profile 
        #if the button follow or unfollow is clicked 
        #it is a post request
        if request.method == 'POST':
            if current_user not in logged_profile.follower.all():
                logged_profile.follower.add(current_user)
                context['unfollow'] = 'unfollow'
            else:
                logged_profile.follower.remove(current_user)  
                context['follow'] = 'follow'
        else:
            if current_user not in logged_profile.follower.all():
                context['follow'] = 'follow'
            else:
                context['unfollow'] = 'unfollow' 

        return render(request,'socialnetwork/profile.html',context)
    
@login_required
def get_photo(request, id):
    profile = get_object_or_404(Profile, id=id)

    # Probably don't need this check as form validation requires a picture be uploaded.
    if not profile.picture:
        raise Http404

    return HttpResponse(profile.picture, content_type=profile.content_type)    
    
    
    
    
