package shell;

import java.util.Scanner;

public class Shell {
    Scanner scanner = new Scanner(System.in);
    String command = "";
    String[] directories;

    public void invoke() {
        getDirectoriesFromEnv();

        while(true) {
            System.out.print("$ ");
            command = scanner.nextLine();
            if(processCommands(command)) return;
        }
    }

    private void getDirectoriesFromEnv() {
        directories = System.getenv("PATH").trim().split(":");
    }

    private boolean processCommands(String commandInput) {
        if(commandInput.isBlank()) return false;
        String[] parts = extractCommandParts(commandInput);
        Command command = Command.fromString(parts[0]);
        String args = parts[1];

        // Exits from the terminal
        switch (command) {
            case EXIT -> {return true;}
            case ECHO -> {
                System.out.println(args);
            }
            case TYPE -> handleTypeCommand(args);
            default -> System.out.println(parts[0] + ": command not found");
        }

        return false;
    }

    private void handleTypeCommand(String args) {
        if(Command.fromString(args) != Command.UNKNOWN) {
            System.out.println(args + " is a shell builtin");
            return;
        }

        String filePath = FileHandler.searchExecutableFile(directories, args);
        if(!filePath.isBlank()){
            System.out.println(args + " is " + filePath);
        }
        else{
            System.out.println(args + ": not found");
        }
    }

    private String[] extractCommandParts(String commandInput) {
        // \\s+ prevent creation of empty items from multiple spaces
        String[] parts = commandInput.trim().split("\\s+", 2);
        if(parts.length == 1) return new String[]{parts[0], ""};
        return parts;
    }
}
