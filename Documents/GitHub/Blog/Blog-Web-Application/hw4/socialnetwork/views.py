from django.shortcuts import render, redirect
from django.urls import reverse

from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout
from socialnetwork.forms import LoginForm,RegistrationForm
from django.utils import timezone

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

    login(request, new_user)
    return redirect(reverse('globalstream'))



def global_stream(request):
    dummy_post_1 = { 'id': 1 }
    dummy_post_1['post_content'] = 'It snows when I finish swimming. My hair just freezes!'
    dummy_post_1['post_file']    = 'Shuning Chen'
    dummy_post_1['post_time'] = timezone.now()
    
    dummy_entry_1 = { 'id': 1 }
    dummy_entry_1['comment_file'] = 'Olivia Obama'
    dummy_entry_1['comment_content'] = "Crazy. I'll look into that"
    dummy_entry_1['comment_time'] = timezone.now()
    
    dummy_post_2 = { 'id': 2 }
    dummy_post_2['post_content'] = 'I waited in the line the whole day at career fair yesterday!'
    dummy_post_2['post_file']    = 'Tom Cruise'
    dummy_post_2['post_time'] = timezone.now()
    
    dummy_entry_2 = { 'id': 2 }
    dummy_entry_2['comment_file'] = 'Placid Bush'
    dummy_entry_2['comment_content'] = "Mee too!"
    dummy_entry_2['comment_time'] = timezone.now()
    

    context = { 'post1': dummy_post_1,'post2':dummy_post_2,'entry1':dummy_entry_1,'entry2':dummy_entry_2}

    if request.method == 'GET':
        return render(request,'socialnetwork/globalstream.html',context)
    
def follower_stream(request):
    
    dummy_post_2 = { 'id': 2 }
    dummy_post_2['post_content'] = 'I waited in the line the whole day at career fair yesterday!'
    dummy_post_2['post_file']    = 'Tom Cruise'
    dummy_post_2['post_time'] = timezone.now()
    
    dummy_entry_2 = { 'id': 2 }
    dummy_entry_2['comment_file'] = 'Placid Bush'
    dummy_entry_2['comment_content'] = "Mee too!"
    dummy_entry_2['comment_time'] = timezone.now()
    

    context = { 'post2':dummy_post_2,'entry2':dummy_entry_2}
    if request.method == 'GET':
        return render(request,'socialnetwork/followerstream.html',context)
    
def profile_action(request):
    context = {}
    user = {'first_name':'Tom','last_name':'Cruise'}
    context['user'] = user
    if request.method == 'GET':
        return render(request,'socialnetwork/profile.html',context)
        
def myprofile_action(request):
    context = {}
    user = {'first_name':'Shuning','last_name':'Chen'}
    context['user'] = user
    if request.method == 'GET':
        return render(request,'socialnetwork/myprofile.html',context)
    
