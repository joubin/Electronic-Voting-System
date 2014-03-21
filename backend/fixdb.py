import MySQLdb
from Crypto.Hash import SHA256

def fixMissingHash():
    db=MySQLdb.connect(user="root",passwd="joubin03",db="votingsystem")
    c = db.cursor()

    sql = "select * from voters_of_america"

    result = c.execute(sql)

    if result:
        result = c.fetchall()
        for i in result:
            myHash = SHA256.new()
            myHash.update(i[1])
            sharedKey = myHash.hexdigest()
            sql = "update  voters_of_america set VID_HASH=\"{1}\" where VID = \"{0}\"".format(i[1], sharedKey)
            c.execute(sql)
            db.commit()

def addHashesToBaseFile():
    db=MySQLdb.connect(user="root",passwd="joubin03",db="votingsystem")
    c = db.cursor()
    f = open("../tempdata/voters_of_america_new.sql", 'a+')
    with open("../tempdata/voters_of_america.sql", 'r') as template:
        for i in template:
            myHash = SHA256.new()
            myHash.update(i[113:122])
            sharedKey = myHash.hexdigest()
            f.write(i.format("\'"+sharedKey+"\'"))

def encryptDB():
    db=MySQLdb.connect(user="root",passwd="joubin03",db="votingsystem")
    c = db.cursor()
    from sampleAESEncDec import RunAES
    myAES = RunAES()
    sql = "select * from voters_of_america"

    result = c.execute(sql)
    if result:
        result = c.fetchall()
        for i in result:
            pin = "1234"
            vid = myAES.encrypt(pin, i[1])
            ssn = myAES.encrypt(pin, i[2])
            name =  myAES.encrypt(pin, i[3])
            address =  myAES.encrypt(pin, i[4])
            allowed = myAES.encrypt(pin, str(i[5]))
            sql = " update voters_of_america set VID = \"{1}\", SSN = \"{2}\", full_name = \"{3}\", address = \"{4}\", allow_to_vote = 1 where VID_HASH = \"{0}\"".format(i[0], vid, ssn, name, address, allowed)
            c.execute(sql)
            db.commit()
            
            
def decryptDB():
    db=MySQLdb.connect(user="root",passwd="joubin03",db="votingsystem")
    c = db.cursor()
    from sampleAESEncDec import RunAES
    myAES = RunAES()
    sql = "select * from voters_of_america"

    result = c.execute(sql)
    if result:
        result = c.fetchall()
        for i in result:
            pin = "1234"
            vid = myAES.decrypt(pin, i[1])
            ssn = myAES.decrypt(pin, i[2])
            name =  myAES.decrypt(pin, i[3])
            address =  myAES.decrypt(pin, i[4])
            print vid, ssn, name, address, "\n"
            

#encryptDB()
decryptDB()