import os

# python set env variables
base_url = os.environ.get('FLASK_PUBLIC_IRIDIUM_DOMAIN')
redirect_uri = os.environ.get('FLASK_PUBLIC_IRIDIUM_REDIRECT_URI')
client_id = os.environ.get('FLASK_PUBLIC_IRIDIUM_CLIENT_ID')

# python function taking state and codechallenge as strings and returning url 
def url_generator(state, codechallenge):
    return (base_url + 'login?response_type=code' + 
        '&state=' + state +
        '&redirect_uri=' + redirect_uri + 
        '&client_id=' + client_id +
        '&code_challenge=' + codechallenge + 
        '&code_challenge_method=S256')

