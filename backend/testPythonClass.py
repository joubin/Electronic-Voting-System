class VotingSystem(object):
	"""docstring for VotingSystem"""
	def __init__(self, arg):
		super(VotingSystem, self).__init__()
		self.arg = arg

	def function(self):
		print self.arg

	def function2(self, poop):
		print poop

x = VotingSystem("test")

x.function()

x.function2("rest")

