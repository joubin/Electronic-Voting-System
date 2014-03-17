#!/bin/bash
# (Use #!/bin/sh for sh)
if [ `id -u` != 0 ] ; then
        echo "This app needs to be run as root"
        exit 1
fi
apt-get update
apt-get upgrade -y
sudo apt-get install libmysqlclient-dev python-dev -y
sudo apt-get install python-pip -y
sudo easy_install simplejson