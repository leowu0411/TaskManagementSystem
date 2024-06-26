package parameter;

public enum UserCommand {
	LOGIN,
	LOGOUT,
	REGISTRATION,
	SESSION_REFRESH;
	
	
	// handle enum, in case invalid command lead IllegalArgumentException
	public static UserCommand getEnum(String value) {
		if(value == null || value.length() < 1) {
			return null;
		}
		
		for(UserCommand cmd : values()) {
			if(cmd.name().equalsIgnoreCase(value)) {
				return cmd;
			}
		}
		
		return null;
		
	}
}
