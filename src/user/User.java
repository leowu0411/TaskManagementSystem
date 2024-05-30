package user;

public class User {

	private String username;
	private String password;

	// init info
	public User(String username, String password){
		System.out.println("Enter user name:");
		this.username = username;
		System.out.println("Enter user password:");
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}

