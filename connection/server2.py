port = 9999

# import socket library
import socket
import os
import threading
import sys,time

class serverThread (threading.Thread):
	
	# constructor
	def __init__(self, c, a):
		threading.Thread.__init__(self)
		self.sock = c # client socket 
		self.addr = a # ip address information
		
	# the entry point into the thread
	def run(self):
		
		# todo -- really need a way to synchronize the log messages
		print self.addr, " connected. Starting worker thread ", self.name,
		try:
			msg = self.sock.recv(4096)
			self.sock.send(msg.upper())
			x = self.sock.recv(4096)
			print x
		finally:
			# close socket
			print "thread is sleeping", self.name,

			time.sleep(10)

			print "thread", self.name, " is going to die"
			self.sock.close()
			
###################################
# main server thread starts here
###################################

# open server socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# bind to all interfaces
s.bind(("", port))

# listen for max of 5 connections (raised it to 1000 for testing on LAN)
s.listen(50)

print "listening on all interfaces..."

# accept loop
while True:
	(c, addr) = s.accept()

	# create a new thread and start it
	# passes the socket to the thread as an 
	worker = serverThread(c, addr)
	worker.start()
