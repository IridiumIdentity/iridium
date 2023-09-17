#python class for authorization service     

import os
import json
import http.client

class AuthorizationService:
    # python constructor taking http client as parameter
    def __init__(self, http_client):
        # python set http client
        self.http_client = http_client  
        self.base_url = os.environ.get('FLASK_PUBLIC_IRIDIUM_DOMAIN')
        self.redirect_uri = os.environ.get('FLASK_PUBLIC_IRIDIUM_REDIRECT_URI') 
        self.client_id = os.environ.get('FLASK_PUBLIC_IRIDIUM_CLIENT_ID')


    # python function taking code and state and retrieving external identity
    def get_external_identity(self, code, state):
        # python set headers for http client
        headers = {'Content-type': 'application/json'}
        # python set payload for http client
        payload = {'code': code, 'state': state}
        # python set url for http client
        url = (self.base_url + 
               'oauth/token?'+
               'grant_type=authorization_code' + 
               code + '&redirect_uri=' + 
               self.redirectUri + '&client_id=' + 
               self.clientId + '&code_verifier=' + 
               codeVerifier + '&state=' + state)
        
        # python set method for http client
        method = 'POST'
        # python set body for http client
        body = json.dumps(payload)
        # python set response for http client
        response = self.http_client.request(method, url, body, headers)
        # python return response
        return response
    