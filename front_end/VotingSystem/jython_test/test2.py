from org.jython.book.interfaces import Object

class ClassName(Object):
"""docstring for ClassName"""
	def __init__(self, arg):
		super(ClassName, self).__init__()
		self.arg = arg
		self.mod = none

	def add(self, a, b):
		return a+b

	def remove(self, a, b):
		return a-b

	def getmod(self, a):
		if self.mod == none:
			return none
		return a%self.mod
	
	def setMod(self, mod)
		self.mod = mod