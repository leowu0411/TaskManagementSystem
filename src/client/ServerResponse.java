package client;

import java.awt.BorderLayout;

import javax.swing.*;

public class ServerResponse extends JFrame {
	private JLabel status;

	public ServerResponse(){
		setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        status = new JLabel("wait for server response....", SwingConstants.CENTER);
        add(status, BorderLayout.CENTER);
	}
	
	public void init() {
		status.setText("wait for server response....");
	}
	
	public void show(String response) {
		status.setText(response);
		
	}
	
}
