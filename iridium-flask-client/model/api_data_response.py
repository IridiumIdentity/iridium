# python response object containing code, message, successful, data

class api_data_response(object):
    # python function returning object containing code, message, successful, data
    def __init__(self, code, message, successful, data):
        # python set code for code
        self.code = code
        # python set message for message
        self.message = message
        # python set successful for successful
        self.successful = successful
        # python set data for data
        self.data = data

    # python function returning json object containing code, message, successful, data
    def to_json(self):
        # python set payload for http client
        payload = {'code': self.code, 'message': self.message, 'successful': self.successful, 'data': self.data}
        return payload
    
    # python function returning string containing code, message, successful, data
    def __str__(self):
        # python set payload for http client
        payload = {'code': self.code, 'message': self.message, 'successful': self.successful, 'data': self.data}
        return str(payload)
    
    