package client;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoginForm extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;
    private JLabel statusLabel;
    private ClientInfo info;
    private Socket socket;
    private BufferedReader serverIn;
    private DataOutputStream serverOut;
    private ConfirmWindow confirmWindow;
    

    public LoginForm(ClientInfo info) {
        this.info = info;
        setupUI();

        try {
            this.socket = new Socket(Client.SERVER_ADDRESS, Client.SERVER_PORT);
            this.serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.serverOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        confirmWindow = new ConfirmWindow(serverOut, serverIn, info, socket);
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    private void setupUI() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new CheckOnExit());
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

        loginButton = new JButton("Login");
        loginButton.setBounds(9, 110, 80, 25);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(103, 110, 80, 25);
        add(registerButton);
        
        exitButton = new JButton("Exit");
        exitButton.setBounds(197, 110, 80, 25);
        add(exitButton);


        statusLabel = new JLabel("");
        statusLabel.setBounds(10, 140, 260, 25);
        add(statusLabel);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == loginButton) {
    		handleLogin();
    	}else if(e.getSource() == exitButton) {
    		this.confirmWindow.setVisible(true);
    	}else if(e.getSource() == registerButton) {
    		new RegistrationForm(serverOut, serverIn).setVisible(true);
    	}  	
    }
    
    
    private synchronized void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            serverOut.writeBytes("LOGIN " + username + " " + password + "\n");

            String response = serverIn.readLine();
            statusLabel.setText("Server response: " + response);

            if (response.startsWith("Login Successfully!")) {
            	// Schedule session refresh task
            	String sessionId = response.split(" ")[2];
                info.setSessionId(sessionId);
                
                if(info.getSessionId() != null) {
                	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            sendSessionRefresh(serverOut);
                        }
                    }, 0, 28, TimeUnit.MINUTES);
                }
                
                
                statusLabel.setText("Login successful");
                // Hide the login form and proceed to task management
                usernameField.setText("");
                passwordField.setText("");
                usernameField.requestFocus();
                setVisible(false);
         
                new MainFrame(serverOut, serverIn, info, confirmWindow, this);
            } else {
                statusLabel.setText("Login failed: " + response.split(": ")[1]);
                // Clear the text fields and focus on username field
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
    

    private class CheckOnExit implements WindowListener{	
    	public void windowClosing(WindowEvent e) {
    		confirmWindow.setVisible(true);
    	}
    	
    	public void windowOpened(WindowEvent e) {}
    	public void windowClosed(WindowEvent e) {}
    	public void windowIconified(WindowEvent e) {}
    	public void windowDeiconified(WindowEvent e) {}
    	public void windowActivated(WindowEvent e) {}
    	public void windowDeactivated(WindowEvent e) {}
    	
    }
    
    
    private synchronized void sendSessionRefresh(DataOutputStream serverOut) {
        try {
            serverOut.writeBytes("SESSION_REFRESH " + info.getSessionId() + "\n");
            System.out.println("Session refreshed.");
        } catch (IOException e) {
            System.out.println("Error refreshing session: " + e.getMessage());
        }
	}
}
