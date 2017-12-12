package learn.java.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class SocketChannelTest {
    private static final int PORT = 1234;

    private static String pThread() {
        return String.format("[%10s]\t", Thread.currentThread().getName());
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.wrap("let's dance\r\n".getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(PORT));
        ssc.configureBlocking(false);

        new Thread(() -> {
            ByteBuffer buf = ByteBuffer.allocate(100);
            WritableByteChannel stdout = Channels.newChannel(System.out);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //ignore
            }
            try (SocketChannel sc = SocketChannel.open()) {
                //非阻塞模式，会并发地建立连接
                sc.configureBlocking(false);
                sc.connect(new InetSocketAddress("localhost", PORT));
//                System.out.println(sc.isConnectionPending());     /* 判断是否正在建立连接的过程中 */
                while (true) {
                    if (sc.finishConnect()) break;
                    //doSomethingUseful();
                    System.out.println("doSomethingUseful");
                }
                buf.put((pThread() + " is connecting to prot " + PORT + "\r\n").getBytes());
                while (sc.isConnected() && sc.read(buf) != -1) {
                    buf.flip();
                    stdout.write(buf);
                    buf.clear();
                }
            } catch (IOException e) {
                System.out.println(pThread() + "I/O exception");
            }
        }).start();

        while (true) {
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                System.out.println(pThread() + "No connection, waiting...");
                Thread.sleep(1000);
            } else {
                System.out.println(pThread() + "Get connection from " + sc.getRemoteAddress());
                sc.write(buffer);
                buffer.rewind();
            }
        }
    }
}
