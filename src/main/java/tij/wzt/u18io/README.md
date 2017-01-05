##来源《Think in Java》

>java1.1开始的老io java.io.,java1.4中加入java.nio. 1.7发扬光大 见jxl.wzt.u2nio


###InputStream 有6大子类除了FilterInputStream外，其他都代表一种数据源
	  
	  **ByteArrayInputStream(byte)	从内存中读取字节数组**
	  StringBufferInputStream(String) 从StringBuffer读取 方法已经被取消
	  **FileInputStream(String->fileName) 文件流**
	  **PipedInputStream(PipedOutputStream) 管道流，多线程中线程通信**
	  SequenceInputStream(连个InputStream 或者容纳了InputStream的容器Enumeration) 多个流进行合并
	  FilterInputStream 装饰器
	  	**DataInputStream(InputStream) 扩展了可用于读取基本数据类型int char long**
	  	**BufferedInputStream(InputStream) 带缓冲的InputStream 先将文件的内容全部读到缓冲区，再
	    用byte[]或挨个从内从中读取，而普通的InputStream直接调用系统的（native）read方法byte[]或者
	    挨个从硬盘上读取数据**
	   LineNumberInputStream(InputStream) 可跟踪流里的行号
	   PushbackInputStream(InputStream) 可回退的流，java编译器需要，基本用不到
	   
###Reader 其read实现，是从StreamDecoder调取的，由StreamDecoder进行转码
	   
	  **InputStreamReader -> Reader(国际化，字符，不乱码)**
	   **FileReader(String->fileName boolean ture=添加)**
	  CharArrayReader(空或int->buf大小)
	  StringReader(空或int->buf大小)
	  PipedReader(PipedReader)
	  **BufferedReader(Reader)**
	  	 LineNumberReader(Reader)
	  FilterReader 抽象类 
	  	PushbackReader
	  
###OutputStream 有4大子类除了FilterOutputStream外，其他都代表输出位置
	  
	  **ByteArrayOutputStream(byte) 向内存输出内容**
	  **FileOutputStream(String->fileName) 向文件写内容**
	  **PipedOutputStream(PipedInputStream) 向管道流中输出 进程间通信**
	  FilerOutputStream
	  	DataOutputStream(OutputStream) 扩展了可以写基本数据类型 
	  	**PrintStream(OutputStream) 用于格式化输出**
	  	**BufferedOutputStream(OutputStream) 带缓冲区**
	  	
###Writer 其writer实现，是从StreamEncoder调取的，由StreamEncoder进行转码	 
 
	  **OutputStreamWriter -> Writer(国际化，字符，不乱码)**
	  	**FileWriter(String->fileName boolean ture=添加)**
	  CharArrayWriter(空或int->buf大小)
	  StringWriter(空或int->buf大小)
	  PipedWriter(PipedReader)
	  **BufferedWriter(Writer)**
	  **PrintWriter(Writer)**
	  