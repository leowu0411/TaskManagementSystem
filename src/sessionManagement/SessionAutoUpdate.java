package sessionManagement;

public class SessionAutoUpdate implements Runnable {
	
	public void run() {
		
		SessionManager.checkAndInvalidateExpiredSessions();
		
	}
}
