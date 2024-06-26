package client;

public class Client {
	public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 8000;
    private static ClientInfo info = new ClientInfo();

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginForm(info).setVisible(true);
            }
        });
	}
}
