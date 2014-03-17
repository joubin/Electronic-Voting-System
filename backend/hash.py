import hashlib
m = hashlib.sha1()
m.update("joubin")
print m.hexdigest()