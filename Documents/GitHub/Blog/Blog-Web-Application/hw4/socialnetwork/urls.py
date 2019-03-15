from django.urls import path
from django.contrib.auth import views as auth_views
from . import views

urlpatterns = [
    path('', views.login_action, name='login'),
    path('register', views.register_action, name='register'),
    path('globalstream',views.global_stream,name='globalstream'),
    path('profile', views.profile_action, name='profile'),
    path('myprofile', views.myprofile_action, name='myprofile'),
    path('followerstream',views.follower_stream,name='followerstream'),
]