package sessionManagement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final long SESSION_TIMEOUT_MS = 30 * 60 * 1000; // 30 minutes
    private static final Map<String, Session> sessions = new HashMap<>();

    public static String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + SESSION_TIMEOUT_MS;
        Session session = new Session(sessionId, username, expirationTime);
        sessions.put(sessionId, session);
        return sessionId;
    }

    public static boolean isValidSession(String sessionId) {
        Session session = sessions.get(sessionId);
        return session != null && System.currentTimeMillis() <= session.getExpiryTime();
    }

    public static String getUsername(String sessionId) {
        Session session = sessions.get(sessionId);
        return session != null ? session.getUsername() : null;
    }

    public static void invalidateSession(String sessionId) {
    	if(sessionId == null) {
    		return;
    	}
        sessions.remove(sessionId);
    }
    
    public static void checkAndInvalidateExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<String, Session>> iterator = sessions.entrySet().iterator();
        while(iterator.hasNext()) {
        	Map.Entry<String, Session> entry = iterator.next();
        	if(entry.getValue().getExpiryTime() <= currentTime) {
        		iterator.remove();
        	}
        }
    }
    
    public static boolean sessionRefresh(String sessionId) {
    	Session session = sessions.get(sessionId);
    	if(session == null) {
    		System.out.println("session not exist");
    		return false;
    	}
    	
    	session.resetExpiryTime();
    	return true;
    }
    
    private static class Session {
        private final String sessionId;
        private final String username;
        private long expirationTime;

        public Session(String sessionId, String username, long expirationTime) {
            this.sessionId = sessionId;
            this.username = username;
            this.expirationTime = expirationTime;
        }

        public long getExpiryTime() {
        	return this.expirationTime;
        }
        
        public void resetExpiryTime() {
        	this.expirationTime = System.currentTimeMillis() + SESSION_TIMEOUT_MS;
        }

        public String getUsername() {
            return username;
        }
    }
}
