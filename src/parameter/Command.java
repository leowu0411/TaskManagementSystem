package parameter;

// define command definition, used in ServiceThread class to declare hwo to action
public enum Command {
	LOGIN,
	REGISTRATION,
	EXIT;
	
	
	// handle enum, in case invalid command lead IllegalArgumentException
	public static Command getEnum(String value) {
		if(value == null || value.length() < 1) {
			return null;
		}
		
		for(Command cmd : values()) {
			if(cmd.name().equalsIgnoreCase(value)) {
				return cmd;
			}
		}
		
		return null;
		
	}
	
}
