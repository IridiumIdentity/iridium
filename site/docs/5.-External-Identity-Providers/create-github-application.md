# Create a GitHub Application

**Note** 
If you are standing up the Iridium UI you can use the values specified in the [Form Values](#form-values) section of this page.  

If you are using your own app, you will need to substitute the specific `Homepage URL` and `Authorization callback URL` for you app.

[You can create one by filling out the form here](https://github.com/settings/applications/new).

![register-iridium-github-application](..%2Fimages%2Fregister-github-oauth.png)

## Form Values
For the form values enter the following:

* `ApplicationName`: Any name you wish.  In this example we use "MyIridiumInstance"
* `Homepage URL`: http://localhost:4200.
* `Authorization callback URL`: http://localhost:4200/callback

## Get your Client ID
After the application is created you'll need to create a secret for it.  
1. Make sure to capture your "Client ID" value. You'll need while initializing  Iridium
2. Generate a secret for your GitHub application by clicking on the  "Generate new client secret" button.
![generate-iridium-github-secret.png](..%2Fimages%2Fgenerate-github-secret.png) 

## Capture the GitHub Secret
1. After a secret is created you'll need to capture that value to use with the CLI tool.

![post-generate-github-secret.png](..%2Fimages%2Fpost-generate-github-secret.png)
