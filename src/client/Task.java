package client;

import java.util.ArrayList;
import task.TaskAssignment;

public class Task {
    private String name;
    private String status;
    private int year;
    private int month;
    private int day;
    private String content;
    private ArrayList<String> userIDs;
    private int notificationYear;
    private int notificationMonth;
    private int notificationDay;

    public Task(String name, String status, int year, int month, int day, String content) {
        this.name = name;
        this.status = status;
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
        this.notificationYear = 0;
        this.notificationMonth = 0;
        this.notificationDay = 0;
        this.userIDs = new ArrayList<>();
    }
    
    public Task(String name, String status, int year, int month, int day, String content, int notificationYear, int notificationMonth, int notificationDay) {
        this.name = name;
        this.status = status;
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
        this.notificationYear = notificationYear;
        this.notificationMonth = notificationMonth;
        this.notificationDay = notificationDay;
        this.userIDs = new ArrayList<>();
    }
    
    public Task(String name, String status, int year, int month, int day, String content, int notificationYear, int notificationMonth, int notificationDay, ArrayList<String> userIds) {
        this.name = name;
        this.status = status;
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
        this.notificationYear = notificationYear;
        this.notificationMonth = notificationMonth;
        this.notificationDay = notificationDay;
        this.userIDs = userIds;
    }
    
    public Task(TaskAssignment taskAssignment) {
        this.name = taskAssignment.getName();
        this.status = taskAssignment.getStatus();
        this.year = taskAssignment.getYear();
        this.month = taskAssignment.getMonth();
        this.day = taskAssignment.getDay();
        this.content = taskAssignment.getContent();
        this.notificationYear = taskAssignment.getNotificationYear();
        this.notificationMonth = taskAssignment.getNotificationMonth();
        this.notificationDay = taskAssignment.getNotificationDay();
        this.userIDs = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(ArrayList<String> userIDs) {
        this.userIDs = userIDs;
    }

    public void addUser(String userID) {
        userIDs.add(userID);
    }

    public void removeUser(String userID) {
        userIDs.remove(userID);
    }

    public int getNotificationYear() {
        return notificationYear;
    }

    public void setNotificationYear(int notificationYear) {
        this.notificationYear = notificationYear;
    }

    public int getNotificationMonth() {
        return notificationMonth;
    }

    public void setNotificationMonth(int notificationMonth) {
        this.notificationMonth = notificationMonth;
    }

    public int getNotificationDay() {
        return notificationDay;
    }

    public void setNotificationDay(int notificationDay) {
        this.notificationDay = notificationDay;
    }
    
    public String toString() {
    	String taskData = this.getName() + "|" +
			              this.getStatus() + "|" +
			              this.getYear() + "|" +
			              this.getMonth() + "|" +
			              this.getDay() + "|" +
			              this.getContent() + "|" +
			              this.getNotificationYear() + "|" +
                          this.getNotificationMonth() + "|" +
                          this.getNotificationDay() + "|" +
			              String.join(",", this.getUserIDs());
    	return taskData;
    }
}
