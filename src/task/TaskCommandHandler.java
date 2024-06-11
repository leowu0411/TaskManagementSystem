package task;
import user.*;
import server.TaskUpdateChecker;
import database.DatabaseUtil;
import client.Task;
import sessionManagement.SessionManager;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import parameter.TaskCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TaskCommandHandler {
	private TaskUpdateChecker taskUpdateChecker;
	
	public TaskCommandHandler(TaskUpdateChecker taskUpdateChecker) {
		this.taskUpdateChecker = taskUpdateChecker;
	}
	
	public void handle(StringTokenizer tokenizer, TaskCommand command, DataOutputStream clientOut)throws IOException {
		switch(command) {		
			case USER_INIT_DATA:
				if(!handleInit(tokenizer, clientOut)) {
                	System.out.println("User init request data Faild");
                }else {
                	System.out.println("User init request data sucess");
                	taskUpdateChecker.start();
                }
                break;
			case UPDATE_TASK:
				if(!handleUpdate(tokenizer, clientOut)) {
                	System.out.println("Database update Faild");
                }else {
                	System.out.println("Database update sucess");
                }
				break;				
			case ASSIGN_TASK:
				if(!handleAssign(tokenizer, clientOut)) {
                	System.out.println("Assign process Faild");
                }else {
                	System.out.println("Assign process sucess");
                }
				
				break;
			default:
				 clientOut.writeBytes("Invalid command\n");	
		}
	}
	
	private boolean handleInit(StringTokenizer tokenizer, DataOutputStream clientOut) throws IOException {
		
		 if (tokenizer.countTokens() < 1) {
             clientOut.writeBytes("Invalid Command\n");
             return false;
         }
		 
		 String sessionId = tokenizer.nextToken();
		 String username = SessionManager.getUsername(sessionId);
		 if(username == null) {
			 clientOut.writeBytes("INIT_TASK" + " Error" + " session is invalid, please relogin...." + "\n");
			 return false;
		 }
		 
		 // Use user name to get the client's task list in the database
	     // This is a placeholder, replace with actual database update logic
		 
		 // how task list be send to server:
		 // each task's attribute is separated by "|" and each task is separate by ";" 
		 // the attribute "user id list" is use "," to separate elements in the list
		 // replace "file" below as real string
		 String initData = DatabaseUtil.initializeUserData(username);
		 //taskUpdateChecker.setUsername(username);
		 clientOut.writeBytes("INIT_TASK SUCCESS " + initData + "\n");
		 return true;
	}
	
	private boolean handleUpdate(StringTokenizer tokenizer, DataOutputStream clientOut) throws IOException {
		if (tokenizer.countTokens() < 2) {
            clientOut.writeBytes("Invalid Command\n");
            return false;
        }
		
		String sessionId = tokenizer.nextToken();
		String username = SessionManager.getUsername(sessionId);
		if(username == null) {
			 clientOut.writeBytes("RE_UPDATE_TASK Error\n");
			 return false;
		 }
		// how task list be send by client:
		// each task's attribute is separated by "|" and each task is separate by ";" 
		// the attribute "user id list" is use "," to separate elements in the list

        // Use user name to update the client's task list in the database
        // This is a placeholder, replace with actual database update logic
		
		List<Task> tasks = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
        	String taskData = tokenizer.nextToken(";");
            String[] fields = taskData.split("\\|");
            Task task = new Task(
                    fields[0],                          // name
                    fields[1],                          // status
                    Integer.parseInt(fields[2]),        // year
                    Integer.parseInt(fields[3]),        // month
                    Integer.parseInt(fields[4]),        // day
                    fields[5],                          // content
                    Integer.parseInt(fields[6]),        // notificationYear
                    Integer.parseInt(fields[7]),        // notificationMonth
                    Integer.parseInt(fields[8]),        // notificationDay
                    new ArrayList<>(Arrays.asList(fields[9].split(","))) // userIDs
                );
            tasks.add(task);
        }
        boolean updateSuccess = DatabaseUtil.updateTasksInDatabase(username, tasks);

        if (updateSuccess) {
            clientOut.writeBytes("RE_UPDATE_TASK SUCCESS\n");
            return true;
        } else {
            clientOut.writeBytes("RE_UPDATE_TASK FAIL\n");
            return false;
        }
    }
	
	private boolean handleAssign(StringTokenizer tokenizer, DataOutputStream clientOut) throws IOException {
		if (tokenizer.countTokens() < 3) {
            clientOut.writeBytes("Invalid Command\n");
            return false;
        }
		
		String sessionId = tokenizer.nextToken();
		String username = SessionManager.getUsername(sessionId);
		if(username == null) {
			 clientOut.writeBytes("RE_ASSIGN" + " Error\n");
			 return false;
		 }
		
		// how task list be send by client:
		// each task's attribute is separated by "|", the attribute "user id list" is use "," to separate elements in the list
		// use user name map to database to update the client's task list in database :
	    String taskData = tokenizer.nextToken("\n");
	    String[] fields = taskData.split("\\|");
	    if (fields.length < 10) {
	        clientOut.writeBytes("RE_ASSIGN Error\n");
	        return false;
	    }
	    // Create the task object
	    TaskAssignment task = new TaskAssignment(
	        fields[0],                          // name
	        fields[1],                          // status
	        Integer.parseInt(fields[2]),        // year
	        Integer.parseInt(fields[3]),        // month
	        Integer.parseInt(fields[4]),        // day
	        fields[5],                          // content
	        Integer.parseInt(fields[6]),        // notificationYear
	        Integer.parseInt(fields[7]),        // notificationMonth
	        Integer.parseInt(fields[8]), 		// notificationDay
	        username							// assigner
	    );

	    List<String> userIds = Arrays.asList(fields[9].split(","));
	    
	    boolean assignSuccess = DatabaseUtil.assignTaskToUsers(task, userIds);

	    if (assignSuccess) {
	        clientOut.writeBytes("RE_ASSIGN SUCCESS\n");
	        return true;
	    } else {
	        clientOut.writeBytes("RE_ASSIGN FAIL\n");
	        return false;
	    }
	}
	
}
