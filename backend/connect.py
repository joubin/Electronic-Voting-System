# #!/usr/bin/python
# # -*- coding: utf-8 -*-

import sys
import ast
import json
import hashlib
from Crypto.Hash import SHA256
import sampleAESEncDec 

xx = sampleAESEncDec.RunAES()
#setup DB
import MySQLdb

db=MySQLdb.connect(user="root",passwd="joubin03",db="votingsystem")
c = db.cursor()
sessions = {}


# for i in result:
# 	print i
jd = ''
with open("sample.json") as json_file:
    jd = json.load(json_file)
    # print(jd)

# print len(jd)

def getballots():
    sql = "select * from proposition;"
    result = c.execute(sql)
    if result:
        result = c.fetchall()
    result_as_dict = []
    for r in result:
        tmp = {
            'id' : r[0],
            'proposition_number' : r[1],
            'question' : r[2]}
        result_as_dict.append(tmp)
    print json.dumps(result_as_dict)
    f = open('propjsonDump.txt', 'w+')
    f.write(str(result_as_dict))
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


class VotingSystem(object):
    """docstring for VotingSystem"""
    def __init__(self):
        super(VotingSystem, self).__init__()
        global RunAES
        self.options = {"register" : self._registerToVote,
                "submit_votes" : self._submit_votes
                }
        self.rsaKey = open("key.private").read()
        self.aes = xx


        # Do we need to check md5 or whatever?



    
    def _encrypt_RSA(self, public_key_loc, message):
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


    def _decrypt_RSA(self, private_key_loc, package):
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

    def _sign_data(self, private_key_loc, data):
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

    def _registerToVote(self, packet):
        myPacket = json.loads(packet)
        user_hash = myPacket[0]["vid_hash"]
        sql = "select vid, ssn from votingsystem.voters_of_america where `vid_hash` = \"{}\"".format(user_hash)
        result = c.execute(sql)
        if not result:
            #send error code that the user was not found and they need to go to a goverment office to register.
            print "wrong user"
            pass
        else: 
            result = c.fetchone()
            # may have to do 
            #result = result[0]
            user_pin = packet[1]["pin"]
            user_vid = packet[1]["vid"]
            user_ssn = packet[1]["ssn"]
            vid_matches = user_vid == self.aes.decrypt(user_pin, result[0])
            ssn_matches = user_ssn == self.aes.decrypt(user_pin, result[1])
            vid_hash_matches = user_hash == self.aes.sha255Item(user_vid)
            if vid_matches and ssn_matches and vid_hash_matches:
                myHash = SHA256.new()
                myHash.update(user_vid)
                myHash.update(user_ssn)
                myHash.update(user_pin)
                sharedKey = myHash.digest()
                self.connections[user_vid] = sharedKey
                # TODO
                # getBallot
                # encryptBallot with sharedKey
                # sendBallot

                # remove Test code
                print "Good"
            else:
                # error that the pin, social or the user was incorrect
                print "Wrong information"
                return 






            return

        # search the db for what is in packet[0]["vid_hash"]
        # if not found
            # return wrong user id
        # if found
            # use pin to decrypt each item from the select
            # compare each item from the select to each item supplied in packet[1]
            # if the items match
                # add an entry to connections[vid_hash] = Hash_sha1(vid+ssn+pin)
                # send the ballot template encrypted using Hash_sha1(vid+ssn+pin)
            #if the items dont match
                # return some of the items provided didnt match

    def _submit_votes(self, packet):
        pass


    def caller(self, packet):
        packet = self._decrypt_RSA(self.rsaKey, data)
        self.options[str(packet[0]["state"])](packet)




if __name__ == '__main__':
    # vs = VotingSystem()
    # vs._registerToVote("data")
    print getballots()
