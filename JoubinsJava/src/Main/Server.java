package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;



/**
 * Created by joubin on 4/16/14.
 */
public class Server implements Runnable {
    private static final int port = 9999;
    Socket csocket;
    VotingSystem vs;

    Server(Socket csocket, VotingSystem vs) {
        System.out.print("making a new server\n");
        this.csocket = csocket;
        this.vs = vs;


    }

    public static void main(String args[])
            throws Exception {
        ServerSocket ssock = new ServerSocket(port);
        System.out.println("Listening");
        VotingSystem vs = new VotingSystem();


        while (true) {
            Socket sock = ssock.accept();
            System.out.println("Connected");
            new Thread(new Server(sock, vs)).start();
        }
    }
    public void run() {
        try {

            PrintStream pstream = new PrintStream
                    (csocket.getOutputStream());
/*
My stuff
*/


//            System.out.println("1");
//            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.csocket.getInputStream()));
            InputStream stream = this.csocket.getInputStream();
            byte[] bs = new byte[256];
            int count = stream.read(bs);
//            byte[] bs = this.csocket.getInputStream();
//            System.out.println(count);

//            System.out.println("3");
//            String clientSentence = inFromClient.readLine();
//            System.out.println("4");
//            System.out.println("Received: " + clientSentence);
            String response = null;
            try {
               response =  vs.router(bs);
            } catch (Exception e) {
                System.out.println("there was a sql issue");
                e.printStackTrace();
            }
  /*
  end my stuff
   */

            pstream.println(response);
            pstream.close();
            csocket.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}



