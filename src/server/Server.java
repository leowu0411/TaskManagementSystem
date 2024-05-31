package server;
import java.io.*;
import java.net.*;


public class Server {
	public static final int PORT = 8000;
	
	public static void main(String[] args) {
		// open server socket
		ServerSocket serversock = null;
		try {
			serversock = new ServerSocket(PORT);
			System.out.println("Server start...");
			
			while(true) {
				Socket connectionSock = serversock.accept();
				ServiceThread service = new ServiceThread(connectionSock);
				Thread serviceThread = new Thread(service);
				serviceThread.run();
			}
			
		}catch(IOException e ) {
			System.out.println(e.getMessage());
		}finally {
            // Close server socket in finally block to release resources
            if (serversock != null) {
                try {
                    serversock.close();
                } catch (IOException e) {
                    System.out.println("Error closing server socket: " + e.getMessage());
                }
            }
		}

	}

}
