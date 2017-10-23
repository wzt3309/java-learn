package tij.wzt.ch18_io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * InputStream 有6大子类除了FilterInputStream外，其他都代表一种数据源
 * 
 * -ByteArrayInputStream(byte)	从内存中读取字节数组
 * StringBufferInputStream(String) 从StringBuffer读取 方法已经被取消
 * -FileInputStream(String->fileName) 文件流
 * -PipedInputStream(PipedOutputStream) 管道流，多线程中线程通信
 * SequenceInputStream(连个InputStream 或者容纳了InputStream的容器Enumeration) 多个流进行合并
 * FilterInputStream 装饰器
 * 	-DataInputStream(InputStream) 扩展了可用于读取基本数据类型int char long
 * 	-BufferedInputStream(InputStream) 带缓冲的InputStream 先将文件的内容全部读到缓冲区，再
 *   用byte[]或挨个从内从中读取，而普通的InputStream直接调用系统的（native）read方法byte[]或者
 *   挨个从硬盘上读取数据
 *  LineNumberInputStream(InputStream) 可跟踪流里的行号
 *  PushbackInputStream(InputStream) 可回退的流，java编译器需要，基本用不到
 *  
 *  InputStreamReader -> Reader(国际化，字符，不乱码)
 *  -FileReader(String->fileName boolean ture=添加)
 * CharArrayReader(空或int->buf大小)
 * StringReader(空或int->buf大小)
 * PipedReader(PipedReader)
 * -BufferedReader(Reader)
 * 	 LineNumberReader(Reader)
 * FilterReader 抽象类 
 * 	PushbackReader
 * @author wzt
 *
 */
public class MyInputStream {

	public static void main(String[] args) throws IOException {
//		readByteArr();
//		readFile();
		readstd();
	}

	public static void readByteArr() throws IOException{
		byte[] bytes=new byte[20];
		for(int i=0;i<20;i++)
			bytes[i]=(byte)(Math.random()*256);
		System.out.println(new String(bytes,0,20));
		
		InputStream in=new ByteArrayInputStream(bytes);
		int len=0;
		byte[] gets=new byte[6];
		//gets 每次区stream里读数据时，是不被清空的
		//最后一次只读到了2个数据，但是gets后面4位是满的，是第15 16 17 18个数据
		//new String(gets,0,len) 将gets 从0开始len长度转化成string 刚好
		while((len=in.read(gets, 0, 6))!=-1){
			System.out.println(new String(gets,0,len));
		}
		
		//in.mark(0) 在当前位置施加标记配合in.reset()使用，将读取指针重定向到mark位置
		//in.skip(num) 跳过num个字节
		
	}
	
	@Deprecated
	public static void readString()throws IOException{
//		InputStream in=new StringBufferInputStream("think in java");
	}
	
	public static void readFile()throws IOException{
		InputStream in =new FileInputStream("README.md");
		int len=0;
		
		/*
		 * 必乱码，因为是按照字节的方式取出内容，一个完整字符字节组成会被截断
		 * 需要包装成Reader以字符方式读取才行
		 * */
//		byte[] getb=new byte[10];
//		while((len=in.read(getb, 0, 10))!=-1){
//			System.out.println(new String(getb,0,len));
//		}
		
		BufferedInputStream bin=new BufferedInputStream(in);
		
//		byte[] getb=new byte[10];
//		while((len=bin.read(getb, 0, 10))!=-1){
//			System.out.println(new String(getb,0,len));
//		}
		/**
		 * 不带缓冲区，或者说是自身声明缓冲区gets
		 * Reader类的read会与StreamDecoder关联，用于正确地字节转字符
		 * 按照默认字符集，平台默认，此处为ubuntu 默认utf-8
		 * 与README.md的字符集相同，不会出现乱码
		 * 由于不是按行读取，因此行格式是错位的，此处每读取10个字符就换行
		 * 读取时会读取\n 
		 */
		InputStreamReader inr=new InputStreamReader(in);
//		char[] gets=new char[10];
//		len=0;
//		while((len=inr.read(gets, 0, 10))!=-1){
//			System.out.println(new String(gets,0,len));
//		}

		/**
		 * 带行缓冲，每次读取一行，但读取的字符串不包含换行符\n
		 */
		BufferedReader br=new BufferedReader(inr);
		String line=null;
		while((line=br.readLine())!=null){
			System.out.println(line);
		}
	}
	
	public static void readstd() throws IOException{
		/**
		 * 挨个读取标准输入流字符
		 */
		InputStream in=System.in;
		InputStreamReader inr=new InputStreamReader(in);
		int c;
		while((c=inr.read())!=-1){
			if(c!='\n')
				System.out.println((char)c);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
