package jio.wzt.ch03_channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Created by ztwang on 2017/8/29 0029.
 */
public class FileHole {
    public static void main(String[] args) {
        try {
            File tempFile = File.createTempFile("temp", null);
            RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
            FileChannel fileChannel = file.getChannel();
            System.out.println("init file size: " + fileChannel.size());    //0
            ByteBuffer bf = ByteBuffer.allocateDirect(1024);
            bf.put(String
                    .join("", Collections.nCopies(10, "a"))
                    .getBytes(StandardCharsets.US_ASCII)
            );
            bf.flip();
            fileChannel.write(bf);
            System.out.println("first file size: " + fileChannel.size());   //10
            fileChannel.position(54);
            bf.flip();
            fileChannel.write(bf);
            System.out.println("second file size: " + fileChannel.size());  //64
            fileChannel.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
