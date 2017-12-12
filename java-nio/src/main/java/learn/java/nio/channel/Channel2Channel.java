package learn.java.nio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;

public class Channel2Channel {

    public static void main(String[] args) throws IOException {
        WritableByteChannel stdout = Channels.newChannel(System.out);
        Path project_root = Paths.get("").toAbsolutePath();
        Path[] xmls = Files.walk(project_root)
                .filter(path -> path.endsWith("pom.xml"))
                .toArray(Path[]::new);

        for (Path xml: xmls) {
            FileChannel xmlChannel = FileChannel.open(xml, READ);
            stdout.write(ByteBuffer.wrap(("\n===================" +
                                          xml.getFileName() +
                                          "==================\n").getBytes()
                                        ));
            xmlChannel.transferTo(0, xmlChannel.size(), stdout);
        }

    }
}
