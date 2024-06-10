package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import task.TaskAssignment;

public class AssignBox extends JFrame {
    private JList<TaskAssignment> taskList;
    private DefaultListModel<TaskAssignment> taskListModel;
    private JTextArea taskDetails;
    private JButton acceptButton;
    private JButton rejectButton;

    public AssignBox() {
        setTitle("Assigned Tasks");
        setSize(400, 300);
        setLayout(new BorderLayout());

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.addListSelectionListener(e -> displayTaskDetails());

        taskDetails = new JTextArea();
        taskDetails.setEditable(false);

        acceptButton = new JButton("Accept");
        rejectButton = new JButton("Reject");

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptTask();
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rejectTask();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(new JScrollPane(taskDetails), BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);
    }

    public void updateTaskList(List<TaskAssignment> tasks) {
        for (TaskAssignment task : tasks) {
            taskListModel.addElement(task);
        }
    }

    private void displayTaskDetails() {
        TaskAssignment selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            taskDetails.setText("Name: " + selectedTask.getName() +
                                "\nStatus: " + selectedTask.getStatus() +
                                "\nDue Date: " + selectedTask.getYear() + "-" + selectedTask.getMonth() + "-" + selectedTask.getDay() +
                                "\nContent: " + selectedTask.getContent() +
                                "\nNotification Date: " + selectedTask.getNotificationYear() + "-" + selectedTask.getNotificationMonth() + "-" + selectedTask.getNotificationDay() +
                                "\nAssigned By: " + selectedTask.getTaskAssigner());
        }
    }

    private void acceptTask() {
        TaskAssignment selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
        	Task task = new Task(selectedTask);
            MainFrame.tasks.add(task);
            MainFrame.tasksNumber++;
            MainFrame.refreshMainFrame();
            taskListModel.removeElement(selectedTask);
            taskDetails.setText("");
        }
    }

    private void rejectTask() {
        TaskAssignment selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            taskListModel.removeElement(selectedTask);
            taskDetails.setText("");
        }
    }
}
