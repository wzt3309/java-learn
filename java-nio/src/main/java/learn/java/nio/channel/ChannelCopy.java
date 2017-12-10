package learn.java.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChannelCopy {
    private static final int BUFSIZ = 8;
    private static final String CONTENT = "Hello, world. This is ChannelCopy";
    public static void main(String[] args) throws IOException {
        String srcFileName = "ChannelIn", destFileName = "ChannelOut";
        Files.write(Paths.get(srcFileName), CONTENT.getBytes(StandardCharsets.UTF_8));
        Files.deleteIfExists(Paths.get(destFileName));

        RandomAccessFile srcFile = new RandomAccessFile(srcFileName, "r");
        RandomAccessFile destFile = new RandomAccessFile(destFileName, "rw");
        ByteChannel src = srcFile.getChannel(), dest = destFile.getChannel();

        ChannelCpoy(src, dest, 5);    /* 只拷贝5个字节 */
        destFile.writeChar('\n');
        /* 在通道未关闭前，文件偏移量会一直保持 */
        System.out.println(srcFile.getFilePointer());
        srcFile.seek(0);
        ChannelCpoy(src, dest, 10);   /* 只拷贝10个字节 */
        destFile.writeChar('\n');
        srcFile.seek(0);
        ChannelCpoy(src, dest);
        src.close();
        dest.close();
    }

    private static void ChannelCpoy(ByteChannel src, ByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFSIZ);
        while (src.read(buffer) != -1) {
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }

    private static void ChannelCpoy(ByteChannel src, ByteChannel dest, int length) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFSIZ);
        int remain = length;
        while (remain > 0 && src.read(buffer) != -1) {
            int len = buffer.position();
            if (remain > len) {
                buffer.flip();
                remain -= dest.write(buffer);
                buffer.compact();
            } else {
                buffer.position(0).limit(remain);
                remain -= dest.write(buffer);
                buffer.compact();
            }
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }
}
