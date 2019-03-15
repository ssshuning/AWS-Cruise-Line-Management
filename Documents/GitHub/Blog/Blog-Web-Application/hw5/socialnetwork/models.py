from django.db import models
from django.contrib.auth.models import User
from django.utils import timezone


    
class Post(models.Model):
    content = models.TextField()
    date_posted = models.DateTimeField(default=timezone.now)
    author = models.ForeignKey(User,on_delete=models.CASCADE)
    
    def _str_(self):
        return 'Post(context=' + str(self.content) + ')'

class Profile(models.Model):
    bio = models.TextField(default='This is my bio')
    picture = models.ImageField(upload_to='pictures',default = 'anime.jpg')
    user = models.OneToOneField(User,on_delete=models.CASCADE)
    follower = models.ManyToManyField(User,related_name='follower')

    

