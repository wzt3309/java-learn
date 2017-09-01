package jio.wzt.ch03_channel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by ztwang on 2017/8/28 0028.
 */
public class GatherTest {
    private static final String FILE = "data3";
    private static final String NEW_LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String[] COL1 = {
            "Aggregate", "Enable", "Leverage",
            "Facilitate", "Synergize", "Repurpose",
            "Strategize", "Reinvent", "Harness"
    };
    private static final String[] COL2 = {
            "cross-platform", "best-of-breed", "frictionless",
            "ubiquitous", "extensible", "compelling",
            "mission-critical", "collaborative", "integrated"
    };
    private static final String[] COL3 = {
            "methodologies", "infomediaries", "platforms",
            "schemas", "mindshare", "paradigms",
            "functionalities", "web services", "infrastructures"
    };
    public static void main(String[] args) {
        int lineNum = 10;
        if (args != null && args.length > 0) {
            lineNum = Integer.valueOf(args[0]);
        }
        try {
            File file = new File(FILE);
            FileOutputStream fos = new FileOutputStream(file);
            GatheringByteChannel gatherChannel = fos.getChannel();
            ByteBuffer[] bs = createByteBuffers(lineNum);
            while (true) {
                if (!(gatherChannel.write(bs) > 0)) break;
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ByteBuffer[] createByteBuffers(int lineNum) {
        List<ByteBuffer> list = new LinkedList<>();
        for (int i = 0;i < lineNum;i++) {
            list.add(pickRandom(COL1, " "));
            list.add(pickRandom(COL2, " "));
            list.add(pickRandom(COL3, NEW_LINE_SEPARATOR));
        }
        return list.toArray(new ByteBuffer[0]);
    }

    private static ByteBuffer pickRandom(String[] strings, String suffix) {
        if (strings == null || strings.length == 0) {
            throw new IllegalArgumentException("strings can't be null or empty");
        }
        String string = strings[new Random().nextInt(strings.length)] + suffix;
        byte[] bytes = string.getBytes(StandardCharsets.US_ASCII);
        return ByteBuffer.wrap(bytes);
    }


}
