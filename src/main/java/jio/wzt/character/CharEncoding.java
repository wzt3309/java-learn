package jio.wzt.character;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by ztwang on 2017/8/23 0023.
 */
public class CharEncoding {
    public static void main(String[] args) {
        /*test1("中");
        test1(new String(new byte[]{(byte)0xe5, (byte)0x95, (byte)0x8a}));
        String a = "中";
        System.out.println(Integer.toHexString(a.toCharArray()[0]));*/

        String a = "中";
        System.out.println(a.toCharArray().length); //1
        System.out.println(Integer.toHexString(a.toCharArray()[0])); //4e2d
        char b = '中';
        System.out.println(Integer.toHexString(b)); //4e2d

    }

    public static void test1(String a) {
        System.out.println(a + " byteArr length " + a.getBytes().length);
        printHex(a.getBytes());  //和保存源代码的编码格式有关utf-8的是3字节一个中文
        char[] aCharArr = a.toCharArray();
        System.out.println(a + " charArr length " + aCharArr.length); //中文长度为1 因为3个字节的头一个字节无用自动略去 2字节一个char
        System.out.println(Arrays.toString(aCharArr));
    }

    public static void printHex(byte[] bytes) {
        for (byte b: bytes) {
            System.out.print(Integer.toHexString(b));
        }
    }
}
