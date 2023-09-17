# python for setting cookie on flask response
from flask import make_response   
from flask import request
import datetime

# python set cookie with parameters for name, value, expiration, and path
def set_cookie(name, value, expireDays=None, path='/'):
    # python create response object
    response = make_response()

    expires = get_expiration(expireDays)

    # python set cookie with parameters for name, value, expiratin, and path
    response.set_cookie(name, value, expires=expires, path=path)
    # python return response object
    return response

# python for getting cookie from flask request  
def get_cookie(name):
    # python return cookie value from flask request
    return request.cookies.get(name)

# delete cookie in flask response   
def delete_cookie(name, path='/'):
    # python create response object
    response = make_response()
    # python set cookie with parameters for name, value, expiratin, and path
    response.set_cookie(name, '', expires=0, path=path)
    # python return response object
    return response

# python function taking input days and calculatig current time and adding days to it
def get_expiration(days):
    # python return current time plus days
    return datetime.datetime.now() + datetime.timedelta(days=days)  
