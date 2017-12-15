package learn.java.nio.select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class EchoServer {
    private static final int DEFAULT_PORT = 1234;
    private static final Charset DEFULT_CHARSET = StandardCharsets.UTF_8;
    private ByteBuffer buf = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        new EchoServer().run(args);
    }

    void run(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length == 1) {
            port = Integer.valueOf(args[0]);
        }

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);

        Selector sel = Selector.open();

        ssc.register(sel, SelectionKey.OP_ACCEPT);

        while (true) {
            int n = sel.select();

            if (n == 0) {
                continue;
            }

            Iterator<SelectionKey> it = sel.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                SocketChannel sc = null;
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel)key.channel();
                        sc = server.accept();
                        register(sc, sel, SelectionKey.OP_READ);
                        gretting(sc);
                    }

                    if (key.isReadable()) {
                        sc = (SocketChannel) key.channel();
                        read(sc);
                    }
                } catch (IOException e) {
                    //ignore
                    if (sc != null) {
                        sc.close();
                    }
                } finally {
                    it.remove();
                }
            }
        }
    }

    void register(SocketChannel sc, Selector sel, int skey) throws IOException {
        if (sc == null) {
            return;
        }
        sc.configureBlocking(false);
        sc.register(sel, skey);
    }

    void gretting(SocketChannel sc) throws IOException {
        buf.clear();
        buf.put("Hello, world\n".getBytes(DEFULT_CHARSET));
        buf.flip();
        sc.write(buf);
    }

    void read(SocketChannel sc) throws IOException{
        SocketAddress addr = null;
        int count;
        try {
            addr = sc.getRemoteAddress();
            while ((count = sc.read((ByteBuffer)buf.clear())) > 0) {
                buf.flip();
                while (buf.hasRemaining()) {
                    sc.write(buf);
                }
            }
            // EOF stream, 表示连接已经关闭
            if (count == -1) {
                sc.close();
            }
        } catch (IOException e) {
            System.err.println("client channel "+ addr +" was closed");
            throw e;
        }
    }
}
