package learn.java.nio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by ztwang on 2017/8/30 0030.
 * 需要要启动两次，才会有效果，文件锁是进程级的不是线程级的
 */
public class FileLockTest {
    private static final int SIZEOF_INT = 4;
    private static final int INDEX_START = 0;
    private static final int INDEX_COUNT = 10;
    private static final int INDEX_SIZE = INDEX_COUNT * SIZEOF_INT;

    private ByteBuffer buffer = ByteBuffer.allocate(INDEX_SIZE);
    private IntBuffer indexBuffer = buffer.asIntBuffer();   //创建视图
    private int idVal = 0;
    private Random random = new Random();

    public static void main(String[] args) throws Exception{
        String filePath;
        boolean isWritable;
        Path path;
        FileChannel fc;
        FileLockTest fileLockTest = new FileLockTest();

        if (args.length != 2) {
            System.out.println("Usage: [ -r | -w ] filePath");
            return;
        }

        isWritable = args[0].equalsIgnoreCase("-w");
        filePath = args[1];
        path = Paths.get(filePath);
        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                path = Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (isWritable) {
                fc = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
                fileLockTest.doUpdates(fc);
            }else {
                fc = FileChannel.open(path, StandardOpenOption.READ);
                fileLockTest.doQueries(fc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doUpdates(FileChannel fc) throws Exception{
        if (fc == null) {
            throw new IllegalArgumentException("FileChannel can't be null");
        }
        FileLock lock = null;
        while (true) {
            try{
                lock = fc.lock(INDEX_SIZE, INDEX_SIZE, false);
                indexBuffer.clear();
                for (int i = 0;i < INDEX_COUNT;i++) {
                    idVal++;
                    System.out.println("Updating index " + i + "=" + idVal);
                    indexBuffer.put(idVal);
                    TimeUnit.SECONDS.sleep(1);
                }
                buffer.clear();
                fc.write(buffer, INDEX_START);
            } finally {
                if (lock != null) {
                    lock.release();
                }
            }
            TimeUnit.SECONDS.sleep(random.nextInt(5) + 1L);
        }
    }

    private void doQueries(FileChannel fc) throws Exception{
        if (fc == null) {
            throw new IllegalArgumentException("FileChannel can't be null");
        }
        FileLock lock = null;
        while (true) {
            try {
                lock = fc.lock(INDEX_START, INDEX_SIZE, true);
                int reps = random.nextInt(60) + 20;
                for (int i = 0;i < reps;i++) {
                    int n = random.nextInt(INDEX_COUNT);
                    int position = INDEX_START + (n * SIZEOF_INT);
                    buffer.clear();
                    fc.read(buffer, position);
                    int value = indexBuffer.get(0);
                    System.out.println("Index entry " + n + "=" + value);
                    TimeUnit.SECONDS.sleep(1);
                }
            }finally {
                if (lock != null) {
                    lock.release();
                }
            }
            TimeUnit.SECONDS.sleep(random.nextInt(5) + 1L);
        }
    }


}
