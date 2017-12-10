package learn.java.nio.buffer;

import java.nio.BufferUnderflowException;
import java.nio.CharBuffer;

public class BufferMoves {

    public static void main(String[] args) {
        char[] array = new char[5];
        CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put("Hello！");
        //position = 6 limit = 10
        System.out.println(buffer.remaining());
        try {
            buffer.get(array);
            //after position = 4
            System.out.println(buffer.remaining());
        } catch (BufferUnderflowException e) {
            //position 和 limit 未被get改变
            System.out.println("error: " + buffer.remaining());
        }
    }
}
