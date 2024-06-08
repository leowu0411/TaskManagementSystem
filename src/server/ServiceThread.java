package server;
import user.*;
import task.*;
import parameter.*;
import sessionManagement.SessionManager;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class ServiceThread implements Runnable {
	private Socket socket;
	private UserCommandHandler ucm;
	private TaskCommandHandler tcm;
	

	
	public ServiceThread(Socket socket) {
        this.socket = socket;
        this.ucm = new UserCommandHandler(); 
        this.tcm = new TaskCommandHandler();
    }

	
	public void run() {
		try {
			BufferedReader clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream clientOut = new DataOutputStream(socket.getOutputStream());
			
			
			while(true) {
				String request = clientIn.readLine();
				System.out.println("received client request :" + request);
				StringTokenizer tokenizer = new StringTokenizer(request);
				String commandType = tokenizer.nextToken();
				UserCommand userCommand = UserCommand.getEnum(commandType);
                TaskCommand taskCommand = TaskCommand.getEnum(commandType);
                
                if(userCommand != null) {
                	ucm.handle(tokenizer, userCommand, clientOut);
                }else if(taskCommand != null) {
                	tcm.handle(tokenizer, taskCommand, clientOut);
                }else if("EXIT".equals(commandType)){
                	clientOut.writeBytes("Service close\n");
                	if(tokenizer.hasMoreTokens()) {
                		SessionManager.invalidateSession(tokenizer.nextToken());
                	}
                	clientIn.close();
                	socket.close();
                	break;
                }else {
                	clientOut.writeBytes("Invalid Command\n");
                }
                   
			} 

		}catch(IOException e) {
			System.out.println(e.getMessage());
		}finally {
	        try {
	            socket.close(); // Ensure socket is closed even if an exception occurs
	        } catch (IOException e) {
	            System.out.println("Error closing socket: " + e.getMessage());
	        }
	    }
	}
	
	
}
