package user;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
	
	public static String passwordHash(String Plainpassword) {
		String salt = BCrypt.gensalt();
        return BCrypt.hashpw(Plainpassword, salt); 
	}
	
	public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
