package learn.java.nio.buffer;

import java.nio.CharBuffer;

public class BufferDuplicate {

    public static void main(String[] args) {
        CharBuffer buffer1 = CharBuffer.allocate(8), buffer2;
        buffer1.put("hello").flip();
        //buffer2初始position limit mark继承了buffer1
        buffer2 = buffer1.duplicate();
        System.out.println(buffer2.remaining());

        char a = (char) -1;
        System.out.println(a & 0xffff);
    }
}
