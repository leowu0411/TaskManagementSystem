package user;
import parameter.*;
import sessionManagement.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class UserCommandHandler {
	
	private UserService userService;

    public UserCommandHandler() {
        this.userService = UserService.getInstance();
    }

    public void handle(StringTokenizer tokenizer, UserCommand command, DataOutputStream clientOut) throws IOException {
        switch (command) {
            case LOGIN:
                if(!handleLogin(tokenizer, clientOut)) {
                	System.out.println("User Login Faild");
                }
                break;
            case LOGOUT:
            	if(!handleLogout(tokenizer, clientOut)) {
                	System.out.println("User Login Faild");
                }
            	break;
            case REGISTRATION:
                if(!handleRegistration(tokenizer, clientOut)) {
                	System.out.println("Registeration Faild");
                }
                break;
            case SESSION_REFRESH:
            	// this command is automatically send from client end
            	if(!handleSessionRefresh(tokenizer)) {
            		System.out.println("session refresh Faild");
            	}
            	
            	break;
            default:
                clientOut.writeBytes("Invalid command\n");
        }
    }
    
    private boolean handleLogin(StringTokenizer tokenizer,  DataOutputStream clientOut) {
    	String username = tokenizer.nextToken();
    	String password = tokenizer.nextToken();
    	try {
    		if(tokenizer.countTokens() < 2) {
    			clientOut.writeBytes("Invalid Command");
    			return false;
    		}
    		
    		switch (userService.userLogin(username, password)) {
    			case SUCCESS:
    				SessionManager.createSession(username);
            		clientOut.writeBytes("Login Sucessfully \n!");
            		return true;
    			case USER_NOT_EXIST:
    				clientOut.writeBytes("Login faild : user not exist");
            		return false;
    			case PASSWORD_WRONG:
    				clientOut.writeBytes("Login faild : password wrong");
            		return false;
            	default:
            		clientOut.writeBytes("Login faild : unexpected error");
            		return false;
    		}
    		
    	}catch(IOException e) {
    		System.out.println(e.getMessage());
    	}
    	return false;
    	
    }
    
    private boolean handleRegistration(StringTokenizer tokenizer, DataOutputStream clientOut) {
    	String username = tokenizer.nextToken();
    	String password = tokenizer.nextToken();
    	try {
    		if(tokenizer.countTokens() < 2) {
    			clientOut.writeBytes("Invalid Command");
    			return false;
    		}
    		switch(userService.registerUser(username, password)) {
				case SUCCESS:
					clientOut.writeBytes("Registeration Sucessed\n");
	    			return true;
				case USER_NAME_BE_USED:
					clientOut.writeBytes("Registeration Faild : username be used\n");
	    			return false;
				default:
            		clientOut.writeBytes("Registeration Faild : unexpected error");
            		return false;
    		}

    	}catch(IOException e) {
    		System.out.println(e.getMessage());
    	}
    	return false;
    }
    
    private boolean handleLogout(StringTokenizer tokenizer, DataOutputStream clientOut) {
        try {
            if (tokenizer.countTokens() < 1) {
                clientOut.writeBytes("Invalid logout command\n");
                return false;
            }

            String sessionId = tokenizer.nextToken();
            SessionManager.invalidateSession(sessionId);
            clientOut.writeBytes("Logout successful\n");
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
    private boolean handleSessionRefresh(StringTokenizer tokenizer) {
    	if(tokenizer.countTokens() < 1) {
    		return false;
    	}
    	
    	String sessionId = tokenizer.nextToken();
    	if(SessionManager.sessionRefresh(sessionId)) {
    		return true;
    	}
    	
    	return false;
    }
    
    
}
