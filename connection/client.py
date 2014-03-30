#!/usr/bin/env python

import socket


TCP_IP = '127.0.0.1' # IP of the server
TCP_PORT = 9999
BUFFER_SIZE = 1024
# MESSAGE = "Hello, World!"

def connect():
	print("Connecting ...")
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((TCP_IP, TCP_PORT))
def set(MESSAGE):
	print("Sending Message ...")
	s.send(MESSAGE)
def get():
	print("Receivng Data ...")
	data = s.recv(BUFFER_SIZE)
	return data
s.close()

print "received data:", data