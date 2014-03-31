import java.io.*;
import java.net.*;

class TestJava
{
  public static void sendData(String s)throws Exception{
    String sentence = s;
    String modifiedSentence;
    Socket clientSocket = new Socket("127.0.0.1", 9999);
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    outToServer.writeBytes(sentence + '\n');
    modifiedSentence = inFromServer.readLine();
    System.out.println("FROM SERVER: " + modifiedSentence);
    clientSocket.close();
  }
  public static void main(String argv[]) throws Exception
  {
    sendData("poop");
  }
}