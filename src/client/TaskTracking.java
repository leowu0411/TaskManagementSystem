package client;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskTracking {
    private static boolean ascendingOrder = true; // 用來確認當下的排序方法

    public static void sortByStatus() {
        List<TaskWithUsers> taskWithUsersList = new ArrayList<>();
        for (Task task : MainFrame.tasks) {
            taskWithUsersList.add(new TaskWithUsers(task, task.getUserIDs()));
        }

        Collections.sort(taskWithUsersList, new Comparator<TaskWithUsers>() {

            public int compare(TaskWithUsers twu1, TaskWithUsers twu2) {
                String status1 = twu1.task.getStatus();
                String status2 = twu2.task.getStatus();
                if (status1 == null && status2 == null) {
                    return 0;
                } else if (status1 == null) {
                    return 1; // 將null放在最後
                } else if (status2 == null) {
                    return -1; // 將null放在最後
                } else {
                    int result = status1.compareTo(status2);
                    return ascendingOrder ? result : -result; 
                }
            }
        });

        for (int i = 0; i < MainFrame.tasks.size(); i++) {
            MainFrame.tasks.set(i, taskWithUsersList.get(i).task);
            MainFrame.tasks.get(i).setUserIDs(taskWithUsersList.get(i).userIDs);
        }

        MainFrame.refreshMainFrame();
        ascendingOrder = !ascendingOrder; 
    }

    public static void sortByDate() {
        List<TaskWithUsers> taskWithUsersList = new ArrayList<>();
        for (Task task : MainFrame.tasks) {
            taskWithUsersList.add(new TaskWithUsers(task, task.getUserIDs()));
        }

        Collections.sort(taskWithUsersList, new Comparator<TaskWithUsers>() {

            public int compare(TaskWithUsers twu1, TaskWithUsers twu2) {
                try {
                    Task task1 = twu1.task;
                    Task task2 = twu2.task;
                    if (task1.getYear() == 0 || task1.getMonth() == 0 || task1.getDay() == 0) {
                        return 1; // 將null放在最後
                    } else if (task2.getYear() == 0 || task2.getMonth() == 0 || task2.getDay() == 0) {
                        return -1; // 將null放在最後
                    } else {
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(task1.getYear(), task1.getMonth() - 1, task1.getDay());
                        Calendar cal2 = Calendar.getInstance();
                        cal2.set(task2.getYear(), task2.getMonth() - 1, task2.getDay());
                        int result = cal1.compareTo(cal2);
                        return ascendingOrder ? result : -result; 
                    }
                } catch (Exception e) {
                    return 0;
                }
            }
        });

        for (int i = 0; i < MainFrame.tasks.size(); i++) {
            MainFrame.tasks.set(i, taskWithUsersList.get(i).task);
            MainFrame.tasks.get(i).setUserIDs(taskWithUsersList.get(i).userIDs);
        }

        MainFrame.refreshMainFrame();
        ascendingOrder = !ascendingOrder; 
    }



    static class TaskWithUsers {
        Task task;
        ArrayList<String> userIDs;

        TaskWithUsers(Task task, ArrayList<String> userIDs) {
            this.task = task;
            this.userIDs = userIDs;
        }
    }
}