package client;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.HashMap;

public class ChatForm extends JFrame implements ActionListener {
	private DataOutputStream serverOut;
	private ClientInfo info;
	private HashMap<String, String> textTable = new HashMap<String, String>();
	
	public ChatForm(DataOutputStream serverOut, ClientInfo info) {
		this.info = info;
		this.serverOut = serverOut;
		setupUI();
	}
	
	
	public void actionPerformed(ActionEvent e) {
		this.setVisible(true);
	}
	
	private void setupUI() {
		
	}
	
	public void updateUI() {
		
	}

}
