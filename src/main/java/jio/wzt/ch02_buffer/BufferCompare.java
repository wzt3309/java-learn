package jio.wzt.ch02_buffer;

import java.nio.CharBuffer;

/**
 * Created by ztwang on 2017/8/24 0024.
 */
public class BufferCompare {
    public static void main(String[] args) {
        CharBuffer buffer1 = CharBuffer.allocate(100);
        CharBuffer buffer2 = CharBuffer.allocate(20);
        String s1 = "hello";
        String s2 = "mellow";
        fillBuffer(buffer1, s1);
        fillBuffer(buffer2, s2);
        //参数小于，等于，或者大于引用 compareTo( )的对象实例时，分别返回一个负整数，0 和正整数
        if (buffer1.compareTo(buffer2) > 0) {
            System.out.println("buffer1 < buffer2");
        }
    }

    private static void fillBuffer(CharBuffer buffer, String s) {
        assert buffer != null && s != null;
        for (int i = 0;i < s.length();i++) {
            buffer.put(s.charAt(i));
        }
    }
}
