import java.util.Scanner;

public class Shell {
    Scanner scanner = new Scanner(System.in);
    String command = "";

    public void invoke() {
        while(true) {
            System.out.print("$ ");
            command = scanner.nextLine();
            if(processCommands(command)) return;
        }
    }

    private boolean processCommands(String commandInput) {
        if(commandInput.isBlank()) return false;
        String command = extractCommandParts(commandInput)[0];
        String args = extractCommandParts(commandInput)[1];

        // Exits from the terminal
        switch (command) {
            case "exit" -> {return true;}
            case "echo" -> {
                System.out.println(args);
            }
            default -> System.out.println(command + ": command not found");
        }

        return false;
    }

    private String[] extractCommandParts(String commandInput) {
        // \\s+ prevent creation of empty items from multiple spaces
        String[] parts = commandInput.trim().split("\\s+", 2);
        if(parts.length == 1) return new String[]{parts[0], ""};
        return parts;
    }
}
