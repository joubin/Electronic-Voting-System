#!/usr/bin/python
# -*- coding: utf-8 -*-

import MySQLdb
import sys
import json
db=MySQLdb.connect(user="root",passwd="hello123!@#",db="votingsystem")
c = db.cursor()
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
for key,delta in jd.iteritems():
	if key == "presidents":
		loadCandidates(delta)
	if key == "props":
		loadballets(delta)

