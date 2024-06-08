package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainFrame {
    public static List<Task> tasks = new ArrayList<>();
    public static int tasksNumber = 0; // 目前的 task 數量
    private static Container cp;
    private static JFrame frame;
    private static JPanel taskPanel;
    private BufferedReader serverIn;
    private DataOutputStream serverOut;
    private ClientInfo info;
    private ChatForm chatForm;
    private JButton chatSystem;
    private ServerResponse serverResponse;

    public MainFrame(DataOutputStream serverOut, BufferedReader serverIn, ClientInfo info) {
        createMainFrame();
        this.info = info;
        this.serverIn = serverIn;
        this.serverOut = serverOut;
        chatForm = new ChatForm(serverOut, info);
        serverResponse = new ServerResponse();
        //send data request to server...
        InitData();
        // register listener
        chatSystem.addActionListener(chatForm);

        new Thread(new MessageHandler()).start();
    }

    private synchronized void InitData() {
        /*先用userId去資料庫抓取該Id底下有哪些task
        再用迴圈去執行:
        1. String[] task = new String[6]、taskList.add(task) 用這兩個去存取每個task的"task 名稱、status、date年、date月、date日、內容" 進到array中
        2. taskUserIDs 用這個list去存取每一圈對應的task其共用的userid有哪些
        */
    	try {
    		serverResponse.repaint();
    		serverOut.writeBytes("USER_INIT_DATA" + info.getSessionId());
    		String response = serverIn.readLine();
    		serverResponse.show(response);
    		serverResponse.repaint();
    		try {
    			Thread.sleep(500);
    			
    		}catch(InterruptedException e) {
    			e.printStackTrace();
    		}	
    		serverResponse.setVisible(false);

    	}catch(IOException e) {
    		System.out.println(e.getMessage());
    	}
  
    	
    }

    private static void createMainFrame() {
        // 介面建立
        frame = new JFrame();
        frame.setSize(1200, 600);
        frame.setLocation(100, 150);
        frame.setTitle("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainFrame.cp = frame.getContentPane();
        MainFrame.cp.setLayout(null);

        // Scrollable task panel
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for dynamic content


        // Create a JScrollPane to contain the task panel
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBounds(20, 60, 930, 480); // Adjust size and position as needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll
        cp.add(scrollPane);
        

        // 下面三個task是測試用，之後刪掉
        tasks.add(new Task("task0", "Not Started", 2021, 9, 10, "Task 0 content"));
        tasks.get(0).addUser("0");

        tasks.add(new Task("task1", "Not Started", 2023, 10, 15, "Task 1 content"));
        tasks.get(1).addUser("1");

        tasks.add(new Task("task2", "Done", 2022, 5, 20, "Task 2 content"));
        tasks.get(2).addUser("2");

        tasksNumber = tasks.size();


        // 照狀態排序
        JButton sortByStatusButton = new JButton("Sort by Status");
        sortByStatusButton.setBounds(970, 20, 150, 25);
        sortByStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskTracking.sortByStatus();
                for (int i = 0; i < 3; i++) {
                    System.out.println(MainFrame.tasks.get(i).getUserIDs());
                }
                refreshMainFrame();
            }
        });
        cp.add(sortByStatusButton);

        // 照日期排序
        JButton sortByDateButton = new JButton("Sort by Date");
        sortByDateButton.setBounds(970, 55, 150, 25);
        sortByDateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskTracking.sortByDate();
                for (int i = 0; i < 3; i++) {
                    System.out.println(MainFrame.tasks.get(i).getUserIDs());
                }
                refreshMainFrame();
            }
        });
        cp.add(sortByDateButton);
        
        JButton createButton = new JButton("Create");
        createButton.setBounds(970, 85, 150, 25);
        createButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TaskManagement.Create(tasksNumber, frame);
                TaskTracking.dataUpdate(tasks);
                refreshMainFrame();
            }
        });
        cp.add(createButton);


        // 建立介面上的 task
        refreshMainFrame();

        frame.setVisible(true);
    }

    public static void refreshMainFrame() {
        taskPanel.removeAll();

       
        // Add labels row
        JPanel labelPanel = new JPanel();
        labelPanel.setPreferredSize(new Dimension(800, 24));
        labelPanel.setMaximumSize(new Dimension(800, 24));
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        
        // Add empty label for "-" column
        labelPanel.add(Box.createRigidArea(new Dimension(50, 24)));

        // Add labels
        JLabel names = new JLabel("Task Names");
        names.setPreferredSize(new Dimension(150, 24));
        names.setMaximumSize(new Dimension(150, 24));
        names.setHorizontalAlignment(SwingConstants.LEADING); // Center align text
        labelPanel.add(names);

        JLabel status = new JLabel("Status");
        status.setPreferredSize(new Dimension(150, 24));
        status.setMaximumSize(new Dimension(150, 24));
        status.setHorizontalAlignment(SwingConstants.LEADING); // Center align text
        labelPanel.add(status);

        JLabel date = new JLabel("Dead Line");
        date.setPreferredSize(new Dimension(200, 24));
        date.setMaximumSize(new Dimension(200, 24));
        date.setHorizontalAlignment(SwingConstants.LEADING); // Center align text
        labelPanel.add(date);
        
        taskPanel.add(labelPanel);

        // Add tasks to panel
        for (int i = 0; i < tasksNumber; i++) {
            final int finalI = i;
            JPanel tp = new JPanel();
            tp.setPreferredSize(new Dimension(800, 35));
            tp.setMaximumSize(new Dimension(800, 35));
            tp.setLayout(new BoxLayout(tp, BoxLayout.X_AXIS));

            // Delete task button
            JButton deleteButton = new JButton("-");
            deleteButton.setPreferredSize(new Dimension(50, 30));
            deleteButton.setMaximumSize(new Dimension(50, 30));
            deleteButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    TaskManagement.Delete(finalI, frame);
                    TaskTracking.dataUpdate(tasks);
                    refreshMainFrame();
                }
            });
            tp.add(deleteButton);

            // Task name button
            JButton button = new JButton(tasks.get(i).getName());
            button.setPreferredSize(new Dimension(150, 30));
            button.setMaximumSize(new Dimension(150, 30));
            button.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    TaskManagement.Edit(finalI);
                    TaskTracking.dataUpdate(tasks);
                    refreshMainFrame();
                }
            });
            tp.add(button);

            // Task status combo box
            String[] choices = {"Done", "In Progress", "Not Started"};
            final JComboBox<String> comboBox = new JComboBox<>(choices);
            comboBox.setPreferredSize(new Dimension(150, 30));
            comboBox.setMaximumSize(new Dimension(150, 30));
            comboBox.setSelectedItem(tasks.get(i).getStatus());
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedOption = (String) comboBox.getSelectedItem();
                        tasks.get(finalI).setStatus(selectedOption);
                        TaskTracking.dataUpdate(tasks);
                    }
                }
            });
            tp.add(comboBox);

            // Task date picker
            UtilDateModel model = new UtilDateModel();
            Calendar defaultDate = Calendar.getInstance();
            defaultDate.set(tasks.get(i).getYear(), tasks.get(i).getMonth() - 1, tasks.get(i).getDay());
            model.setValue(defaultDate.getTime());

            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");
            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
            final JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
            datePicker.setPreferredSize(new Dimension(200, 30));
            datePicker.setMaximumSize(new Dimension(200, 30));
            datePicker.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Date selectedDate = (Date) datePicker.getModel().getValue();
                    if (selectedDate != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(selectedDate);
                        tasks.get(finalI).setYear(cal.get(Calendar.YEAR));
                        tasks.get(finalI).setMonth(cal.get(Calendar.MONTH) + 1);
                        tasks.get(finalI).setDay(cal.get(Calendar.DAY_OF_MONTH));
                        TaskTracking.dataUpdate(tasks);
                    }
                }
            });

            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 30));
            panel.setMaximumSize(new Dimension(200, 30));
            panel.add(datePicker);
            tp.add(panel);

            // Task assign button
            JButton buttonAssign = new JButton("Assign");
            buttonAssign.setPreferredSize(new Dimension(150, 30));
            buttonAssign.setMaximumSize(new Dimension(150, 30));
            buttonAssign.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    TaskManagement.Assigns(tasks.get(finalI), finalI);
                    TaskTracking.dataUpdate(tasks);
                    refreshMainFrame();
                }
            });
            tp.add(buttonAssign);
            
            // Notification 按鈕的驚嘆號(作為通知用)
            JLabel notificationLabel = new JLabel("!");
            notificationLabel.setPreferredSize(new Dimension(50, 30));
            notificationLabel.setMaximumSize(new Dimension(50, 30));
            notificationLabel.setVisible(false); 
            notificationLabel.setForeground(Color.RED); 
            tp.add(notificationLabel);

            // 判定當天是否有通知
            boolean hasNotificationToday = false;
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            for (Task task : tasks) {
                if (task.getNotificationYear() == today.get(Calendar.YEAR) &&
                        task.getNotificationMonth() == today.get(Calendar.MONTH) + 1 &&
                        task.getNotificationDay() == today.get(Calendar.DAY_OF_MONTH)) {
                    hasNotificationToday = true;
                    break;
                }
            }

            // 有通知就顯現驚嘆號
            if (hasNotificationToday) {
                notificationLabel.setVisible(true);
            }


            taskPanel.add(tp);
        }

        taskPanel.revalidate();
        taskPanel.repaint();
    }



    private class MessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = serverIn.readLine()) != null) {
                    processMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String message) {
        StringTokenizer tokenizer = new StringTokenizer(message);
        String commandType = tokenizer.nextToken();

        switch (ServerInMsg.getEnum(commandType)) {
            case UPDATE_TASK:
                // handle task update
                break;
            case UPDATE_CHAT:
                // handle chat update
                break;
        }
    }
}
