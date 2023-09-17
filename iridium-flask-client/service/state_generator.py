# python class to generate oauth state 

import uuid

class OAuthStateGenerator:
     # python function to generate random uuid and convert from utf-8 to hex
    def generate(self):
        # python return random uuid and convert from utf-8 to hex
        return uuid.uuid4().hex
