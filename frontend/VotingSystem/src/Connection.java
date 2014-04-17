import java.net.*;
import java.io.*;

public class Connection {
	private String host = "localhost";
	int port = 9999;
	
	public Connection(){
		StringBuffer instr = new StringBuffer();
		String timestamp;
		System.out.println("SocketClient Initialized");
	}
	
	public boolean start(String[] loginInfo){
		try{
			InetAddress address = InetAddress.getByName(host);
			Socket connection = new Socket(address, port);
			BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
			osw.write(loginInfo[0] + loginInfo[1] + loginInfo[2]);
			
			return true;
		}catch(Exception e){
			System.out.println(e.getStackTrace());
			return false;
		}
	}
}
