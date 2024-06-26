package client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Notification {

    public static void notification() {
        JFrame frame = new JFrame("Notifications");
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());


        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Name", "Status", "Task Date", "Content", "Notification Date"}, 0);
        table.setModel(model);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        List<Task> notifications = new ArrayList<>();

        for (Task task : MainFrame.tasks) {
            if (task.getNotificationYear() != 0 && task.getNotificationMonth() != 0 && task.getNotificationDay() != 0) {
                Calendar notificationDate = Calendar.getInstance();
                notificationDate.set(task.getNotificationYear(), task.getNotificationMonth() - 1, task.getNotificationDay());
                notificationDate.set(Calendar.HOUR_OF_DAY, 0);
                notificationDate.set(Calendar.MINUTE, 0);
                notificationDate.set(Calendar.SECOND, 0);
                notificationDate.set(Calendar.MILLISECOND, 0);

                if (!notificationDate.after(today)) {
                    notifications.add(task);
                }
            }
        }


        Collections.sort(notifications, (t1, t2) -> {
            Calendar date1 = Calendar.getInstance();
            date1.set(t1.getNotificationYear(), t1.getNotificationMonth() - 1, t1.getNotificationDay());

            Calendar date2 = Calendar.getInstance();
            date2.set(t2.getNotificationYear(), t2.getNotificationMonth() - 1, t2.getNotificationDay());

            return date1.compareTo(date2);
        });

        for (Task task : notifications) {
 
            Calendar taskDate = Calendar.getInstance();
            taskDate.set(task.getYear(), task.getMonth() - 1, task.getDay());
            taskDate.set(Calendar.HOUR_OF_DAY, 0);
            taskDate.set(Calendar.MINUTE, 0);
            taskDate.set(Calendar.SECOND, 0);
            taskDate.set(Calendar.MILLISECOND, 0);

  
            model.addRow(new Object[]{task.getName(), task.getStatus(), sdf.format(convertToDate(taskDate)), task.getContent(), sdf.format(convertToDate(task.getNotificationYear(), task.getNotificationMonth(), task.getNotificationDay()))});
        }


        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);


        frame.add(panel);
        frame.setVisible(true);
    }


    private static Date convertToDate(Calendar calendar) {
        return calendar.getTime();
    }

 
    private static Date convertToDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
