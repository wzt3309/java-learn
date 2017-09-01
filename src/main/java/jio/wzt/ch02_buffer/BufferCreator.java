package jio.wzt.ch02_buffer;

import java.nio.CharBuffer;

/**
 * Created by ztwang on 2017/8/24 0024.
 */
public class BufferCreator {
    public static void main(String[] args) {
        String str = "hello world!";
        CharBuffer buffer = CharBuffer.wrap(str);
        assert buffer.remaining() == str.length();
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.println();
        buffer = CharBuffer.wrap("char array".toCharArray());
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
    }
}
