package learn.java.nio.select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class EchoClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1234;
    private static final Charset DEFULT_CHARSET = StandardCharsets.UTF_8;
    private static final int CLIENT_ID = new Random().nextInt(100);
    private ByteBuffer buf = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        new EchoClient().run(args);
    }

    void run(String[] args) throws IOException {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length == 2) {
            host = args[0];
            port = Integer.valueOf(args[1]);
        }

        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress(host, port));

        /* non-blocking 简单实现 */
        while (true) {
            if (!sc.finishConnect()) {
                continue;
            }
            String resposne = read(sc);
            if (resposne != null && resposne.length() > 0) {
                System.out.print("Server: " + resposne);
                write(sc);
            }
        }

        /* non-blocking 选择器实现 */
        /*Selector sel = Selector.open();
        sc.register(sel, SelectionKey.OP_CONNECT);

        while (true) {
            int count = sel.select();
            if (count == 0) {
                continue;
            }

            Iterator<SelectionKey> it = sel.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isConnectable()) {
                    while (true) {
                        if (sc.finishConnect()) break;
                    }
                    System.out.println("connected to " + host + ":" + port);
                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }

                if (key.isReadable()) {
                    String response = read(sc);
                    if (response != null && response.length() > 0) {
                        System.out.print("Server: " + response);
                    }
                    if (key.isWritable())
                        write(sc);
                }
                it.remove();
            }
        }*/
    }

    void write(SocketChannel sc) throws IOException {
        try {
            sc.write(stdin());
        } catch (ClosedChannelException e) {
            System.err.println("client channel was closed");
        }
    }
    ByteBuffer stdin() {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        buf.clear();
        buf.put(encode(String.format("Client-%s [%s]\n", CLIENT_ID, str)));
        buf.flip();
        return buf;
    }

    String read(SocketChannel sc) throws IOException {
        if (!sc.isConnected()) {
            System.err.println("client was disconnected");
            return "";
        }

        int count;
        StringBuilder sbd = new StringBuilder();
        try {
            while ((count = sc.read((ByteBuffer)buf.clear())) > 0){
                buf.flip();
                while (buf.hasRemaining()) {
                    sbd.append(DEFULT_CHARSET.decode(buf));
                }
                buf.clear();
            }

            if (count == -1) {
                sc.close();
            }
        } catch (ClosedChannelException e) {
            System.err.println("client channel was closed");
        }
        return sbd.toString();
    }

    byte[] encode(String str) {
       return str != null ? str.getBytes(DEFULT_CHARSET) : new byte[]{0};
    }
}
