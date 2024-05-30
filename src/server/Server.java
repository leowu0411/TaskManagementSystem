package server;
import user.*;
import java.io.*;
import java.net.*;


public class Server {
	public static final int PORT = 8000;
	public static UserService userservice = new UserService();
	
	public static void main(String[] args) {
		// open server socket
		try {
			ServerSocket serversock = new ServerSocket(PORT);
			System.out.println("Server end start...");
			
			while(true) {
				Socket connectionSock = serversock.accept();
				ServiceThread service = new ServiceThread(connectionSock);
				Thread serviceThread = new Thread(service);
				serviceThread.run();
			}
			
		}catch(IOException e ) {
			System.out.println(e.getMessage());
		}

	}

}
