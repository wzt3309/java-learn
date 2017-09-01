## 简单介绍编码
Unicode：是容纳世界所有文字符号的国标标准编码，使用四个字节为每个字符编码

UTF：是英文 Unicode Transformation Format 的缩写，意为把 Unicode 字符转换为某种格式。UTF系列编码方
案（UTF-8、UTF-16、UTF-32）均是由 Unicode 编码方案衍变而来，以适应不同的数据存储或传递，它们都可以完全
表示 Unicode 标准中的所有字符。目前，这些衍变方案中 UTF-8 被广泛使用，而 UTF-16 和 UTF-32 则很少被使用。

UTF-8 使用一至四个字节为每个字符编码，其中大部分汉字采用三个字节编码，少量不常用汉字采用四个字节编码。
因为 UTF-8 是可变长度的编码方式，相对于 Unicode 编码可以减少存储占用的空间，所以被广泛使用。

UTF-16 使用二或四个字节为每个字符编码，其中大部分汉字采用两个字节编码，少量不常用汉字采用四个字节编码。
UTF-16 编码有大尾序和小尾序之别，即 UTF-16BE 和 UTF-16LE，在编码前会放置一个 U+FEFF 或 U+FFFE
（UTF-16BE 以 FEFF 代表，UTF-16LE 以 FFFE 代表），其中 U+FEFF 字符在 Unicode 中代表的意义
是 ZERO WIDTH NO-BREAK SPACE，顾名思义，它是个没有宽度也没有断字的空白。

UTF-32 使用四个字节为每个字符编码，使得 UTF-32 占用空间通常会是其它编码的二到四倍。
UTF-32 与 UTF-16 一样有大尾序和小尾序之别，编码前会放置 U+0000FEFF 或 U+0000FFFE 以区分。

## 乱码的原因
文件要保存在硬盘上就必须要以某种编码方式进行保存，一般都是按照操作系统默认的编码格式进行保存，现在我们
假设要创建一个文件a
1. 打开编辑器
2. 输入中文字符+英文字符。这时字符存储在编辑器所在进程的内存之中(以编辑器自身在内存中存储字符的编码格式
存储字符在内存上，假设在内存中字符以utf-8编码方式保存)
3. 保存到硬盘上。选择格式为gbk，编辑器自动且正确(除非编辑器本身有问题)将内存中utf-8编码的字符转换成gbk
编码保存到硬盘上，字符内容保持不变(无乱码)，只是格式换了一下(就像西班牙语的"hello"转换成了希腊语的"hello")
4. 选择文本浏览工具打开文件a。打开文件时选择读取文件的编码格式为utf-8，这时就出现问题了，以gbk编码保存的文件a
实际上就是"你好,hello"这句话的gbk格式的16进制数字，然后用utf-8读取16进制的方式读取它必然出现乱码(等同于一句西班牙语
你用希腊语的语法去读，肯定都不懂)

综上，出现乱码的原因是：文件保存编码格式和读取编码格式的不匹配造成的

## java编码存在两方面内容：JVM之外和JVM之内

JVM之外：java源文件(.java)和编译后的.class文件，源文件可以采用多种编码格式如utf-8(unix linux平台默认)
或者gbk(windows平台默认)，当将源码用javac编译的时候，默认是javac按照系统默认的编码格式读取java源文件，
然后以utf-8的格式输出到.class文件中，换句话说，在默认情况下unix平台，javac用utf-8格式读取java源文件
然后以utf-8格式写.class；在默认情况下windows平台，javac用gbk格式读取java源文件然后以utf-8格式写.class

但是如果说源文件的编码格式不采用操作系统默认格式呢？如在windows平台下用utf-8格式保存java源文件(一般ide中
都有选项选择文件保存编码格式)，如果不采用ide，直接用javac(javac)编译该源文件，则会造成乱码
```java
/**
* windows 平台下用utf-8保存，同时直接用javac编译
* (不要用ide，ide会智能根据文件编码格式告诉javac用正确的方式便宜)
* 发现乱码
*/
public class Main {
    public static void main(String[] args) {
        String a = "中";
        System.out.println(a);
        System.out.println(a.toCharArray().length); //1
    }
}
```
因此这里的编码转换牵扯到3步 以utf-8保存源文件，以gbk读取源文件，再以utf-8保存.class文件 第二步就出错了
等于将utf-8转gbk的乱码以utf-8写，那么必然是乱码

JVM之内：当运行java字节码时，读入到内存里的字符或者字符串都用`char`或`char[]`表示，而`char`是采用utf-16的
但不是真正的utf-16，为什么这样说? `char`在java里一直是16位的也就是说只能表示带utf-16的0x0000-0xFFFF(BMP  
basic multilingual plane)为止，当对于超过16位的utf-16字符来说(比如表情符号)就需要多个`char`了

```java
public class Main {
    public static void main(String[] args) {
        String a = new String(new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x81});
        System.out.println(a);
        System.out.println(a.toCharArray().length); //2
    }
}
```

概括一点说，在JVM中运行时字符串是`char[]`，字符是`char` 永远的16位，以utf-16(2-4字节)编码但只截取了低16位
截取的结果与unicode(永远的4字节)的低16位同
```java
public class Main {
    public static void main(String[] args) {
        String a = "中";
        System.out.println(a.toCharArray().length); //1
        System.out.println(Integer.toHexString(a.toCharArray()[0])); //4e2d
        char b = '中';
        System.out.println(Integer.toHexString(b)); //4e2d
    }
}
```
[此网站查询字符的各种编码值](http://www.qqxiuzi.cn/bianma/Unicode-UTF.php)

stackoverflow中的解释
- A Java char takes always 16 bits.
- A Unicode character, when encoded as UTF-16, takes "almost always" (not always) 16 bits: that's because there are more than 64K unicode characters. Hence, a Java char is NOT a Unicode character (though "almost always" is).
- "Almost always", above, means the 64K first code points of Unicode, range 0x0000 to 0xFFF (BMP), which take 16 bits in the UTF-16 encoding.
- A non-BMP ("rare") Unicode character is represented as two Java chars (surrogate representation). This applies also to the literal representation as a string: For example, the character U+20000 is written as "\uD840\uDC00".
- Corolary: string.length() returns the number of java chars, not of Unicode chars. A string that has just one "rare" unicode character (eg U+20000) would return length() = 2 . Same consideration applies to any method that deals with char-sequences.
- Java has little intelligence for dealing with non-BMP unicode characters as a whole. There are some utility methods that treat characters as code-points, represented as ints eg: Character.isLetter(int ch). Those are the real fully-Unicode methods.


 