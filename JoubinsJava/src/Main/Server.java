package Main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
            System.out.println("Connected");
            new Thread(new Server(sock)).start();
        }
    }
    public void run() {
        try {
            BufferedReader inFromClient;
            inFromClient = new BufferedReader(csocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            PrintStream pstream = new PrintStream
                    (csocket.getOutputStream());
            for (int i = 100; i >= 0; i--) {
                pstream.println(i +
                        " bottles of beer on the wall");
            }
            pstream.close();
            csocket.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
