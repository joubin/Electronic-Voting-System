import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class Connection {
	private String host = "24.10.116.146";
	private int port = 9999;
	private VSCrypt toolKit;
	private JSONObject ballot;
	private byte[] finalKey;
	private byte[] vid_hash;
	
	public Connection(){
		toolKit = new VSCrypt();
		StringBuffer instr = new StringBuffer();
		String timestamp;
		System.out.println("SocketClient Initialized");
	}
	
	@SuppressWarnings("unchecked")
	public boolean start(String[] loginInfo){
		try{
			Socket clientSocket = new Socket(host, 9999);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            /*
            Example get Shared key
             */
            VSCrypt toolKit = new VSCrypt();
            String[] s = {loginInfo[0]};
            vid_hash = toolKit.mySha256(s);
            String[] k = {"somethingRandom"}; // generate somethign random so that it can be used to generate a shared key
            byte[] somethingRandom_Hash = toolKit.mySha256(k); // get the hash of it

            String vid_hashString = new String(vid_hash); // string value of vid hash for testing
            String sometingRandom_string = new String(somethingRandom_Hash); //string value of the random for testing
            String sendData = vid_hashString+","+sometingRandom_string; // comma separate them so that the server knows who this is for.
            outToServer.write(toolKit.rsaEncrypt(sendData.getBytes())); // send it to server
            // Data was sent using RSA encrypt this is the first stage
            //From here
            String returnedString = inFromServer.readLine(); // we get back the other random thing
            String[] keyset = {new String(sendData.split(",")[1]), returnedString}; // we need the second part of the string we sent,
                                                                                    //because that is what the server will use to create a hash
            finalKey = toolKit.mySha256(keyset); // get the final shard key using the same set of the data as the server
            System.out.print("the new key is "+new String(finalKey)); // we now have a shared key to use for aes
            if (finalKey.length != 128){
                System.out.print("\n key size is: " + finalKey.length);
            }
            /*
            End Example get Shared key
             */

            //sample data on how to use aes to encrypt to the server
            String vid = loginInfo[0];
            String ssn = loginInfo[1];
            String pin = loginInfo[2];
            clientSocket = new Socket(host, 9999); // make a second connection
            outToServer = new DataOutputStream(clientSocket.getOutputStream()); // setup output pipe
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // setup input pipe
            JSONObject dataToSend = new JSONObject(); // make a json to send
            System.out.println("\nThis is my vid hash "+ new String(vid_hash));
            dataToSend.put("vid_hash", new String(vid_hash)); // packet to go out. Do not encrypt
            System.out.println("going to do aes encrypt");

            JSONObject data = new JSONObject();

            JSONObject userInfo = new JSONObject();
            userInfo.put("vid", vid);
            userInfo.put("ssn", ssn);
            userInfo.put("pin", pin);
            data.put("userInfo", userInfo);
            data.put("state", "register");
            byte[] encryptedData = toolKit.encrypt(finalKey, data.toString());
            dataToSend.put("data", new String(encryptedData));
//            outToServer.write(toolKit.encrypt(finalKey, dataToSend.toJSONString()));
            outToServer.write(dataToSend.toString().getBytes());
            returnedString = inFromServer.readLine();
            System.out.println(returnedString);
            JSONObject returnedJson = stringToJson(returnedString);
            System.out.println(returnedJson.toJSONString());
            
            String string = new String(returnedJson.get("data").toString());
            Map<Object, Object> activeUsers = new HashMap<Object, Object>();
            String normalized_string = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\u0000", "");
            System.out.println(normalized_string);
            //JSONObject packetObject = stringToJson(normalized_string);
            //String vid_hash2 = packetObject.get("vid_hash").toString();
            //String userinfo = new String(packetObject.get("data").toString());
            //byte[] key = activeUsers.get(vid_hash2).toString().getBytes();
            byte[] decryptedBytes = toolKit.decrypt(finalKey, normalized_string);
            String decryptedStuff = new String(decryptedBytes);
            ballot = new JSONObject();
            ballot = stringToJson(decryptedStuff.toString());
            System.out.println("key is: "+new String(finalKey)+"\nvid_hash is: "+vid_hash +"\nuser info is "+ballot.toJSONString());
            //packetObject = userInfoObject;
            
			return true;
		}catch(Exception e){
			System.out.println("this"+e.getStackTrace());
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean sendBallot(JSONObject finalBallot){
		try{
			JSONObject dataToSend = new JSONObject();
			Socket clientSocket = new Socket(host, 9999);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			byte[] encryptedData = toolKit.encrypt(finalKey, finalBallot.toString());
			
			dataToSend.put("vid_hash", new String(vid_hash));
			dataToSend.put("data", new String(encryptedData));
			System.out.println("Ballot being sent: " + dataToSend.toString());
			//byte[] dataToSend = ballotToSend.toString().getBytes();
			//System.out.println(dataToSend.length);
			outToServer.write(dataToSend.toString().getBytes());
            String returnedString = inFromServer.readLine();
            
            System.out.println(returnedString);
            JSONObject returnedJson = stringToJson(returnedString);
            System.out.println(returnedJson.toJSONString());
            
            /*String string = new String(returnedJson.get("data").toString());
            Map<Object, Object> activeUsers = new HashMap<Object, Object>();
            String normalized_string = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\u0000", "");
            System.out.println(normalized_string);
            //JSONObject packetObject = stringToJson(normalized_string);
            //String vid_hash2 = packetObject.get("vid_hash").toString();
            //String userinfo = new String(packetObject.get("data").toString());
            //byte[] key = activeUsers.get(vid_hash2).toString().getBytes();
            byte[] decryptedBytes = toolKit.decrypt(finalKey, normalized_string);
            String decryptedStuff = new String(decryptedBytes);
            ballot = new JSONObject();
            ballot = stringToJson(decryptedStuff.toString());
            System.out.println("key is: "+new String(finalKey)+"\nvid_hash is: "+vid_hash +"\nuser info is "+ballot.toJSONString());
			*/
			return true;
		}catch(Exception e){
			System.out.println("this"+e.getStackTrace());
			return false;
		}
	}
	
	public static JSONObject stringToJson(String s){
        JSONObject myNewString = null;
        try {
            myNewString =   (JSONObject)new JSONParser().parse(s);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not read json");
        }
        return myNewString;
    }
	
	public JSONObject getBallot(){
		return ballot;
	}
}
