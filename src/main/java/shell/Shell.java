package shell;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

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
        String command = parts[0];
        Command commandEnum = Command.fromString(parts[0]);
        String args = parts[1];

        // Exits from the terminal
        switch (commandEnum) {
            case EXIT -> {return true;}
            case ECHO -> System.out.println(args);
            case TYPE -> handleTypeCommand(args);
            default -> {
                if(!findAndExecute(command, args)){
                    System.out.println(parts[0] + ": command not found");
                }
            }
        }
        return false;
    }

    private boolean findAndExecute(String command, String args) {
        String path = FileHandler.searchExecutableFile(directories, command)
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .orElse("");

        if(path.isBlank()) return false;

        String trimmed = args.trim();
        String[] commands = trimmed.isEmpty() ? new String[] {path}
                : Stream.concat(Stream.of(path), Arrays.stream(trimmed.split("\\s+"))
            ).toArray(String[]::new);
        FileHandler.executeFile(commands);
        return true;
    }

    private void handleTypeCommand(String args) {
        if(Command.fromString(args) != Command.UNKNOWN) {
            System.out.println(args + " is a shell builtin");
            return;
        }

        String filePath = FileHandler.searchExecutableFile(directories, args)
                .map(Path::toString)
                .orElse("");
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
