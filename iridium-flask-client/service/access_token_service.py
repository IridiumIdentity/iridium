import cookie_service
import oauth_constants
import os
import json

# python class access token service
class AcessTokenService:
    def __init__(self):
        # define cookie service
        self.cookie_service = CookieService()


    # function to build exchange from code 
    def exchange(self, code):
        # python set headers for http client
        headers = {'Content-type': 'application/json'}
        # python set payload for http client
        payload = {'code': code}

        # get redirect uri from environment variable
        self.redirect_uri = os.environ.get(oauth_constants.PUBLIC_IRIDIUM_REDIRECT_URI)

        # get cookie from http_client request
        codeVerifier = self.cookie_service.get_cookie(oauth_constants.PKCE_CODE_VERIFIER)

        # python set url for http client
        url = (self.base_url + 
               'oauth/token?'+
               'grant_type=authorization_code' + 
               code + '&redirect_uri=' + 
               self.redirect_uri + '&client_id=' + 
               self.client_id + '&code_verifier=' + 
               codeVerifier + '&state=' + state)
        
        # python set method for http client
        method = 'POST'
        # python set body for http client
        body = json.dumps(payload)
        # python set response for http client
        response = self.http_client.request(method, url, body, headers)
        # python return response
        return response