package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import parameter.ServerInMsg;

public class MessageHandler implements Runnable {
    private BufferedReader serverIn;
    private ServerResponse serverResponse;
    private JFrame frame;
    private JFrame loginForm;
    private volatile boolean running = true; // Add a flag to control the loop

    public MessageHandler(BufferedReader serverIn, ServerResponse serverResponse, JFrame frame, JFrame loginForm) {
        this.serverIn = serverIn;
        this.serverResponse = serverResponse;
        this.frame = frame;
        this.loginForm = loginForm;
    }

    @Override
    public void run() {
        try {
            String message;
            while (running && (message = serverIn.readLine()) != null) {
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
        if (tokenizer.countTokens() < 2) {
            serverResponse.show("Invalid Command");
            return;
        }

        String status = tokenizer.nextToken();
        if (status.equals("SUCCESS")) {
            serverResponse.show("request initial data:" + status);
        } else {
            serverResponse.show(status + tokenizer.nextToken());
        }

       
        // once the database complete and have correct format then this can work
        /*
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
        */
        
        //below is place holder,
        MainFrame.tasks.add(new Task("task0", "Not Started", 2021, 9, 10, "Task 0 content"));
        MainFrame.tasks.get(0).addUser("0");

        MainFrame.tasks.add(new Task("task1", "Not Started", 2023, 10, 15, "Task 1 content"));
        MainFrame.tasks.get(1).addUser("1");

        MainFrame.tasks.add(new Task("task2", "Done", 2022, 5, 20, "Task 2 content"));
        MainFrame.tasks.get(2).addUser("2");

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
            frame.dispose();
            loginForm.setVisible(true);
            running = false; // Call to stop the thread
        } else {
            serverResponse.show("Invalid Request");
        }
    }
    
    private void handleTaskUpdate(StringTokenizer tokenizer) {
    	
    }

}
