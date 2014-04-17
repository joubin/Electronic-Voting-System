package Main;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by joubin on 4/16/14.
 */
public class VotingSystem {
    private  Map<Object, Object> activeUsers = null;
    private  static VSCrypt cryptoToolKit = null;
    public VotingSystem(){
        activeUsers = new HashMap<Object, Object>();
        cryptoToolKit = new VSCrypt();
    }

    private synchronized String getKeyForUser(String user){
        return activeUsers.get(user).toString();
    }

    private synchronized void setKeyForUser(String user, String key){
        activeUsers.put(user, key);
    }

    public synchronized String router(byte[] packet) throws SQLException {
        String decryptedPacket = null;
        JSONObject packetObject = null;
        try {
            String packetAsString = new String(packet);
            if (packetAsString.contains("vid_hash")){
                System.out.print("AES Encrypted");
                System.out.print("Doing the right thing so ill exit");
                String string = new String(packet);
                String normalized_string = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\u0000", "");
                System.out.println(normalized_string);
                packetObject = stringToJson(normalized_string);
                String vid_hash = packetObject.get("vid_hash").toString();
                String userinfo = new String(packetObject.get("userInfo").toString());
                byte[] key = activeUsers.get(vid_hash).toString().getBytes();
                byte[] decryptedBytes = cryptoToolKit.decrypt(key, userinfo);
                String decryptedStuff = new String(decryptedBytes);
                JSONObject userInfoObject = new JSONObject();
                userInfoObject = stringToJson(decryptedStuff.toString());
                System.out.println("key is: "+new String(key)+"\nvid_hash is: "+vid_hash +"\nuser info is "+userInfoObject.toJSONString());
                System.out.println(decryptedPacket);
                System.exit(99);

            }else{
            byte[] decryptedByteArray = cryptoToolKit.rsaDecrypt(packet);
                Random rand = new Random();
                String incoming = new String(decryptedByteArray);
                String[] incomingArr = incoming.split(",");
                Integer random = rand.nextInt();
                String myRandom = random.toString().getBytes().toString();
                String outGoing = myRandom;
                String[] keySet = {incomingArr[1], outGoing};
                byte[] myKey = cryptoToolKit.mySha256(keySet);
                String myKeyString = new String(myKey);
                setKeyForUser(incomingArr[0], myKeyString);
                System.out.print("for user "+incomingArr[0]+"key is "+myKeyString);
                return outGoing;
            }
            System.out.print("asdasdasd is " + decryptedPacket);
//            packetObject = stringToJson(decryptedPacket.toString());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not decrypt");
            return null;
        }
        String s;
        s = packetObject.get("state").toString();


        if (s.equals("register")) {
            // run register
            this.register(packetObject);
        }else if (s.equals("ballot_response")){
            //run ballot_response
        }else if (s.equals("getReuslts")){
            //send final results
        }
        return null;
    }

    private byte[] register(JSONObject packet) throws SQLException {
        String user_hash = packet.get("vid_hash").toString();
        JSONObject userInfo = (JSONObject) packet.get("userInfo");
        String userProvided_pin = userInfo.get("pin").toString();
        String userProvided_vid = userInfo.get("vid").toString();
        String userProvided_ssn = userInfo.get("ssn").toString();
        JSONObject returnPacket = new JSONObject();
        returnPacket.put("state", "ballot");
        returnPacket.put("vid_hash", user_hash);
        char[] sharedKey = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(userProvided_vid.getBytes());
            digest.update(userProvided_ssn.getBytes());
            digest.update(userProvided_pin.getBytes());
            sharedKey = Hex.encodeHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            System.out.print("Could not establish shared key");
            e.printStackTrace();
        }
        activeUsers.put(userProvided_vid, sharedKey);
        DB_handler db = new DB_handler();
        String sql;
        sql = "select vid, ssn from votingsystem.voters_of_america where `vid_hash` = " + user_hash + ";" ;
        ResultSet result = db.getResult(sql);
        db.cleanup();
        int runs = 0;

        String dbReturn_vid = null;
        String dbReturn_ssn = null;

        while (result.next()){
            runs++;
            dbReturn_vid = result.getString("vid");
            dbReturn_ssn = result.getString("ssn");
        }
        if (runs == 0 || runs > 1){
            System.out.println("There were either too many things or not enough " + runs);
        }else {
            System.out.println("Was able to get stuff for this user and I only found one");
            boolean vidMatches = userProvided_vid == dbReturn_vid;
            boolean ssnMatches = userProvided_ssn == dbReturn_ssn;
            if (vidMatches && ssnMatches){
                JSONObject ballot = setupBallot();
                returnPacket.put("proposition", ballot.get("proposition"));
                returnPacket.put("presidential_candidates", ballot.get("presidential_candidates"));
                String returnPacketString = returnPacket.toString();
                try {
                   return cryptoToolKit.encrypt((new String(sharedKey)).getBytes(), returnPacketString);
                } catch (Exception e) {
                    System.out.println("Could not encrypt the string going out client " + e);
                    e.printStackTrace();
                }
            }else{
                // the user pin was wrong
            }
        }




        return returnPacket.toJSONString().getBytes();

    }


    private JSONObject setupBallot() throws SQLException {
        JSONObject ballotObject = new JSONObject();
        ArrayList propositions = new ArrayList();
        ArrayList presidents = new ArrayList();

        String sql = "select * from proposition;";
        DB_handler db = new DB_handler();
        ResultSet ballotsTabel = db.getResult(sql);
        sql = "select * from candidates;";
        ResultSet presidentsTable = db.getResult(sql);
        db.cleanup();

        while (ballotsTabel.next()){
            JSONObject tmp = new JSONObject();
            tmp.put("id", ballotsTabel.getArray(0));
            tmp.put("proposition_number", ballotsTabel.getArray(1));
            tmp.put("id", ballotsTabel.getArray(2));
            propositions.add(tmp);

        }
        while (presidentsTable.next()){
            JSONObject tmp = new JSONObject();
            tmp.put("id", presidentsTable.getArray(0));
            tmp.put("proposition_number", presidentsTable.getArray(1));
            tmp.put("id", presidentsTable.getArray(2));
            presidents.add(tmp);
        }


        ballotObject.put("proposition", propositions);
        ballotObject.put("presidential_candidates", presidents);

        return ballotObject;
    }

    private JSONObject stringToJson(String s){
        JSONObject myNewString = null;
        try {
             myNewString =   (JSONObject)new JSONParser().parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Could not read json");
        }
        return myNewString;
    }

}
