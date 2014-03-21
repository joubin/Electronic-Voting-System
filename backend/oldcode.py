##oldcode

    def _registerToVote(self):
        """
        Data should look like
        -----------------------
        data = '[{"vid":"000lNinon", "pin":"1234", "ssn":"654-46-3894"}]'

        It is encrypted using the servers public key, decrypt it using the private key
        """
        # decrypt the packet using servers public key
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
            # del(vid_hash, vidHash)
            # key = hashlib.sha1()
            # key.update(tmp['vid'])
            # vidHash = key.hexdigest()
            # key.update(tmp['ssn'])
            # key.update(tmp['pin'])
            # print vidHash, key.hexdigest()