package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import parameter.ServerInMsg;
import task.TaskAssignment;

public class MessageHandler implements Runnable {
    private BufferedReader serverIn;
    private ServerResponse serverResponse;
    private JFrame frame;
    private JFrame loginForm;
    private AssignBox assignBox;
    private volatile boolean running = true; // Add a flag to control the loop

    public MessageHandler(BufferedReader serverIn, ServerResponse serverResponse, JFrame frame, JFrame loginForm, AssignBox assignBox) {
        this.serverIn = serverIn;
        this.serverResponse = serverResponse;
        this.frame = frame;
        this.loginForm = loginForm;
        this.assignBox = assignBox;
    }

    @Override
    public void run() {
        try {
            String message;
            while (running && (message = serverIn.readLine()) != null) {
            	System.out.println("received server response :" + message);
                processMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private void processMessage(String message) {
        StringTokenizer tokenizer = new StringTokenizer(message);
        String commandType = tokenizer.nextToken();

        switch (ServerInMsg.getEnum(commandType)) {
            case RE_UPDATE_TASK:
                handleUpdateReply(tokenizer);
                break;
            case RE_ASSIGN:
                handleAssignReply(tokenizer);
                break;
            case RE_LOGOUT:
                handleLogout(tokenizer);
                break;
            case INIT_TASK:
                handleInit(tokenizer);
                break;
            case UPDATE_TASK:
            	handleTaskUpdate(tokenizer);
                // handle task update
                break;
            case INIT_CHAT:
                break;
            case UPDATE_CHAT:
                // handle chat update
                break;
            default:
                break;
        }
    }

    private void handleInit(StringTokenizer tokenizer) {
        if (tokenizer.countTokens() < 1) {
            serverResponse.show("Invalid Command");
            return;
        }

        String status = tokenizer.nextToken();
        if (status.equals("SUCCESS")) {
            serverResponse.show("request initial data:" + status);
        } else {
            serverResponse.show(status + tokenizer.nextToken());
        }

       
        
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
                    Integer.parseInt(fields[8])        // notificationDay        
                );
            if(fields.length > 9 && fields[9] != null) {
            	task.setUserIDs(new ArrayList<>(Arrays.asList(fields[9].split(",")))); 
            }
            MainFrame.tasks.add(task);
        }
        
        MainFrame.tasksNumber = MainFrame.tasks.size();
        MainFrame.refreshMainFrame();
    }

    private void handleUpdateReply(StringTokenizer tokenizer) {
        String status = tokenizer.nextToken();
        if (status.equals("SUCCESS")) {
            serverResponse.show("data are sucessfully update to database !\n");
        } else if (status.equals("FAIL")) {
            serverResponse.show("data are faild to update to database !\n");
        } else {
            serverResponse.show("session is invalid, please relogin....\n");
        }
    }

    private void handleAssignReply(StringTokenizer tokenizer) {
        String status = tokenizer.nextToken();
        if (status.equals("SUCCESS")) {
            serverResponse.show("task assign process sucess\n");
        } else if (status.equals("FAIL")) {
            serverResponse.show("task assign process fail\n");
        } else {
            serverResponse.show("session is invalid, please relogin....\n");
        }
    }

    private void handleLogout(StringTokenizer tokenizer) {
        String status = tokenizer.nextToken();
        System.out.println(status);
        if (status.equals("SUCCESS")) {
            serverResponse.show("Logout sucessfully: go back to login interface...");
            MainFrame.tasks.clear();
            frame.getContentPane().removeAll();
            frame.dispose();
            loginForm.setVisible(true);
            running = false; // Call to stop the thread
        } else {
            serverResponse.show("Invalid Request");
        }
    }
    
    private void handleTaskUpdate(StringTokenizer tokenizer) {
    	List<TaskAssignment> taskAssignments = new ArrayList<>();
    	while(tokenizer.hasMoreTokens()) {
    		String taskInfo = tokenizer.nextToken(";");
    		String[] fields = taskInfo.split("\\|");
    		TaskAssignment task = new TaskAssignment(fields[0],                          // name
								                    fields[1],                          // status
								                    Integer.parseInt(fields[2]),        // year
								                    Integer.parseInt(fields[3]),        // month
								                    Integer.parseInt(fields[4]),        // day
								                    fields[5],                          // content
								                    Integer.parseInt(fields[6]),        // notificationYear
								                    Integer.parseInt(fields[7]),        // notificationMonth
								                    Integer.parseInt(fields[8]),        // notificationDay)
								                    fields[9]);
    		taskAssignments.add(task);
    	}
    	assignBox.updateTaskList(taskAssignments);
    }

}
