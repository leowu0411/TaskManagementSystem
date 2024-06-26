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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import parameter.*;

public class MainFrame {
    public static List<Task> tasks = new ArrayList<>();
    public static int tasksNumber = 0; // 目前的 task 數量
    private static Container cp;
    private static JFrame frame;
    private static JPanel taskPanel;
    private BufferedReader serverIn;
    private static DataOutputStream serverOut;
    private static ClientInfo info;
    private ChatForm chatForm;
    private JButton chatSystem;
    private static ServerResponse serverResponse;
    private ConfirmWindow confirmWindow;
    private LoginForm loginForm;
    private AssignBox assignBox;

    public MainFrame(DataOutputStream serverOut, BufferedReader serverIn, ClientInfo info, ConfirmWindow confirmWindow, LoginForm loginForm) {
        this.info = info;
        this.serverIn = serverIn;
        this.serverOut = serverOut;
        this.confirmWindow = confirmWindow;
        this.loginForm = loginForm;
        chatForm = new ChatForm(serverOut, info);
        createMainFrame();
        // open server message listener
        serverResponse = new ServerResponse();
        serverResponse.setVisible(true);
        this.assignBox = new AssignBox();
        new Thread(new MessageHandler(serverIn, this.serverResponse, this.frame, this.loginForm, this.assignBox)).start();
        //send data request to server...
        initRequest();
        
        // register listener
        chatSystem.addActionListener(chatForm);
    }
    
    private void createMainFrame() {
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
        createButton.setBounds(970, 90, 150, 25);
        createButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TaskManagement.Create(frame);                
                refreshMainFrame();
            }
        });
        cp.add(createButton);
        
     // Assign Box button
        JButton assignBoxButton = new JButton("Assign Box");
        assignBoxButton.setBounds(970, 125, 150, 25);
        assignBoxButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                assignBox.setVisible(true);
            }
        });
        cp.add(assignBoxButton);

        // Save button
        JButton SaveButton = new JButton("Save");
        SaveButton.setBounds(970, 160, 150, 25);
        SaveButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                updateTask(serverOut, tasks);
            }
        });
        cp.add(SaveButton);

        // Logout button
        JButton LogoutButton = new JButton("Logout");
        LogoutButton.setBounds(970, 195, 150, 25);
        LogoutButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                logout(serverOut, info);
            }
        });
        cp.add(LogoutButton);

        // EXIT button
        JButton ExitButton = new JButton("EXIT");
        ExitButton.setBounds(970, 230, 150, 25);
        ExitButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                confirmWindow.setVisible(true);
            }
        });
        cp.add(ExitButton);


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
                    TaskManagement.Assigns(serverResponse, info, serverOut, tasks.get(finalI), finalI);
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
    
    
    private synchronized void initRequest() {
    	try {
        	serverOut.writeBytes("USER_INIT_DATA" + " " + info.getSessionId() + "\n");
        }catch(IOException e) {
        	System.out.println(e.getMessage());
        }
    }
    
    private static synchronized void logout(DataOutputStream serverOut, ClientInfo info){
    	serverResponse.init();
    	serverResponse.setVisible(true);
    	try {
    		serverOut.writeBytes("LOGOUT" + " " + info.getSessionId() + "\n");
    		info.setSessionId(null);
    	}catch(IOException e) {
    		
    	}
    }
    
    private static synchronized void updateTask(DataOutputStream serverOut, List<Task> tasks){
    	serverResponse.init();
    	serverResponse.setVisible(true);
    	try {
    		StringBuilder taskDataBuilder = new StringBuilder();
    		for(Task task : tasks) {
    			String taskData = task.toString();
    			taskDataBuilder.append(taskData).append(";");
    		}
    		serverOut.writeBytes("UPDATE_TASK" + " " + info.getSessionId() + " " + taskDataBuilder.toString() + "\n");
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    
}
