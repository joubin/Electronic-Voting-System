package Main;

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
            //Connect to the server
            Socket clientSocket = new Socket("localhost", 9999);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            /*
            Example get Shared key
             */
            VSCrypt toolKit = new VSCrypt();
            String[] s = {"265jMeges"};
            byte[] vid_hash = toolKit.sha256digest(s);
            String[] k = {"somethingRandom"};
            byte[] somethingRandom_Hash = toolKit.sha256digest(k);

            String vid_hashString = new String(vid_hash);
            String sometingRandom_string = new String(somethingRandom_Hash);
            String sendData = vid_hashString+","+sometingRandom_string;
            outToServer.write(toolKit.rsaEncrypt(sendData.getBytes()));

            //From here
            String returnedString = inFromServer.readLine();
            String[] keyset = {new String(sendData), returnedString};
            byte[] finalKey = toolKit.sha256digest(keyset);
            System.out.print("the new key is "+new String(finalKey));
            /*
            End Example get Shared key
             */

            String vid = "265jMeges";
            String ssn = "700-33-6870";
            String pin = "123";


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
