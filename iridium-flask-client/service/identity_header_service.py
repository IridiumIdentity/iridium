# function to generate a header from a token
def generate_header(token):
    # http GET header from token
    var = {'Accept': 'application/vnd.iridium.id.identity-response.1+json', 
           'Authorization': 'Bearer ' + token}

    # python return header
    return var
