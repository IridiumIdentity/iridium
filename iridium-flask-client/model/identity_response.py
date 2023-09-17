# python response object containing id, username, profile_response
class identity_response(object):
    # python function returning object containing id, username, profile_response
    def __init__(self, id, username, profile_response):
        # python set id for id
        self.id = id
        # python set username for username
        self.username = username
        # python set profile_response for profile_response
        self.profile_response = profile_response

    # python function returning json object containing id, username, profile_response
    def to_json(self):
        # python set payload for http client
        payload = {'id': self.id, 'username': self.username, 'profile_response': self.profile_response}
        return payload
    
    # python function returning string containing id, username, profile_response
    def __str__(self):
        # python set payload for http client
        payload = {'id': self.id, 'username': self.username, 'profile_response': self.profile_response}
        return str(payload)
    