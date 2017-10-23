package learn.java.nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by ztwang on 2017/8/31 0031.
 */
public class FileMemoryMap {
    private static final String OUTPUT_FILE ="file_memory_map.out";

    private static final String HTTP_RFC_SEP = "\r\n";
    private static final String SERVER_ID = "Server: MY SERVER";
    private static final String HTTP_HDR_200 = "HTTP/1.0 200 OK" + HTTP_RFC_SEP + SERVER_ID + HTTP_RFC_SEP;
    private static final String HTTP_HDR_404 = "HTTP/1.0 404 NOT FOUND" + HTTP_RFC_SEP + SERVER_ID + HTTP_RFC_SEP;
    private static final String HTTP_MSG_404 = "Can't find file: ";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: fileName");
            return;
        }
        FileChannel output;
        ByteBuffer[] gather = new ByteBuffer[3];
        String fileName = args[0];
        RandomAccessFile file;

        try {
            Path path = Paths.get(OUTPUT_FILE);
            if (Files.exists(path)) {
                Files.delete(path); //clean the file
            }
            path = Files.createFile(path);
            output = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try{
            file = new RandomAccessFile(fileName, "r");
        }catch (FileNotFoundException e) {
            gather = not_found(e);
            output(output, gather);
            return;
        }

        FileChannel fc = null;
        try{
            fc = file.getChannel();
            gather[0] = ByteBuffer.wrap(HTTP_HDR_200.getBytes(DEFAULT_CHARSET));

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Length", String.valueOf(fc.size()));
            headers.put("Content-Type", URLConnection.guessContentTypeFromName(fileName));
            gather[1] = dynhdrs(headers);

            MappedByteBuffer mbf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            gather[2] = mbf;
            output(output, gather);
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
                file.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ByteBuffer[] not_found(Throwable throwable) {
        ByteBuffer header, dynhdrs, body;
        header =  ByteBuffer.wrap(HTTP_HDR_404.getBytes(DEFAULT_CHARSET));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Length", String.valueOf(HTTP_MSG_404.length()));
        headers.put("Content-Type", "text/plain");
        dynhdrs = dynhdrs(headers);

        String msg = HTTP_MSG_404 + throwable + HTTP_RFC_SEP;
        body = body(msg);

        return new ByteBuffer[]{header, dynhdrs, body};
    }

    private static ByteBuffer dynhdrs(Map<String, String> headers) {
        ByteBuffer dynhdrs = ByteBuffer.allocate(128);
        StringJoiner attrs = new StringJoiner(HTTP_RFC_SEP);
        if (headers == null || headers.isEmpty()) return null;
        for (Map.Entry<String, String> entry: headers.entrySet()) {
            StringJoiner attr = new StringJoiner(": ");
            attr.add(entry.getKey());
            attr.add(entry.getValue());
            attrs.add(attr.toString());
        }
        attrs.add(HTTP_RFC_SEP);
        dynhdrs.put(attrs.toString().getBytes(DEFAULT_CHARSET));
        dynhdrs.flip(); //ready to use
        return dynhdrs;
    }

    private static ByteBuffer body(String msg) {
        byte[] body = (msg == null || msg.isEmpty()) ? null : msg.getBytes(DEFAULT_CHARSET);
        return body == null ? null : ByteBuffer.wrap(body);
    }

    private static void output(FileChannel out, ByteBuffer[] gather) {
        if (out == null || gather == null) return;
        try {
            while (true) {
                if (!(out.write(gather) > 0)) break;
            }
            System.out.println("finish output");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
