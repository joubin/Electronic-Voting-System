import java.net.*;
import java.io.*;

public class Connection {
	private String host = "localhost";
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
			/*InetAddress address = InetAddress.getByName(host);
			Socket serverSocket = new Socket(address, port);
			DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			*/
	        //String[] s = {"265jMeges"};
            byte[] vid_hash = toolKit.mySha256(loginInfo);
            String[] k = {"somethingRandom"}; // generate something random so that it can be used to generate a shared key
            byte[] somethingRandom_Hash = toolKit.mySha256(k); // get the hash of it
            
            String vid_hashString = new String(vid_hash); // string value of vid hash for testing
            String sometingRandom_string = new String(somethingRandom_Hash); //string value of the random for testing
            String sendData = vid_hashString+","+sometingRandom_string; // comma separate them so that the server knows who this is for.
            /*outToServer.write(toolKit.rsaEncrypt(sendData.getBytes())); // send it to server
            // Data was sent using RSA encrypt this is the first stage
            //From here
            String returnedString = inFromServer.readLine(); // we get back the other random thing
            String[] keyset = {new String(sendData.split(",")[1]), returnedString}; // we need the second part of the string we sent,
                                                                                    //because that is what the server will use to create a hash
            byte[] finalKey = toolKit.mySha256(keyset); // get the final shard key using the same set of the data as the server
            System.out.print("the new key is "+new String(finalKey)); // we now have a shared key to use for aes
            if (finalKey.length != 128){
                System.out.print("\n key size is: " + finalKey.length);
            }*/
	        
			return true;
		}catch(Exception e){
			System.out.println("this"+e.getStackTrace());
			return false;
		}
	}
}
