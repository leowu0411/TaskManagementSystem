package client;

public enum ServerInMsg {
	UPDATE_CHAT,
	UPDATE_TASK,
	INIT_CHAT,
	INIT_TASK;
	
	
	public static ServerInMsg getEnum(String value) {
		if(value == null || value.length() < 1) {
			return null;
		}
		
		for(ServerInMsg cmd : values()) {
			if(cmd.name().equalsIgnoreCase(value)) {
				return cmd;
			}
		}
		
		return null;
	}
}
