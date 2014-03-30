#!/usr/bin/env python

from Crypto.Cipher import AES
from Crypto.Hash import SHA256
import base64
import os



class RunAES(object):
    """docstring for AES"""
    def __init__(self):
        self.BLOCK_SIZE = 32
        self.PADDING = '{'
        self.pad = lambda s: s + (self.BLOCK_SIZE - len(s) % self.BLOCK_SIZE) * self.PADDING
        self.EncodeAES = lambda c, s: base64.b64encode(c.encrypt(self.pad(s)))
        self.DecodeAES = lambda c, e: c.decrypt(base64.b64decode(e)).rstrip(self.PADDING)
    def sha256Item(self, password):
        secret = SHA256.new()
        secret.update(password)
        secret = secret.digest()
        return secret
    def sha256Item_Hex(self, password):
        secret = SHA256.new()
        secret.update(password)
        secret = secret.hexdigest()
        return secret
    def encrypt(self, password, message):
        cipher = AES.new(self.sha256Item(password))
        return self.EncodeAES(cipher, message)
    def decrypt(self, password, message):
        cipher = AES.new(self.sha256Item(password))
        return self.DecodeAES(cipher, message)
 
if __name__ == '__main__':              
    x = myAES()
    print x.decrypt("asd",x.encrypt("asd", "asd"))
    hello = "hi fucker"
    bye = """
    hello mother 
    bye mother
    """
    print bye
