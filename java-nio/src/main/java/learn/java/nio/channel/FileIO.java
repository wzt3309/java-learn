package learn.java.nio.channel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

public class FileIO {
    private static final String exist_file = "exist_file.txt";
    private static final String new_file = "new_file.txt";
    private static final String content = "Hello, world\n" +
                                          "This is a already existed file";

    private static void before() throws IOException {
        Path exist_file_path = Paths.get(exist_file);
        Files.write(exist_file_path, content.getBytes(), WRITE, CREATE, TRUNCATE_EXISTING);
    }

    private static void after() throws IOException {
        Path exist_file_path = Paths.get(exist_file);
        Path new_file_path = Paths.get(new_file);
        Files.deleteIfExists(exist_file_path);
        Files.deleteIfExists(new_file_path);
    }

    public static void main(String[] args) throws IOException {
        before();
        after();
    }
}
