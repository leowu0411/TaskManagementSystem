package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ConfirmWindow extends JFrame implements ActionListener{
	 private BufferedReader serverIn;
	 private DataOutputStream serverOut;
	 private ClientInfo info;
	 private Socket socket;
	
	public ConfirmWindow(DataOutputStream serverOut, BufferedReader serverIn, ClientInfo info, Socket socket) {
		this.serverIn = serverIn;
		this.serverOut = serverOut;
		this.info = info;
		this.socket = socket;
		
		setSize(200, 100);
		setLayout(new BorderLayout());
		JLabel confirmLabel = new JLabel("Are you sure you want to exit?");
		add(confirmLabel, BorderLayout.CENTER);
		JButton exitButton = new JButton("Yes");
		exitButton.addActionListener(this);
		add(exitButton, BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent e) {
		handleExit();
		System.exit(0);
	}
	
	private synchronized void handleExit() {
        try {
            serverOut.writeBytes("EXIT " + info.getSessionId() + "\n");
            System.out.println("Closing connection...");
            socket.close();
            
         // Dispose of the frame and exit the application
            dispose();
            System.exit(0);
        } catch (IOException e) {
            
        }
    }
}