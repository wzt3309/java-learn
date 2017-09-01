package jio.wzt.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by ztwang on 2017/8/31 0031.
 */
public class FileMemoryMap2 {
    public static void main(String[] args) {
        RandomAccessFile file;
        FileChannel fc;
        ByteBuffer temp = ByteBuffer.allocate(100);
        MappedByteBuffer ro, rw, cow;
        try {
            file = new RandomAccessFile("data6", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        fc = file.getChannel();
        try {
            temp.put("This is the file content".getBytes()).flip();
            fc.write(temp);
            temp.clear();
            temp.put("This is more file content".getBytes()).flip();
            //position is 8 KB, almost certainly a different memory/FS page
            fc.write(temp, 1024*8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ro = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            rw = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
            cow = fc.map(FileChannel.MapMode.PRIVATE, 0, fc.size());
            //begin
            showBuffers("begin", ro, rw, cow);

            //modify cow
            cow.position(8);
            cow.put("COW".getBytes());
            showBuffers("change to COW buffer", ro, rw, cow);

            //modify rw
            rw.position(9);
            //cow will not change because copy a memory page for original page
            rw.put(" R/W ".getBytes());
            rw.position(8194);
            //cow will change because share the same original page
            rw.put(" R/W ".getBytes());
            rw.force();
            showBuffers("change to R/W buffer", ro, rw, cow);

            //modify channel
            temp.clear();
            temp.put(" Channel ".getBytes()).flip();
            fc.write(temp, 8194);
            showBuffers("change to Channel", ro, rw, cow);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void showBuffers(String notice, ByteBuffer... buffers) {
        // assume buffers=ro rw cow
        String[] prefixs = {"R/O", "R/W", "COW"};
        int i = 0;
        System.out.println(String.format("=============%s============", notice));
        for (ByteBuffer buffer: buffers) {
            dumpBuffer(prefixs[i++], buffer);
        }
        System.out.println();
    }

    private static void dumpBuffer(String prefix, ByteBuffer buffer) {
        if (prefix == null || prefix.isEmpty()) {
            prefix = "unknow";
        }
        if (buffer == null || !buffer.hasRemaining()) {
            System.out.println(prefix + ": null");
            return;
        }

        StringBuilder sbd = new StringBuilder(prefix + ": ");
        int nulls = 0;
        //limit equals file size, also equals to buffer capacity
        int limit = buffer.limit();
        //avoid changing the position of buffer, is clear to get all byte
        for (int i = 0;i < limit;i++) {
            char c = (char)buffer.get(i);
            if (c == '\u0000') {
                ++nulls;
                continue;
            }
            if (nulls > 0) {
                sbd.append(String.format("[... %d ...]", nulls));
                nulls = 0;
            }
            sbd.append(c);
        }
        System.out.println(sbd);
    }
}
/** output
 =============begin============
 R/O: This is the file content[... 8168 ...]This is more file content
 R/W: This is the file content[... 8168 ...]This is more file content
 COW: This is the file content[... 8168 ...]This is more file content

 =============change to COW buffer============
 R/O: This is the file content[... 8168 ...]This is more file content
 R/W: This is the file content[... 8168 ...]This is more file content
 COW: This is COW file content[... 8168 ...]This is more file content

 =============change to R/W buffer============
 R/O: This is t R/W le content[... 8168 ...]Th R/W  more file content
 R/W: This is t R/W le content[... 8168 ...]Th R/W  more file content
 COW: This is COW file content[... 8168 ...]Th R/W  more file content

 =============change to Channel============
 R/O: This is t R/W le content[... 8168 ...]Th Channel e file content
 R/W: This is t R/W le content[... 8168 ...]Th Channel e file content
 COW: This is COW file content[... 8168 ...]Th Channel e file content
 */
