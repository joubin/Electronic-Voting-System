#!/usr/bin/env python

from Crypto.Cipher import AES
from Crypto.Hash import SHA256
import base64
import os


BLOCK_SIZE = 32
PADDING = '{'
pad = lambda s: s + (BLOCK_SIZE - len(s) % BLOCK_SIZE) * PADDING
EncodeAES = lambda c, s: base64.b64encode(c.encrypt(pad(s)))
DecodeAES = lambda c, e: c.decrypt(base64.b64decode(e)).rstrip(PADDING)


def encrypt_RSA(public_key_loc, message):
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

def sha256Item(password):
    secret = SHA256.new()
    secret.update(password)
    secret = secret.digest()
    return secret

def sha256Item_Hex(user_vid, user_ssn, user_pin):
    myHash = SHA256.new()
    myHash.update(user_vid)
    myHash.update(user_ssn)
    myHash.update(user_pin)
    return myHash.hexdigest()

def encrypt(password, message):# you dont need this
    cipher = AES.new(sha256Item(password))
    return EncodeAES(cipher, message)

def decrypt(password, message):
    cipher = AES.new(sha256Item(password))
    return DecodeAES(cipher, message)