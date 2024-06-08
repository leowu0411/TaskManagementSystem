package user;
import java.util.ArrayList;
import parameter.*;
import java.util.List;
import database.DatabaseUtil;


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

	
	// register info and store back to  database
	public ErrorMsg registerUser(String name, String password) {
		if(DatabaseUtil.findByUsername(name) != null) {
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
		User user = DatabaseUtil.findByUsername(username);
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
	
	public boolean changePassword(String newPassword, String oldPassword, String username) {
		User user = DatabaseUtil.findByUsername(username);
		
		if(user == null) {
			System.out.println("User :" + username + "not exist");;
			return false;
		}
		
		if(user != null && HashUtil.checkPassword(oldPassword, user.getPassword())){
			 user.setPassword(newPassword);
			 return true;
		}
		
		return false;
	}
	
	public boolean changeUsername(String username, String newName) {
		User user = DatabaseUtil.findByUsername(username);
		
		if(user == null) {
			System.out.println("User :" + username + "not exist");;
			return false;
		}
		
		if(DatabaseUtil.findByUsername(newName) != null) {
			System.out.println("Name has been used");
			return false;
		}else {
			user.setUsername(newName);
			return true;
		}
	}


}
