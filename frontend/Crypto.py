#!/usr/bin/env python

from Crypto.Cipher import AES
from Crypto.Hash import SHA256
import base64
import os

class CryptoPack(object):
    """docstring for CryptoPack"""
    def __init__(self):
        super(CryptoPack, self).__init__()
        self.arg = arg
        self.BLOCK_SIZE = 32
        self.PADDING = '{'
        self.pad = lambda s: s + (self.BLOCK_SIZE - len(s) % self.BLOCK_SIZE) * self.PADDING
        self.EncodeAES = lambda c, s: base64.b64encode(c.encrypt(self.pad(s)))
        self.DecodeAES = lambda c, e: c.decrypt(base64.b64decode(e)).rstrip(self.PADDING)


    def encrypt_RSA(self, public_key_loc, message):
        '''
        param: '`key.pub' Path to public key
        param: message String to be encrypted
        return base64 encoded encrypted string
        '''
        from Crypto.PublicKey import RSA
        from Crypto.Cipher import PKCS1_OAEP
        key = open('key.public', "r").read()
        rsakey = RSA.importKey(key)
        rsakey = PKCS1_OAEP.new(rsakey)
        encrypted = rsakey.encrypt(message)
        return encrypted.encode('base64')
    
    def sha256Item(self, password):
        secret = SHA256.new()
        secret.update(password)
        secret = secret.digest()
        return secret
    
    def sha256Item_Hex(self, user_vid, user_ssn, user_pin):
        myHash = SHA256.new()
        myHash.update(user_vid)
        myHash.update(user_ssn)
        myHash.update(user_pin)
        return myHash.hexdigest()
    
    def encrypt(self, password, message):# you dont need this
        cipher = AES.new(self.sha256Item(password))
        return self.EncodeAES(cipher, message)
    
    def decrypt(self, password, message):
        cipher = AES.new(self.sha256Item(password))
        return self.DecodeAES(cipher, message)