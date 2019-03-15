from django.urls import path
from django.contrib.auth import views as auth_views
from . import views


urlpatterns = [
    path('', views.login_action, name='login'),
    path('register/', views.register_action, name='register'),
    path('globalstream/',views.global_stream,name='globalstream'),
    path('publicprofile/<int:id>/', views.public_profile, name='publicprofile'),
    path('followerstream/',views.follower_stream,name='followerstream'),
    path('add-comment/<int:post_id>',views.add_comment),
    path('get-photo/<int:id>',views.get_photo),
    path('refresh-global/',views.refreshGlobal),
    path('refresh-follower/',views.refreshFollower),
]

