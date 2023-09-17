# python response object with fields access_token, token_type, expires_in, and scope
class access_token_response(object):
    # python function returning object with fields access_token, token_type, expires_in, and scope
    def __init__(self, access_token, token_type, expires_in, scope):
        # python set access_token for access_token
        self.access_token = access_token
        # python set token_type for token_type
        self.token_type = token_type
        # python set expires_in for expires_in
        self.expires_in = expires_in
        # python set scope for scope
        self.scope = scope

    # python function returning json object with fields access_token, token_type, expires_in, and scope
    def to_json(self):
        # python set payload for http client
        payload = {'access_token': self.access_token, 'token_type': self.token_type, 'expires_in': self.expires_in, 'scope': self.scope}
        return payload
    
    # python function returning string with fields access_token, token_type, expires_in, and scope
    def __str__(self):
        # python set payload for http client
        payload = {'access_token': self.access_token, 'token_type': self.token_type, 'expires_in': self.expires_in, 'scope': self.scope}
        return str(payload)



