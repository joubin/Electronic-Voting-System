import java.io.*;
import java.net.*;

class client
{
  String server;
  int port;
  String publicKeyFile;
  String myKey;

  public client(String server, int port, String path_to_key, String myKey){
    this.server = server;
    this.port = port;
    this.publicKeyFile = OPEN(path_to_key, 'r').read();
    this.mykey = myKey;  
  }
  public String aes_decrypt(String string){ // string is encryptes using myKey
    System.out.println("Decrypting ... ");
  }
  public String rsa_encrypt(String string){ // encrypt it using the public key of the server
    System.out.println("Encrypting ... ");
  }
  public String sendData(String s)throws Exception{
    String sentence = s;
    //run rsa_encrypt on sentence.
    String modifiedSentence;
    Socket clientSocket = new Socket("127.0.0.1", 9999);
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    outToServer.writeBytes(sentence + '\n');

    modifiedSentence = inFromServer.readLine();
    // return aes_decrypt on modifiedSentence

    System.out.println("FROM SERVER: " + modifiedSentence);

    clientSocket.close();
    return modifiedSentence
  }
  /*
  public static void main(String argv[]) throws Exception
  {
    sendData("poop");
  }
  */
}