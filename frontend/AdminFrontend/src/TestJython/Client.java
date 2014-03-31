package TestJython;

import java.io.BufferedReader;
import java.io.DataOutputStream;
//import org.spongycastle.openssl.PEMReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;


class Client
{
  private String server;
  private int port;
  private byte[] keyBytes;
  private String myKey;
  private String path_to_key;

  public Client(String server, int port, String path_to_key, String myKey) throws IOException{
    this.server = server;
    this.port = port;
    this.path_to_key = path_to_key;
    this.myKey = myKey;  
  }

//  private String aes_decrypt(String string){ // string is encryptes using myKey
//	  
//  }
  
  public PublicKey readPublicKeyFromFile(String fileName) throws IOException{  
      FileInputStream fis = null;  
      ObjectInputStream ois = null;  
      try {  
          fis = new FileInputStream(new File(fileName));  
          ois = new ObjectInputStream(fis);  
            
          BigInteger modulus = (BigInteger) ois.readObject();  
          BigInteger exponent = (BigInteger) ois.readObject();  
            
          //Get Public Key  
          RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);  
          KeyFactory fact = KeyFactory.getInstance("RSA");  
          PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);  
                        
          return publicKey;  
            
      } catch (Exception e) {  
          e.printStackTrace();  
      }  
      finally{  
          if(ois != null){  
              ois.close();  
              if(fis != null){  
                  fis.close();  
              }  
          }  
      }  
      return null;  
  }  
  
  private byte[] encryptData(String data) throws IOException {  
      System.out.println("\n----------------ENCRYPTION STARTED------------");  
        
      System.out.println("Data Before Encryption :" + data);  
      byte[] dataToEncrypt = data.getBytes();  
      byte[] encryptedData = null;  
      try {  
          PublicKey pubKey = readPublicKeyFromFile(this.path_to_key);  
          Cipher cipher = Cipher.getInstance("RSA");  
          cipher.init(Cipher.ENCRYPT_MODE, pubKey);  
          encryptedData = cipher.doFinal(dataToEncrypt);  
          System.out.println("Encryted Data: " + encryptedData);  
            
      } catch (Exception e) {  
          e.printStackTrace();  
      }     
        
      System.out.println("----------------ENCRYPTION COMPLETED------------");       
      return encryptedData;  
  }  
  

  public String sendData(String s)throws Exception{
    String sentence = s;
    String toSend = encryptData(sentence).toString();
    String modifiedSentence;
    Socket clientSocket = new Socket("127.0.0.1", 9999);
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    outToServer.writeBytes(toSend + '\n');

    modifiedSentence = inFromServer.readLine();
    // return aes_decrypt on modifiedSentence

    System.out.println("FROM SERVER: " + modifiedSentence);

    clientSocket.close();
    return modifiedSentence;
  }
  public static void main(String argv[]) throws Exception
  {
	  String s = "{\"state\":\"register\",\"vid_hash\":\"917ef7e7be4a84e279b74a257953307f1cff4a2e3d221e363ead528c6b556edb\",\"userInfo\":{\"vid\":\"265jMeges\",\"ssn\":\"123-45-6789\",\"pin\":\"1234\"}}";
	  
  }
 
}
