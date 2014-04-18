package Main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;

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

    public synchronized String router(byte[] packet) throws Exception {
        String decryptedPacket = null;
        JSONObject packetObject = null;
        String vid_hash = null;
        String packetAsString = new String(packet);

        try {
            if (packetAsString.contains("vid_hash")){
                System.out.print("AES Encrypted");
                System.out.print("Doing the right thing so ill exit");
                String string = new String(packet);
                String normalized_string = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\u0000", "");
                System.out.println(normalized_string);
                packetObject = stringToJson(normalized_string);
                vid_hash = packetObject.get("vid_hash").toString();
                String userinfo = new String(packetObject.get("data").toString());
                byte[] key = activeUsers.get(vid_hash).toString().getBytes();
                byte[] decryptedBytes = cryptoToolKit.decrypt(key, userinfo);
                String decryptedStuff = new String(decryptedBytes);
                JSONObject userInfoObject = new JSONObject();
                userInfoObject = stringToJson(decryptedStuff.toString());
                System.out.println("key is: "+new String(key)+"\nvid_hash is: "+vid_hash +"\nuser info is "+userInfoObject.toJSONString());
                packetObject = userInfoObject;

            }else{
            byte[] decryptedByteArray = cryptoToolKit.rsaDecrypt(Arrays.copyOf(packet, 256));
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
            System.out.println("Could not decrypt\n" + packetAsString);
            System.out.println(packetAsString.getBytes().length);
            return null;
        }
        String state;
        state = packetObject.get("state").toString();


        if (state.equals("register")) {
            // run register
            return this.register(packetObject, vid_hash);
        }else if (state.equals("ballot_response")){
            return this.ballot_response(packetObject, vid_hash);
        }else if (state.equals("getReuslts")){
            return null;
        }
        return null;
    }

    private String register(JSONObject packet, String vid_hash) throws Exception {
        String user_hash = vid_hash;
        JSONObject userInfo = (JSONObject) packet.get("userInfo");
        String userProvided_pin = userInfo.get("pin").toString();
        String userProvided_vid = userInfo.get("vid").toString();
        String userProvided_ssn = userInfo.get("ssn").toString();
        JSONObject returnPacket = new JSONObject();
        returnPacket.put("vid_hash", user_hash);

        DB_handler db = new DB_handler();
        String sql;
        sql = "select vid, ssn from votingsystem.voters_of_america where `vid_hash` = \"" + user_hash + "\";" ;
        ResultSet result = db.getResult(sql);
        int runs = 0;

        String dbReturn_vid = null;
        String dbReturn_ssn = null;

        while (result.next()){
            runs++;
            dbReturn_vid = new String(cryptoToolKit.decrypt(userProvided_pin.getBytes(), result.getString("vid")));
            dbReturn_ssn = new String(cryptoToolKit.decrypt(userProvided_pin.getBytes(), result.getString("ssn")));
        }
        db.cleanup();

        if (runs == 0 || runs > 1){
            System.out.println("There were either too many things or not enough " + runs);
        }else {
            System.out.println("Was able to get stuff for this user and I only found one");
            boolean vidMatches = userProvided_vid.compareTo( dbReturn_vid) == 0;
            boolean ssnMatches = userProvided_ssn.compareTo(dbReturn_ssn) == 0;
            if (vidMatches && ssnMatches){
                JSONObject ballot = setupBallot();
//                returnPacket.put("proposition", ballot.get("proposition"));
//                returnPacket.put("presidential_candidates", ballot.get("presidential_candidates"));
                ballot.put("state", "ballot");
                String returnPacketString = returnPacket.toString();
                try {
                   byte[] encryptedData = cryptoToolKit.encrypt(activeUsers.get(vid_hash).toString().getBytes(), ballot.toString());
                    returnPacket.put("data", new String(encryptedData));
                    System.out.println("\nSending the ballots");
                    return returnPacket.toString();
                } catch (Exception e) {
                    System.out.println("Could not encrypt the string going out client " + e);
                    e.printStackTrace();
                }
            }else{
                activeUsers.remove(vid_hash);
                System.out.println("the provided info was wrong");
            }
        }




        return returnPacket.toJSONString();

    }


    private String ballot_response(JSONObject packet, String vid_hash){
        ArrayList proposition = (ArrayList) packet.get("proposition");
        ArrayList presidential_candidates = (ArrayList) packet.get("presidential_candidates");
        int runs = 0;
        int flags = 0;
        for (Object o : proposition){
            runs++;
            JSONObject tmp = stringToJson(o.toString());
            String proposition_number = tmp.get("proposition_number").toString();
            String answer = tmp.get("answer").toString();
            String id = tmp.get("id").toString();
            DB_handler db = new DB_handler();
            java.util.Date date= new java.util.Date();

            String sql = "insert into prop_votes values(\""+vid_hash+"\", \""+answer+"\", \""+new Timestamp(date.getTime())+"\", \""+id+"\")";
            boolean insert = db.setValues(sql);
            if (insert)
                  flags++;
            db.cleanup();

        }
         int runs2 = 0;
        int flags2 = 0;
        boolean winner = false;
        String full_name = null;
        String id = null;
        for (Object o : presidential_candidates){
            runs2++;
            JSONObject tmp = stringToJson(o.toString());
            if(tmp.get("pick").toString().compareTo("true") == 0){
               if (!winner){
                   winner = true;
                   full_name = tmp.get("full_name").toString();
                   id = tmp.get("id").toString();
               }
               else{
                   return null;
               }
            }

        }
        DB_handler db = new DB_handler();
        String sql = "insert into candidate_votes values(\""+vid_hash+"\",\""+id+"\")";
        boolean presidentSet = db.setValues(sql);
        db.cleanup();
        boolean checkProps = runs == flags;
        if (presidentSet && checkProps){
            // make sure this person can not vote again
            db = new DB_handler();
            sql = "update voters_of_america set allowed_to_vote = 0 where vid_hash = \""+vid_hash+"\"";
            db.setValues(sql);
            db.cleanup();
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("vid_hash", vid_hash);
            JSONObject data = new JSONObject();
            data.put("state", "accepted");
            try {
                byte[] dataEncrypted = cryptoToolKit.encrypt(activeUsers.get(vid_hash).toString().getBytes(), data.toString());
                dataToSend.put("data", new String(dataEncrypted));
                String returnVal = new String(dataToSend.toJSONString());
                return returnVal;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private JSONObject setupBallot()  {
        JSONObject ballotObject = new JSONObject();
        ArrayList propositions = new ArrayList();
        ArrayList presidents = new ArrayList();

        DB_handler db = new DB_handler();
        String sql = "select * from proposition limit 10;";

        ResultSet ballotsTabel = db.getResult(sql);

        try {
            while (ballotsTabel.next()){
                JSONObject tmp = new JSONObject();
                tmp.put("id", ballotsTabel.getInt("id"));
                tmp.put("proposition_number", ballotsTabel.getString("proposition_number"));
                tmp.put("question", ballotsTabel.getString("proposition_question"));
                propositions.add(tmp);

            }
            System.out.println(propositions.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.cleanup();
        db = new DB_handler();
        sql = "select * from candidates;";

        ResultSet presidentsTable = db.getResult(sql);

        try {
            while (presidentsTable.next()){
                JSONObject tmp = new JSONObject();
                tmp.put("id", presidentsTable.getString("id"));
                tmp.put("full_name", presidentsTable.getString("full_name"));
                tmp.put("party_affiliation", presidentsTable.getString("party_affiliation"));
                presidents.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.cleanup();


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
