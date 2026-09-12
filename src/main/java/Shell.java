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
        String[] parts = extractCommandParts(commandInput);
        String command = parts[0];
        String args = parts[1];

        // Exits from the terminal
        switch (command) {
            case "exit" -> {return true;}
            case "echo" -> {
                System.out.println(args);
            }
            case "type" -> {
                // TODO: find a way to keep track of builtin commands (maybe use enums)

                if(args.equals("exit") || args.equals("echo") || args.equals("type")) {
                    System.out.println(args + " is a shell builtin");
                }
                else{
                    System.out.println(args + ": not found");
                }
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
