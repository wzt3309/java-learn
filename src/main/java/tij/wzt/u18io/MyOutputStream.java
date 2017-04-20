package tij.wzt.u18io;

/**
 * OutputStream 有4大子类除了FilterOutputStream外，其他都代表输出位置
 * 
 * -ByteArrayOutputStream(byte) 向内存输出内容
 * -FileOutputStream(String->fileName) 向文件写内容
 * -PipedOutputStream(PipedInputStream) 向管道流中输出 进程间通信
 * FilerOutputStream
 * 	DataOutputStream(OutputStream) 扩展了可以写基本数据类型 
 * 	-PrintStream(OutputStream) 用于格式化输出
 * 	-BufferedOutputStream(OutputStream) 带缓冲区
 * 
 * -OutputStreamWriter -> Writer(国际化，字符，不乱码)
 * 	-FileWriter(String->fileName boolean ture=添加)
 * CharArrayWriter(空或int->buf大小)
 * StringWriter(空或int->buf大小)
 * PipedWriter(PipedReader)
 * -BufferedWriter(Writer)
 * -PrintWriter(Writer)
 * FilterWriter 抽象类 没子类
 * @author wzt
 *
 */
public class MyOutputStream {

}
