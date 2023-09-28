# python class for authorization service     

import os
import json
import http.client

import oauth_constants

class AuthorizationService:
    # python constructor taking http client as parameter
    def __init__(self, http_client):
        # python set http client
        self.http_client = http_client  
        self.base_url = os.environ.get(oauth_constants.PUBLIC_IRIDIUM_DOMAIN)
        self.redirect_uri = os.environ.get(oauth_constants.PUBLIC_IRIDIUM_REDIRECT_URI)) 
        self.client_id = os.environ.get(oauth_constants.PUBLIC_IRIDIUM_CLIENT_ID)


    # python function taking code and state and retrieving external identity
    def get_external_identity(self, code, state):
        # python set headers for http client
        headers = {'Content-type': 'application/json'}
        # python set payload for http client
        payload = {'code': code, 'state': state}

        # get cookie from http_client request
        codeVerifier = self.http_client.get_cookie(oauth_constants.PKCE_CODE_VERIFIER)

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

    # python function for key exchange with a code for input 
    def exchange(self, code):
        # python set headers for http client
        headers = {'Content-type': 'application/json'}
        # python set payload for http client
        payload = {'code': code}

        # get cookie from http_client request
        codeVerifier = self.http_client.get_cookie(PKCE_CODE_VERIFIER)

        # python set url for http client
        url = (self.base_url + 
               'oauth/token?'+
               'grant_type=authorization_code' + 
               code + '&redirect_uri=' + 
               self.redirect_uri + '&client_id=' + 
               self.client_id + '&code_verifier=' + 
               codeVerifier)
        
        # python set method for http client
        method = 'POST'
        # python set body for http client
        body = json.dumps(payload)
        # python set response for http client
        response = self.http_client.request(method, url, body, headers)
        # python return response
        return response 

    # python function for taking a bearer token and returning response 
    def get_identity(self, bearer_token):
        # python set headers for http client
        headers = {
            'Accept':  'application/vnd.iridium.id.identity-response.1+json',
            'Authorization': 'Bearer ' + bearer_token
            }
        # python set url for http client
        url = self.base_url + 'identities'
        # python set method for http client
        method = 'GET'
        # python set body for http client
        body = None
        # python set response for http client
        response = self.http_client.request(method, url, body, headers)
        # python return response
        return response