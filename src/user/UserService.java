package user;
import java.util.ArrayList;
import parameter.*;
import java.util.List;


public class UserService {
	private List<User> userList = new ArrayList<>(); // replace by database implement, CRUD
	private static UserService instance = null;
	
	private UserService() {}
	
	public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

	// this method need to switch search database not in-memory access
	public User findByUsername(String username) {
		for(User user : userList) {
			if(user.getUsername().equalsIgnoreCase(username)) {
				return user;
			}
		}
		return null;
	}
	
	// register info and store back to  database
	public ErrorMsg registerUser(String name, String password) {
		if(findByUsername(name) != null) {
			System.out.println("usernmae have been used");
			return ErrorMsg.USER_NAME_BE_USED;
		}
		
		String hashedPassword = HashUtil.passwordHash(password);

		User user = new User(name, hashedPassword);
		userList.add(user);
		return ErrorMsg.SUCCESS;
		// store back to databases
	}
	
	public ErrorMsg userLogin(String username, String password) {
		User user = findByUsername(username);
		if(user != null && HashUtil.checkPassword(password, user.getPassword())) {
			// enter session part
			return ErrorMsg.SUCCESS;
		}else if(user == null) {
			System.out.println("user not exist");
			return ErrorMsg.USER_NOT_EXIST;
		}else {
			System.out.println("password wrong");
			return ErrorMsg.PASSWORD_WRONG;
		}		
			
	}

}
