package user;
import java.util.ArrayList;
import java.util.List;


public class UserService {
	private List<User> userList = new ArrayList<>(); // replace by database implement, CRUD

	// this method need to switch search database not in-memory access
	public User findByUsername(String username) {
		for(User user : userList) {
			if(user.getUsername() == username) {
				return user;
			}
		}
		return null;
	}
	
	// register info and store back to  database
	public boolean registerUser(String name, String password) {
		if(findByUsername(name) != null) {
			System.out.println("usernmae have been used");
			return false;
		}
		
		String hashedPassword = HashUtil.passwordHash(password);

		User user = new User(name, hashedPassword);
		userList.add(user);
		return true;
		// store back to databases
	}
	
	// once login complete, create a session to the login user
	public boolean userLogin(String username, String password) {
		User user = findByUsername(username);
		
		if(user != null && HashUtil.checkPassword(password, user.getPassword())) {
			// enter session part
			return true;
		}else if(user == null) {
			System.out.println("user not exist");
		}else {
			System.out.println("password wrong");
		}		
		return false;	
	}

}
