package learn.java.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.ClosedByInterruptException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChannelInterupted {
    private final static String TEST = "test";
    private final static int BUFSIZ = 6;
    private static ChannelInterupted test = new ChannelInterupted();
    private Thread readThread = new Thread(new Read());
    class Read implements Runnable {

        @Override
        public void run() {
            System.out.println("Read thread start");
            RandomAccessFile file;
            ByteChannel channel = null;
            ByteBuffer buffer = ByteBuffer.allocate(BUFSIZ);
            try {
                int i = 0;
                file = new RandomAccessFile(TEST, "rw");
                channel = file.getChannel();
                while (channel.read(buffer) != -1) {
                    buffer.flip();
                    //没有改变buffer的position = 0 limit
                    System.out.print(++i + ": " + buffer.asCharBuffer());
                    //没有数据被读取，buffer不变position=limit=capacity
                    buffer.compact();
                }
            } catch (ClosedByInterruptException e) {
                System.out.println("Channel is closed: " + channel.isOpen());
            } catch (IOException e) {
                System.out.println("I/O Exception");
            }
        }
    }

    private static void test() throws IOException, InterruptedException {
        Path path = Paths.get(TEST);
        byte[] bytes = new byte[BUFSIZ * 100];
        for (int i = 0;i < 100;i++) {
            int j = 0;
            for (;j < BUFSIZ - 2;) {
                bytes[i * BUFSIZ + j++] = (byte) 0;
                bytes[i * BUFSIZ + j++] = (byte) 'A';
            }
            bytes[i * BUFSIZ + j++] = 0;
            bytes[i * BUFSIZ + j] = (byte) '\n';
        }

        Files.write(path, bytes);
        test.readThread.start();
        Thread.sleep(1000);
        test.readThread.interrupt();
        test.readThread.join();
        Files.deleteIfExists(path);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        test();
    }

}
