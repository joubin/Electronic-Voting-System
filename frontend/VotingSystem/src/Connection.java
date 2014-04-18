import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection {
	private String host = "10.114.105.175";
	private int port = 9999;
	private VSCrypt toolKit;
	
	public Connection(){
		toolKit = new VSCrypt();
		StringBuffer instr = new StringBuffer();
		String timestamp;
		System.out.println("SocketClient Initialized");
	}
	
	public boolean start(String[] loginInfo){
		try{
			System.out.println(loginInfo[0]+"\n"+loginInfo[1]+"\n"+loginInfo[2]);
			Socket serverSocket = new Socket(host, port);
			DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			
	        String[] s = {loginInfo[0]};
            byte[] vid_hash = toolKit.mySha256(s);
            String[] k = {"somethingRandom"}; // generate something random so that it can be used to generate a shared key
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
            byte[] finalKey = toolKit.mySha256(keyset); // get the final shared key using the same set of the data as the server
            System.out.print("the new key is "+new String(finalKey)); // we now have a shared key to use for aes
            if (finalKey.length != 128){
                System.out.print("\n key size is: " + finalKey.length);
            }
            
            serverSocket = new Socket(host, port);
            outToServer = new DataOutputStream(serverSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            /*
            String[] p = {loginInfo[1]};
            String[] ss = {loginInfo[2]};
            byte[] pin = toolKit.mySha256(p);
            byte[] ssn = toolKit.mySha256(ss);
            JSONObject login = new JSONObject();
            login.put("vid_hash", new String(vid_hash));
            JSONObject userInfo = new JSONObject();
            userInfo.put("vid", loginInfo[0]);
            userInfo.put("ssn", ssn);
            userInfo.put("pin", pin);
            
            String userString = userInfo.toJSONString();
            byte[] encryptedUserInfo = toolKit.encrypt(finalKey, userString);
            login.put("userinfo", new String(encryptedUserInfo));
            
            System.out.println(login.toJSONString());
            
            outToServer.write(login.toJSONString().getBytes());
            
            returnedString = inFromServer.readLine();
            
            System.out.println(returnedString);*/
            
            String vid = loginInfo[0];
            String ssn = loginInfo[1];
            String pin = loginInfo[2];
            /*serverSocket = new Socket("localhost", 9999); // make a second connection
            outToServer = new DataOutputStream(serverSocket.getOutputStream()); // setup output pipe
            inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream())); // setup input pipe*/
            JSONObject dataToSend = new JSONObject(); // make a json to send
            System.out.println("\nThis is my vid hash "+ new String(vid_hash));
            dataToSend.put("vid_hash", new String(vid_hash)); // packet to go out. Do not encrypt
            System.out.println("going to do aes encrypt");
            JSONObject data = new JSONObject();
            data.put("vid", vid);
            data.put("ssn", ssn);
            data.put("pin", pin);
            byte[] encryptedData = toolKit.encrypt(finalKey, data.toJSONString());
            dataToSend.put("userInfo", new String(encryptedData));
//            outToServer.write(toolKit.encrypt(finalKey, dataToSend.toJSONString()));
            outToServer.write(dataToSend.toJSONString().getBytes());
            returnedString = inFromServer.readLine();
            System.out.print("\n finally we have: " + new String(finalKey));
            System.out.println(returnedString);
            
			return true;
		}catch(Exception e){
			System.out.println("this"+e.getStackTrace());
			return false;
		}
	}
}
