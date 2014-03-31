#!/usr/bin/env python

import socket


TCP_IP = '127.0.0.1' # IP of the server
TCP_PORT = 9999
BUFFER_SIZE = 16421
# MESSAGE = "Hello, World!"

def connectServer():
	print("Connecting ...")
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((TCP_IP, TCP_PORT))
	return s
def sendData(MESSAGE):
	print("Sending Message ...")
	s.send(MESSAGE)
def getData():
	print("Receivng Data ...")
	data = s.recv(BUFFER_SIZE)
	return data
def testJava(num):
	return num**2
# s.close()

# print "received data:", data