#!/bin/bash
# (Use #!/bin/sh for sh)
# Run this file to setup what is needed for the backend. s
if [ `id -u` != 0 ] ; then
        echo "This script needs root. Read the file to verify the actions"
        exit 1
fi
apt-get update
apt-get upgrade -y
sudo apt-get install mysql-server -y
sudo apt-get install libmysqlclient-dev python-dev -y
sudo apt-get install python-pip -y
sudo easy_install simplejson -y
sudo apt-get install python-mysqldb -y
sudo apt-get install python-dev -y 
sudo apt-get install python-setuptools -y
sudo ARCHFLAGS='-arch i386 -arch x86_64' easy_install pycrypto


