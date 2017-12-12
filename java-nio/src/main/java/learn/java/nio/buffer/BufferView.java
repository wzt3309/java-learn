package learn.java.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

public class BufferView {
    public static void main(String[] args) {
        int[] ints = {1, 2, 8, 128, 128*128*2};
        char[] chars = {'a', 'b', 'c'};
        int sizeof_int = 4;
        int sizeof_char = 2;
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(sizeof_int * ints.length);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(sizeof_char * chars.length);
        /* 字符串encode */
        ByteBuffer byteBuffer3 = ByteBuffer.wrap("test".getBytes());
        CharBuffer charBuffer;
        IntBuffer intBuffer;

        /* IntBuffer 视图*/
        for (int a: ints) {
            byteBuffer1.putInt(a);
        }
        byteBuffer1.flip();
        intBuffer = byteBuffer1.asIntBuffer();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

        /* CharBuffer 视图，注意一定要是put char进去 才可直接转换，否则会乱码 */
        for (char c: chars) {
            byteBuffer2.putChar(c);
        }
        byteBuffer2.flip();
        charBuffer = byteBuffer2.asCharBuffer();
        System.out.println(charBuffer);
        while (byteBuffer2.hasRemaining()) {
            System.out.println(byteBuffer2.getChar());
        }

        /* unicode 编码是java内部默认编码，每个字符2字节， 头两个字节代表BOM 就是字节高低位顺序 feff代表big-endian 高位在后 */
        /* @since 1.7 StandardCharsets.UTF_8 代替了Charset.forName("utf-8")*/

        System.out.println(toString("t".getBytes(Charset.forName("unicode"))));         /* [fe, ff, 0, 74] */
        System.out.println(toString("te".getBytes(Charset.forName("unicode"))));        /* [fe, ff, 0, 74, 0, 65] */
        System.out.println(toString("t".getBytes(Charset.forName("utf-8"))));           /* [74] */
        System.out.println(toString("我".getBytes(Charset.forName("unicode"))));        /* [fe, ff, 62, 11] */
        System.out.println(toString("我".getBytes(Charset.forName("utf-8"))));          /* [e6, 88, 91] */
        /* 字符串decode */
        System.out.println(Charset.defaultCharset().decode(byteBuffer3));
    }

    private static String toString(byte[] b) {
        if (b == null)
            return "null";
        int iMax = b.length - 1;
        if (iMax == -1)
            return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; ;i++) {
            sb.append(String.format("%x", b[i]));
            if (i == iMax)
                return sb.append("]").toString();
            sb.append(", ");
        }
    }

}
