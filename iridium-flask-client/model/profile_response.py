# profle response object containing firstName, lastName
class profile_response(object):
    # python function returning object containing firstName, lastName
    def __init__(self, firstName, lastName):
        # python set firstName for firstName
        self.firstName = firstName
        # python set lastName for lastName
        self.lastName = lastName

    # python function returning json object containing firstName, lastName
    def to_json(self):
        # python set payload for http client
        payload = {'firstName': self.firstName, 'lastName': self.lastName}
        return payload
    
    # python function returning string containing firstName, lastName
    def __str__(self):
        # python set payload for http client
        payload = {'firstName': self.firstName, 'lastName': self.lastName}
        return str(payload)
    
    