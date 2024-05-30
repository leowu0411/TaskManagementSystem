package server;
import user.*;
import parameter.Command;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class ServiceThread implements Runnable {
	private Socket socket;
	private UserService userservice;
	private String request;
	
	public ServiceThread(Socket socket) {
		this.socket = socket;
		this.userservice = new UserService();
	}
	
	public void run() {
		try {
			BufferedReader clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream clientOut = new DataOutputStream(socket.getOutputStream());
			
			while(true) {
				System.out.println("received client request :" + request);
				StringTokenizer tokenizer = new StringTokenizer(request);
                Command command = Command.getEnum(tokenizer.nextToken());
                
                switch(command) {
                	case LOGIN:
                		break;
                	case REGISTRATION:
                		break;
                	case EXIT:
                		// send back to client say service close...
                		break;
                	default:
                		break;
                }
                
                
			} 

		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		
	}
}
