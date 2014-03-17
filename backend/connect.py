# #!/usr/bin/python
# # -*- coding: utf-8 -*-

import MySQLdb
import sys
import ast
import json
import hashlib



db=MySQLdb.connect(user="root",passwd="joubin03",db="votingsystem")
c = db.cursor()
sessions = {}
# sql = "SELECT * from voters_of_america"
# c.execute(sql)
# result = c.fetchall()

# for i in result:
# 	print i
jd = ''
with open("sample.json") as json_file:
    jd = json.load(json_file)
    # print(jd)

# print len(jd)


def loadballets(data):
	for k,v in data.iteritems():
		sql = """INSERT INTO `proposition` (`id`,`proposition_number`,`proposition_question`) VALUES (null,\"{0}\",\"{1}\" );"""
		sql = sql.format(k,v)
		num = c.execute(sql)
		db.commit()
		print "done", num

def loadCandidates(data):
	for k,v in data.iteritems():
		sql = """INSERT INTO `candidates` (`id`,`full_name`,`party_affiliation`) VALUES (null,\"{0}\",\"{1}\" );"""

		sql = sql.format(k,v)
		print sql
		num = c.execute(sql)
		db.commit()
		print "done", num


class VotingSystem:
   
    def __init__(self, data):
        try:
            self.data = json.loads(data)
        except:
            print "data not loaded"
            return
        self.connections = {}

    
    def encrypt_RSA(public_key_loc, message):
        '''
        param: public_key_loc Path to public key
        param: message String to be encrypted
        return base64 encoded encrypted string
        '''
        from Crypto.PublicKey import RSA
        from Crypto.Cipher import PKCS1_OAEP
        key = open(public_key_loc, "r").read()
        rsakey = RSA.importKey(key)
        rsakey = PKCS1_OAEP.new(rsakey)
        encrypted = rsakey.encrypt(message)
        return encrypted.encode('base64')


    def decrypt_RSA(private_key_loc, package):
        '''
        param: public_key_loc Path to your private key
        param: package String to be decrypted
        return decrypted string
        '''
        from Crypto.PublicKey import RSA 
        from Crypto.Cipher import PKCS1_OAEP 
        from base64 import b64decode 
        key = open(private_key_loc, "r").read() 
        rsakey = RSA.importKey(key) 
        rsakey = PKCS1_OAEP.new(rsakey) 
        decrypted = rsakey.decrypt(b64decode(package)) 
        return decrypted

    def sign_data(private_key_loc, data):
        '''
        param: private_key_loc Path to your private key
        param: package Data to be signed
        return: base64 encoded signature
        '''
        from Crypto.PublicKey import RSA 
        from Crypto.Signature import PKCS1_v1_5 
        from Crypto.Hash import SHA256 
        from base64 import b64encode, b64decode 
        key = open(private_key_loc, "r").read() 
        rsakey = RSA.importKey(key) 
        signer = PKCS1_v1_5.new(rsakey) 
        digest = SHA256.new() 
        # It's being assumed the data is base64 encoded, so it's decoded before updating the digest 
        digest.update(b64decode(data)) 
        sign = signer.sign(digest) 
        return b64encode(sign)

    def _generate_RSA(self):
        '''
        Generate an RSA keypair with an exponent of 65537 in PEM format
        param: bits The key length in bits
        Return private key and public key
        '''
        from Crypto.PublicKey import RSA 
        new_key = RSA.generate(2048, e=65537) 
        public_key = new_key.publickey().exportKey("PEM") 
        private_key = new_key.exportKey("PEM")
        f = open('key.public','w')
        f.write(public_key) # python will convert \n to os.linesep
        f.close() # you can omit in most cases as the destructor will call if
        f = open('key.private','w')
        f.write(private_key) # python will convert \n to os.linesep
        f.close() # you can omit in most cases as the destructor will call if
        return private_key, public_key

    def registerToVote(self):
        """
        Data should look like
        -----------------------
        data = '[{"vid":"000lNinon", "pin":"1234", "ssn":"654-46-3894"}]'

        It is encrypted using the servers public key, decrypt it using the private key
        """
        sql = "select vid, ssn, full_name, address, allow_to_vote from votingsystem.voters_of_america where `vid` = \"{}\"".format(self.data[0]['vid'])
        result = c.execute(sql)
        if result:
            result = c.fetchone()
        tmp = self.data[0]
        if (result[0] == tmp['vid'] and result[1] == tmp['ssn']):
            key = hashlib.sha1()
            key.update(tmp['vid'])
            key.update(tmp['ssn'])
            key.update(tmp['pin'])
            vidHash = hashlib.sha1()
            vidHash.update('vid')
            sessions[vidHash.hexdigest()] = key.hexdigest()
            print sessions
            # del(key)
            # del(vidHash)
            # key = hashlib.sha1()
            # key.update(tmp['vid'])
            # vidHash = key.hexdigest()
            # key.update(tmp['ssn'])
            # key.update(tmp['pin'])
            # print vidHash, key.hexdigest()





data = '[{"vid":"000lNinon", "pin":"1234", "ssn":"654-46-3894"}]'

# data = ast.literal_eval(data)

x = VotingSystem(data)

x.registerToVote()
x._generate_RSA()

#END