package database;
import java.util.ArrayList;
import java.util.List;

import user.User;

public class DatabaseUtil {
	private static List<User> userList = new ArrayList<>(); // replace by database implement, CRUD
	
	// this method need to switch search database not in-memory access
	public static User findByUsername(String username) {
		for(User user : userList) {
			if(user.getUsername().equalsIgnoreCase(username)) {
				return user;
			}
		}
		return null;
	}

}
