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
			case CREATE_TASK:
				break;
			case EDIT_TASK:
				break;
			case DELETE_TASK:
				break;				
			case ASSIGN_TASK:
				break;
			case UPDATE_STATUS:
				break;
			default:
				 clientOut.writeBytes("Invalid command\n");	
		}
	}
	
	private boolean handleInit(StringTokenizer tokenizer, DataOutputStream clientOut) throws IOException {
		
		return false;
	}
	
	
}
