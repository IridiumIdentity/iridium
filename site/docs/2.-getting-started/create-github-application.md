# Create a Github Application

[You can create one by filling out the form here](https://github.com/settings/applications/new).

![register-iridium-github-application](..%2Fimages%2Fregister-github-oauth.png)

For the form values enter the following:

* `ApplicationName`: Any name you wish.  In this example we use "MyIridiumInstance"
* `Homepage URL`: http://localhost:4200.
* `Authorization callback URL`: http://localhost:4200/dashboard

## Get your Client ID
After the application is created you'll need to create a secret for it.  
1. Make sure to capture your "Client ID" value. You'll need while initializing  Iridium
2. Generate a secret for your Github application by clicking on the  "Generate new client secret" button.
![generate-iridium-github-secret.png](..%2Fimages%2Fgenerate-github-secret.png) 

## Capture the Github Secret
1. After a secret is created you'll need to capture that value to use with the CLI tool.

![post-generate-github-secret.png](..%2Fimages%2Fpost-generate-github-secret.png)
