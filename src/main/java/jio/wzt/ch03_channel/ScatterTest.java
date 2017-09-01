package jio.wzt.ch03_channel;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by ztwang on 2017/8/28 0028.
 */
public class ScatterTest {
    public static void main(String[] args) {
        try {
            FileInputStream fio = new FileInputStream("data3");
            ScatteringByteChannel scatterChannel = fio.getChannel();
            ByteBuffer[] bs = Stream
                    .generate(() -> ByteBuffer.allocate(10))
                    .limit(3)
                    .toArray(ByteBuffer[]::new);
            while (scatterChannel.read(bs) > 0) {
                Arrays.stream(bs)
                        .map(buffer -> (ByteBuffer)buffer.flip())
                        .forEach(buffer -> {
                            System.out.print(StandardCharsets.US_ASCII.decode(buffer));
                            buffer.clear();
                        });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
