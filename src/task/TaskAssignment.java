package task;
import client.Task;

public class TaskAssignment extends Task {
	private String taskAssigner;

	public TaskAssignment(String name, String status, int year, int month, int day, String content, int notificationYear, int notificationMonth, int notificationDay, String taskAssigner) {
        super(name, status, year, month, day, content, notificationYear, notificationMonth, notificationDay);
        this.taskAssigner = taskAssigner;
    }
	
	public String getTaskAssigner() {
        return taskAssigner;
    }
	
	@Override
    public String toString() {
        return super.toString() + "|" + taskAssigner;
    }
}
