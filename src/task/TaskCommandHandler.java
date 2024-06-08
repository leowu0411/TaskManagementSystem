package task;
import user.*;
import sessionManagement.SessionManager;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import parameter.TaskCommand;

public class TaskCommandHandler {
	
	public void handle(StringTokenizer tokenizer, TaskCommand command, DataOutputStream clientOut)throws IOException {
		switch(command) {		
			case USER_INIT_DATA:
				if(!handleInit(tokenizer, clientOut)) {
                	System.out.println("User init request data Faild");
                }else {
                	System.out.println("User init request data sucess");
                }
                break;
			case UPDATE_TASK:
				if(!handleUpdate(tokenizer, clientOut)) {
                	System.out.println("Database update Faild");
                }else {
                	System.out.println("Database update sucess");
                }
				break;				
			case ASSIGN_TASK:
				if(!handleAssign(tokenizer, clientOut)) {
                	System.out.println("Assign process Faild");
                }else {
                	System.out.println("Assign process sucess");
                }
				
				break;
			default:
				 clientOut.writeBytes("Invalid command\n");	
		}
	}
	
	private boolean handleInit(StringTokenizer tokenizer, DataOutputStream clientOut) throws IOException {
		
		 if (tokenizer.countTokens() < 1) {
             clientOut.writeBytes("Invalid Command\n");
             return false;
         }
		 
		 String sessionId = tokenizer.nextToken();
		 String username = SessionManager.getUsername(sessionId);
		 if(username == null) {
			 clientOut.writeBytes("INIT_TASK" + " Error" + " session is invalid, please relogin...." + "\n");
			 return false;
		 }
		 
		 // use user name map to database API to find list and send back to client
		 // test code -- wait to implement send list in database to client
		 clientOut.writeBytes("INIT_TASK" + " SUCCESS" + " file" + "\n");
		 
		 return true;
	}
	
	private boolean handleUpdate(StringTokenizer tokenizer, DataOutputStream clientOut) throws IOException {
		if (tokenizer.countTokens() < 2) {
            clientOut.writeBytes("Invalid Command\n");
            return false;
        }
		
		String sessionId = tokenizer.nextToken();
		String username = SessionManager.getUsername(sessionId);
		if(username == null) {
			 clientOut.writeBytes("RE_UPDATE_TASK" + " Error\n");
			 return false;
		 }
		
		//use user name map to database to update the client's task list in database :
		// which file type is need to store? which data type should client send
		
		if(true) {
			 clientOut.writeBytes("RE_UPDATE_TASK" + " SUCCESS\n");
			 return true;
		}else {
			clientOut.writeBytes("RE_UPDATE_TASK" + " FAIL\n");
			 return false;
		}
		
	}
	
	private boolean handleAssign(StringTokenizer tokenizer, DataOutputStream clientOut) throws IOException {
		if (tokenizer.countTokens() < 2) {
            clientOut.writeBytes("Invalid Command\n");
            return false;
        }
		
		String sessionId = tokenizer.nextToken();
		String username = SessionManager.getUsername(sessionId);
		if(username == null) {
			 clientOut.writeBytes("RE_ASSIGN" + " Error\n");
			 return false;
		 }
		
		//use user name map to database to update the client's task list in database :
		// which file type is need to store? which data type should client send
		
		if(true) {
			 clientOut.writeBytes("RE_ASSIGN" + " SUCCESS\n");
			 return true;
		}else {
			clientOut.writeBytes("RE_ASSIGN" + " FAIL\n");
			 return false;
		}
		
		
	}
	
	
	
	
}
