package learn.java.nio.buffer;

import java.nio.CharBuffer;

public class BufferArray {

    public static void main(String[] args) {
        char[] array = new char[10];
        CharBuffer buffer = CharBuffer.wrap(array, 3, 6);
        System.out.println(buffer.arrayOffset());
    }
}
