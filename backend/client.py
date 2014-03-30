#!/usr/bin/env python

import socket
import connect
import sampleAESEncDec 

"""docstring for VotingSystem"""
xx = sampleAESEncDec.RunAES()

TCP_IP = '127.0.0.1' # IP of the server
TCP_PORT = 9999
BUFFER_SIZE = 1024
# MESSAGE = "Hello, World!"
DATA = {}

DATA["state"] = "register"
DATA["vid_hash"] = "917ef7e7be4a84e279b74a257953307f1cff4a2e3d221e363ead528c6b556edb"
tmp = {}
tmp["vid"] = "265jMeges"
tmp["ssn"] = "700-33-6870"
tmp["pin"] = "1234"
DATA["userInfo"] = tmp

vs = connect.VotingSystem()
MESSAGE = vs._encrypt_RSA('key.pub', str(DATA))
print MESSAGE


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))
s.send(MESSAGE)
data = s.recv(BUFFER_SIZE)
s.close()

print "received data:", data