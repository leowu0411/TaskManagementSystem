package database;

import user.User;
import task.TaskAssignment;
import client.Task;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    // Placeholder for storing users and tasks in-memory
    public static List<User> userList = new ArrayList<>();
    public static List<TaskAssignment> taskList = new ArrayList<>();

    /**
     * Fetches new task assignments from the database. 
     * @param username The username of the user whose data be requested
     * @return List of new TaskAssignment objects for the user.
     */
    public synchronized static List<TaskAssignment> getNewTaskAssignments(String username) {
        // Placeholder for actual database logic
        return new ArrayList<>();
    }

    /**
     * Assigns a task to a list of users.
     * @param task The task to be assigned.
     * @param userIds List of user IDs to whom the task is assigned.
     * @return true if the assignment was successful, false otherwise.
     */
    public synchronized static boolean assignTaskToUsers(TaskAssignment task, List<String> userIds) {
        // Placeholder for actual database logic
        return false;
    }

    /**
     * Updates the tasks in the database for a specific user.
     * @param username The username of the user whose tasks are being updated.
     * @param tasks List of tasks to be updated.
     * @return true if the update was successful, false otherwise.
     */
    public synchronized static boolean updateTasksInDatabase(String username, List<Task> tasks) {
        // Placeholder for actual database logic
        return false;
    }

    /**
     * Initializes the task data for a user from the database.
     * @param username The username of the user.
     * @return A string representation of the user's tasks.
     */
    public synchronized static String initializeUserData(String username) {
        // Placeholder for actual database logic
        return "";
    }

    /**
     * Finds a user by username in the database.
     * @param username The username of the user to find.
     * @return The User object if found, null otherwise.
     */
    public synchronized static User findByUsername(String username) {
        // Placeholder for actual database logic
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Adds a user to the database.
     * @param user The User object to add.
     */
    public synchronized static void addUser(User user) {
        // Placeholder for actual database logic
        userList.add(user);
    }

    /**
     * Updates a user's password in the database.
     * @param username The username of the user.
     * @param newPassword The new password.
     * @return true if the update was successful, false otherwise.
     */
    public synchronized static boolean updatePassword(String username, String newPassword) {
        // Placeholder for actual database logic
        User user = findByUsername(username);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Updates a user's username in the database.
     * @param oldUsername The current username of the user.
     * @param newUsername The new username.
     * @return true if the update was successful, false otherwise.
     */
    public synchronized static boolean updateUsername(String oldUsername, String newUsername) {
        // Placeholder for actual database logic
        User user = findByUsername(oldUsername);
        if (user != null && findByUsername(newUsername) == null) {
            user.setUsername(newUsername);
            return true;
        }
        return false;
    }
}
