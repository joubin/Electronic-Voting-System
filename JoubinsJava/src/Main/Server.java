package Main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by joubin on 4/16/14.
 */
public class Server implements Runnable {
    private static final int port = 9999;
    Socket csocket;
    Server(Socket csocket) {
        this.csocket = csocket;
    }

    public static void main(String args[])
            throws Exception {
        ServerSocket ssock = new ServerSocket(port);
        System.out.println("Listening");
        while (true) {
            Socket sock = ssock.accept();


            //


            //


            System.out.println("Connected");
            new Thread(new Server(sock)).start();
        }
    }
    public void run() {
        try {

            PrintStream pstream = new PrintStream
                    (csocket.getOutputStream());
/*
My stuff
*/


            System.out.println("1");
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.csocket.getInputStream()));
            System.out.println("2");

            DataOutputStream outToClient = new DataOutputStream(this.csocket.getOutputStream());
            System.out.println("3");
            String clientSentence = inFromClient.readLine();
            System.out.println("4");
            System.out.println("Received: " + clientSentence);


  /*
  end my stuff
   */

                pstream.println(" bottles of beer on the wall");
            pstream.close();
            csocket.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}



