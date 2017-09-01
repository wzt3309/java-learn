package jio.wzt.ch03_channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by ztwang on 2017/9/1 0001.
 */
public class PipeTest {
    public static void main(String[] args) throws IOException {
        WritableByteChannel writer = Channels.newChannel(System.out);
        ReadableByteChannel reader = startWork(2);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        //The number of bytes read, possibly zero, or -1 if the channel has reached end-of-stream
        //read will be blocked if nothing comes
        while (reader.read(buffer) != -1) {
            buffer.flip();
            System.out.print(Thread.currentThread().getName() + " get: ");
            writer.write(buffer);
            buffer.clear();
        }
    }

    private static ReadableByteChannel startWork(int reps) throws IOException {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel producer = pipe.sink();
        Pipe.SourceChannel consumer = pipe.source();
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(100);
           for (int i = 0;i < reps;i++) {
               String temp = "Worker is doing event-" + i + System.getProperty("line.separator");
               buffer.clear();
               buffer.put(temp.getBytes());
               buffer.flip();
               System.out.print(Thread.currentThread().getName() + " write: " + temp);
               try {
                   while (true) {
                       //The number of bytes written, possibly zero
                       if (!(producer.write(buffer) > 0)) break;
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }).start();
        return consumer;
    }
}
