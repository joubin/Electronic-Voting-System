package Main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by joubin on 4/16/14.
 */
class Client
{


        public static void main(String argv[]) throws Exception
        {
//            VSCrypt toolKit = new VSCrypt();
//            String[] test123 = {"265jMeges"};
//            byte[] vid_hash_test = toolKit.mySha256(test123);
//            System.out.println(new String(vid_hash_test));
//            System.exit(1);

//            byte[] sharedKey = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16};
//            System.out.print(sharedKey.length);
//            System.exit(1);
            //Connect to the server
            Socket clientSocket = new Socket("localhost", 9999);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            /*
            Example get Shared key
             */
            VSCrypt toolKit = new VSCrypt();
            String[] s = {"265jMeges"};
            byte[] vid_hash = toolKit.mySha256(s);
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
            byte[] finalKey = toolKit.mySha256(keyset); // get the final shard key using the same set of the data as the server
            System.out.print("the new key is "+new String(finalKey)); // we now have a shared key to use for aes
            if (finalKey.length != 128){
                System.out.print("\n key size is: " + finalKey.length);
            }
            /*
            End Example get Shared key
             */

            //sample data on how to use aes to encrypt to the server
            String vid = "265jMeges";
            String ssn = "700-33-6870";
            String pin = "1234";
            clientSocket = new Socket("localhost", 9999); // make a second connection
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
        }



    public static JSONObject stringToJson(String s){
        JSONObject myNewString = null;
        try {
            myNewString =   (JSONObject)new JSONParser().parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Could not read json");
        }
        return myNewString;
    }
//        public void something() throws Exception {
//            JSONObject objectToEncrypt =  new JSONObject();
//           String[] myVIDHash = {"265jMeges"};
//
//            objectToEncrypt.put("state", "register");
////            objectToEncrypt.put("vid_hash", sharedKey);
//            JSONObject userInfo = new JSONObject();
//            userInfo.put("vid", "265jMeges" );
//            userInfo.put("ssn", "700-33-6870");
//            userInfo.put("pin", "1234");
//            objectToEncrypt.put("userInfo", userInfo);
//            VSCrypt cryptoToolkit = new VSCrypt();
//            byte[] dataTosend = cryptoToolkit.rsaEncrypt(sharedKey.toString().getBytes());
//            int s = dataTosend.length;
//            System.out.print("\n\ndata to send is " + dataTosend  +"  and the shared key is "+ digest.digest());
//
//            Socket clientSocket = new Socket("localhost", 9999);
//            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
////            outToServer.writeBytes(dataTosend);
//
//            outToServer.write(dataTosend);
//            String modifiedSentence = inFromServer.readLine();
//            System.out.println("\n\nFROM SERVER: " + modifiedSentence);
//            clientSocket.close();
//        }
}
