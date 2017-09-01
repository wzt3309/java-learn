package jio.wzt.buffer;

import java.nio.CharBuffer;

/**
 * Created by ztwang on 2017/8/24 0024.
 */
public class BufferFillDrain {
    private static final String[] strings = {
            "A random string value",
            "The product of an infinite number of monkeys",
            "Hey hey we're the Monkees",
            "Opening act for the Monkees: Jimi Hendrix",
            "'Scuse me while I kiss this fly", // Sorry Jimi ;-)
            "Help Me! Help Me!",
    };

    private static int index = 0;

    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(100);
        while (fillBuffer(buffer)) {
            buffer.flip();
            drainBuffer(buffer);
            buffer.clear();
        }
    }

    private static void drainBuffer(CharBuffer buffer) {
        char[] chars = new char[buffer.length()];
        for (int i = 0;buffer.hasRemaining();i++) {
            chars[i] = buffer.get();
        }
        System.out.println(new String(chars));  // 有的字符需要2char，因此拆开打印会出现乱码
    }

    private static boolean fillBuffer(CharBuffer buffer) {
        if (index >= strings.length)
            return false;
        String line = strings[index++];
        for (int i = 0;i < line.length();i++) {
            buffer.put(line.charAt(i));
        }
        return true;
    }
}

