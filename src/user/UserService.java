package user;

import parameter.ErrorMsg;
import database.DatabaseUtil;

public class UserService {
    private static UserService instance = null;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    // Register info and store back to database
    public ErrorMsg registerUser(String name, String password) {
        if (DatabaseUtil.findByUsername(name) != null) {
            System.out.println("Username has been used");
            return ErrorMsg.USER_NAME_BE_USED;
        }

        String hashedPassword = HashUtil.passwordHash(password);
        User user = new User(name, hashedPassword);
        DatabaseUtil.addUser(user);
        return ErrorMsg.SUCCESS;
    }

    public ErrorMsg userLogin(String username, String password) {
        User user = DatabaseUtil.findByUsername(username);
        if (user != null && HashUtil.checkPassword(password, user.getPassword())) {
            // Enter session part
            return ErrorMsg.SUCCESS;
        } else if (user == null) {
            System.out.println("User not exist");
            return ErrorMsg.USER_NOT_EXIST;
        } else {
            System.out.println("Password wrong");
            return ErrorMsg.PASSWORD_WRONG;
        }
    }

    public boolean changePassword(String newPassword, String oldPassword, String username) {
        User user = DatabaseUtil.findByUsername(username);

        if (user == null) {
            System.out.println("User: " + username + " not exist");
            return false;
        }

        if (user != null && HashUtil.checkPassword(oldPassword, user.getPassword())) {
            return DatabaseUtil.updatePassword(username, newPassword);
        }

        return false;
    }

    public boolean changeUsername(String username, String newName) {
        User user = DatabaseUtil.findByUsername(username);

        if (user == null) {
            System.out.println("User: " + username + " not exist");
            return false;
        }

        if (DatabaseUtil.findByUsername(newName) != null) {
            System.out.println("Name has been used");
            return false;
        } else {
            return DatabaseUtil.updateUsername(username, newName);
        }
    }
}
