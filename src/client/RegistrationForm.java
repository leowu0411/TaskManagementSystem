package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistrationForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel statusLabel;
    private DataOutputStream serverOut;
    private BufferedReader serverIn;

    public RegistrationForm(DataOutputStream serverOut, BufferedReader serverIn) {
        this.serverOut = serverOut;
        this.serverIn = serverIn;
        setupUI();
    }

    private void setupUI() {
        setTitle("Registration");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 30, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 30, 160, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 70, 160, 25);
        add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(10, 110, 80, 25);
        add(registerButton);

        statusLabel = new JLabel("");
        statusLabel.setBounds(10, 140, 260, 25);
        add(statusLabel);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            serverOut.writeBytes("REGISTRATION " + username + " " + password + "\n");

            String response = serverIn.readLine();
            statusLabel.setText("Server response: " + response);

            if (response.startsWith("Registeration Sucessed")) {
                statusLabel.setText("Registration successful");
              
                dispose();
            } else {
                statusLabel.setText(response);
                usernameField.setText("");
                passwordField.setText("");
                usernameField.requestFocus();
            }
        } catch (IOException e) {
            statusLabel.setText("Error: " + e.getMessage());
            // Clear the text fields and focus on username field
            usernameField.setText("");
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }
}
