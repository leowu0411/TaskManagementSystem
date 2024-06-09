package server;

import database.DatabaseUtil;
import task.TaskAssignment;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskUpdateChecker implements Runnable {
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private DataOutputStream clientOut;
	private String username;
	
	public TaskUpdateChecker(DataOutputStream clientOut) {
		this.clientOut = clientOut;
	}

    public void start() {
        scheduler.scheduleAtFixedRate(this, 0, 20, TimeUnit.SECONDS);
    }
    
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
	
	public synchronized void run () {
		
		List<TaskAssignment> newAssignments = DatabaseUtil.getNewTaskAssignments(username);
		try {
			for(TaskAssignment taskAssignment : newAssignments) {
				clientOut.writeBytes("UPDATE_TASK " + taskAssignment.toString() + "\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
