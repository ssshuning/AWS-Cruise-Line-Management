from django.urls import path
from django.contrib.auth import views as auth_views
from . import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path('', views.login_action, name='login'),
    path('register/', views.register_action, name='register'),
    path('globalstream/',views.global_stream,name='globalstream'),
    path('publicprofile/<int:id>/', views.public_profile, name='publicprofile'),
    path('followerstream/',views.follower_stream,name='followerstream'),

]

if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)