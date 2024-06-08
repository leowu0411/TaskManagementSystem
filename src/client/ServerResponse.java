package client;

import javax.swing.*;

public class ServerResponse extends JFrame {
	private static JLabel status;
	public static boolean flag;


	public ServerResponse(){
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        status = new JLabel();
        status.setText("wait for server response....");
	}
	
	public static void show(String response) {
		 if (response.startsWith("SUCESS")) {
	        	status.setText("SUCESS" + response.split(" ")[1]);
	        	flag = true;
	        }else {
	        	status.setText("Request fail :" + response.split(" ")[1]);
	        	flag = false;
	        }

	}
	
	
}
