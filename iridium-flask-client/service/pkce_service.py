import base64
import hashlib


# define python class
class PkceService:
    # define python constructor
    def __init__(self, code_verifier, code_challenge):
        self.code_verifier = code_verifier
        self.code_challenge = code_challenge

    # define python function to generate a header from a token
    def generate_header(self, token):
        return
        
    # define python fuction to generate code challenge
    def generate_code_challenge(self) -> str:
        # python generate code challenge
        return base64.urlsafe_b64encode(hashlib.sha256(self.code_verifier.encode('utf-8')).digest()).decode('utf-8').replace("=", "")   


    # generate base64 encoded challenge
    def generate_base64_encoded_challenge(self) -> str:
        return base64.urlsafe_b64encode(hashlib.sha256(self.code_verifier.encode('utf-8')).digest()).decode('utf-8').replace("=", "")   
    
    # function to generate random string
    def generate_random_string(self) -> str:
        # generate random values using unsigned array of 32 bit integers (length 28)
        return "random_string"
    
    # function to generate sha 256 signature 
    def generate_sha256_signature(self, random_string) -> str:
        # generate sha 256 signature
        return "sha256_signature"
    
    # function to base64 url encode 
    def base64_url_encode(self, sha256_signature) -> str:
        # base64 url encode
        return "base64_url_encode"
    