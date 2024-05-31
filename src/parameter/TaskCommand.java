package parameter;

public enum TaskCommand {
	CREATE_TASK,
    EDIT_TASK,
    DELETE_TASK,
    ASSIGN_TASK,
    UPDATE_STATUS;

    // Handle enum, in case invalid command lead to IllegalArgumentException
    public static TaskCommand getEnum(String value) {
        if (value == null || value.length() < 1) {
            return null;
        }

        for (TaskCommand cmd : values()) {
            if (cmd.name().equalsIgnoreCase(value)) {
                return cmd;
            }
        }

        return null;
    }
}
