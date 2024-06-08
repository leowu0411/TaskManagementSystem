package client;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.awt.Container;
import java.util.Calendar;
import java.util.Date;


public class TaskManagement {

    private static Container cp;

    public static void Create(int index, JFrame frame) { // index 為當下所按的"+"的是第幾個tasks

    	MainFrame.tasksNumber++; // Increase the task count
    	 // 預設日期為創建task的當天日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; 
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        MainFrame.tasks.add(0, new Task("New Task", "Not Started", year, month, day, ""));

        frame.revalidate();
        frame.repaint();
    }

    public static void Delete(int index, JFrame frame) { // 刪掉目前的task
        // 刪除指定的task
        MainFrame.tasks.remove(index);
        MainFrame.tasksNumber--; // 總數量減少
        frame.revalidate();
        frame.repaint();
    }

    public static void Edit(int index) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setLocation(100, 150);
        frame.setTitle("Edit");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cp = frame.getContentPane();
        cp.setLayout(null);

        // task的name標籤
        JLabel nameLabel = new JLabel("Task Name:");
        nameLabel.setBounds(20, 20, 100, 25);
        cp.add(nameLabel);

        // task的name資料(更改地方)
        JTextField nameTextField = new JTextField(MainFrame.tasks.get(index).getName());
        nameTextField.setBounds(130, 20, 200, 25);
        cp.add(nameTextField);

        // task的status標籤
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(20, 60, 100, 25);
        cp.add(statusLabel);

        // task的status資料(更改地方)
        String[] statusOptions = {"Done", "In Progress", "Not Started"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setSelectedItem(MainFrame.tasks.get(index).getStatus());
        statusComboBox.setBounds(130, 60, 200, 25);
        cp.add(statusComboBox);

        // task的date標籤
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(20, 100, 100, 25);
        cp.add(dateLabel);

        // task的date資料(更改地方)
        UtilDateModel model = new UtilDateModel();
        if (MainFrame.tasks.get(index).getYear() != 0 && MainFrame.tasks.get(index).getMonth() != 0 && MainFrame.tasks.get(index).getDay() != 0) {
            Calendar defaultDate = Calendar.getInstance();
            defaultDate.set(MainFrame.tasks.get(index).getYear(), MainFrame.tasks.get(index).getMonth() - 1, MainFrame.tasks.get(index).getDay()); // 設置預設日期
            model.setValue(defaultDate.getTime());
        }
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        JPanel datePanelContainer = new JPanel();
        datePanelContainer.add(datePicker);
        datePanelContainer.setBounds(130, 100, 200, 30);
        cp.add(datePanelContainer);

       // task的notification標籤
       JLabel notificationLabel = new JLabel("Notification:");
       notificationLabel.setBounds(20, 140, 100, 25);
       cp.add(notificationLabel);

       // task的notification資料(更改地方)
       UtilDateModel notificationModel = new UtilDateModel();
       if (MainFrame.tasks.get(index).getNotificationYear() != 0 && MainFrame.tasks.get(index).getNotificationMonth() != 0 && MainFrame.tasks.get(index).getNotificationDay() != 0) {
           Calendar defaultNotification = Calendar.getInstance();
           defaultNotification.set(MainFrame.tasks.get(index).getNotificationYear(), MainFrame.tasks.get(index).getNotificationMonth() - 1, MainFrame.tasks.get(index).getNotificationDay()); // 設置預設notification日期
           notificationModel.setValue(defaultNotification.getTime());
       }
       JDatePanelImpl notificationDatePanel = new JDatePanelImpl(notificationModel, p);
       JDatePickerImpl notificationDatePicker = new JDatePickerImpl(notificationDatePanel, new DateLabelFormatter());
       JPanel notificationPanelContainer = new JPanel();
       notificationPanelContainer.add(notificationDatePicker);
       notificationPanelContainer.setBounds(130, 140, 200, 30);
       cp.add(notificationPanelContainer);

       // task的內容標籤
       JLabel contentLabel = new JLabel("Content:");
       contentLabel.setBounds(20, 180, 100, 25);
       cp.add(contentLabel);

       // task的內容資料(更改地方)
       JTextArea contentTextArea = new JTextArea(MainFrame.tasks.get(index).getContent());
       contentTextArea.setBounds(130, 180, 200, 100);
       cp.add(contentTextArea);

       // 儲存
       JButton saveButton = new JButton("Save");
       saveButton.setBounds(100, 300, 100, 25);
       saveButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               MainFrame.tasks.get(index).setName(nameTextField.getText());
               MainFrame.tasks.get(index).setStatus((String) statusComboBox.getSelectedItem());
               Date selectedDate = (Date) datePicker.getModel().getValue();
               if (selectedDate != null) {
                   Calendar cal = Calendar.getInstance();
                   cal.setTime(selectedDate);
                   MainFrame.tasks.get(index).setYear(cal.get(Calendar.YEAR));
                   MainFrame.tasks.get(index).setMonth(cal.get(Calendar.MONTH) + 1);
                   MainFrame.tasks.get(index).setDay(cal.get(Calendar.DAY_OF_MONTH));
               }
               Date selectedNotification = (Date) notificationDatePicker.getModel().getValue();
               if (selectedNotification != null) {
                   Calendar cal = Calendar.getInstance();
                   cal.setTime(selectedNotification);
                   MainFrame.tasks.get(index).setNotificationYear(cal.get(Calendar.YEAR));
                   MainFrame.tasks.get(index).setNotificationMonth(cal.get(Calendar.MONTH) + 1);
                   MainFrame.tasks.get(index).setNotificationDay(cal.get(Calendar.DAY_OF_MONTH));
               }
               MainFrame.tasks.get(index).setContent(contentTextArea.getText());
               frame.dispose();
               MainFrame.refreshMainFrame();
           }
       });
       cp.add(saveButton);
        // 取消
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(220, 300, 100, 25);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        cp.add(cancelButton);

        frame.setVisible(true);
    }

    public static void Assigns(final Task task, int index) {
        final JFrame assignFrame = new JFrame();
        assignFrame.setSize(400, 400);
        assignFrame.setLocation(200, 200);
        assignFrame.setTitle("Assign Task");
        assignFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cp = assignFrame.getContentPane();
        cp.setLayout(null);

        JLabel userLabel = new JLabel("Assign to User:");
        userLabel.setBounds(50, 20, 120, 25);
        cp.add(userLabel);

        final JTextField userField = new JTextField();
        userField.setBounds(180, 20, 160, 25);
        cp.add(userField);

        JButton assignButton = new JButton("Assign");
        assignButton.setBounds(150, 200, 80, 25);
        assignButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String assignedUser = userField.getText();
                if (!assignedUser.isEmpty()) {
                    JOptionPane.showMessageDialog(assignFrame, "Task assigned to " + assignedUser);
                    task.addUser(assignedUser);
                    assignFrame.dispose();
                    MainFrame.refreshMainFrame();
                } else {
                    JOptionPane.showMessageDialog(assignFrame, "請輸入ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cp.add(assignButton);

        assignFrame.setVisible(true);
    }
}
