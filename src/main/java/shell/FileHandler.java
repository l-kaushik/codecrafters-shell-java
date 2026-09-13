package shell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class FileHandler {
    public static String searchExecutableFile(String[] paths, String targetFileName){
        Optional<Path> validFile = Optional.empty();

        for(String eachDir : paths) {
            Path startPath = Paths.get(eachDir);

            if(!Files.exists(startPath)) continue;

            try(Stream<Path> stream = Files.walk(startPath)) {
                validFile = stream
                        .filter(Files::isRegularFile)
                        .filter(path -> {
                            String name = path.getFileName().toString();
                            int lastDot = name.lastIndexOf('.');
                            String baseName = (lastDot > 0) ? name.substring(0, lastDot) : name;
                            return baseName.equalsIgnoreCase(targetFileName);
                        })
                        .filter(Files::isExecutable)
                        .findFirst();
            } catch (IOException e) {
                System.err.println("Could not read directory " + eachDir + ": " + e.getMessage());
            }

            if(validFile.isPresent()) return validFile.get().toAbsolutePath().toString();
        }
        return "";
    }
}
