from django.shortcuts import render, redirect,get_object_or_404
from django.urls import reverse

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout
from socialnetwork.forms import LoginForm,RegistrationForm,PostForm,UpdateForm
from socialnetwork.models import Post,Profile
from django.utils import timezone
from django.http import HttpResponse,Http404
from django.core.exceptions import ObjectDoesNotExist


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
    
    if request.method == 'GET':
        context = { 'postform': PostForm(),'posts':posts}
        return render(request,'socialnetwork/globalstream.html',context)
    #if a post is created
    else:
        post = Post(content = request.POST['post_text'],author = request.user)
        create_post = PostForm(request.POST)
        if not create_post.is_valid():
            context = { 'postform': create_post,'message':"Post not valid!" }
            return render(request, 'socialnetwork/globalstream.html', context)
        else:
            post.save()
            context = { 'postform': create_post,'posts':posts}
            return render(request, 'socialnetwork/globalstream.html', context)
    
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
    context['posts']= posts
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


    
    
    
