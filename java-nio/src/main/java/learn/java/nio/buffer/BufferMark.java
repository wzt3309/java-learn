package learn.java.nio.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferMark {

    public static void main(String[] args) {
        Buffer buffer = ByteBuffer.allocate(10);
        buffer.position(4).mark().position(2);
        /* 新的position小于当前mark，mark被丢弃，抛出InvalidMarkException */
        buffer.reset();
    }
}
