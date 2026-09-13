package shell;

public enum Command {
    EXIT("exit"),
    ECHO("echo"),
    TYPE("type"),
    UNKNOWN("");

    private final String text;

    Command(String text) {
        this.text = text;
    }

    // string to enum
    public static Command fromString(String text) {
        for (Command c : Command.values()) {
            if(c.text.equalsIgnoreCase(text)) return c;
        }
        return UNKNOWN;
    }
}
