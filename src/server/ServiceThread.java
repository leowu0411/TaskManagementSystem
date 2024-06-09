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
	private TaskUpdateChecker taskUpdateChecker;
	
	public ServiceThread(Socket socket) {
        this.socket = socket;
    }

	
	public void run() {
		try {
			BufferedReader clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream clientOut = new DataOutputStream(socket.getOutputStream());
			
			taskUpdateChecker = new TaskUpdateChecker(clientOut);
			this.tcm = new TaskCommandHandler(taskUpdateChecker);
			this.ucm = new UserCommandHandler(taskUpdateChecker); 
			
			while(true) {
				String request = clientIn.readLine();
				if (request == null) {
                    break; // Client disconnected
                }
				
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
                if (taskUpdateChecker != null) {
                    taskUpdateChecker.stop();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
	    }
	}
	
	
}
