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
	  
###NIO 管道与缓冲器

	  jdk1.4 nio里增加了FileChannel ,用于文件的读和写
	  io速度相对于io提高了，原因在于更接近操作系统的io方式：通道+缓冲器
	  通道像一个包含数据的煤矿，缓冲器是卡车，我们从卡车上取煤，不和煤矿直接交互
	  卡车`ByteBuffer`是唯一和煤矿`FileChannel`交流的媒介
	  
	  fc.read(buf)来让ByteBuffer存储字节
	  ByteBuffer需要调用flip()来准备被它人读取，需要调用clear()清空，以准备下一次读取
	  ByteBuffer.allocate(size) 产生ByteBuffer
	  ByteBuffer.wrap(byte[]) 产生ByteBuffer
	  
	  FileInputStream FileOutputStream RandomAccessFile
	  
	  in.transferTo(0,in.size(),out)或者out.transferFrom(in,0,in.size())用于Channel之间传输内容
	  
	  可以将ByteBuffer 转换成CharBuffer LongBuffer等不同类型的缓冲器,叫试图转换，其实就是在完整的存储单元内，划分不同最小读取单元。
	  比如ByteBuffer转CharBuffer，调用get时，两个两个字节读
	  多字节存储时，要考虑是高位优先还是低位优先buf.order(ByteOrder.BIG_ENDIAN高位优先/LITTLE_ENDIAN低位优先)转换
	  转换成CharBuffer（2字节一个字符，而ByteBuffer是一字节一个）时，否则会乱码。需要注意：
	  	1、要么在写数据的时候进行编码
	  	2、要么在读数据的时候进行转码
	  
	  XXXBuffer.wrap(XXX[]) 产生XXXBuffer  
	  XXXBuffer对象调用array() get(XXX[]) 产生XXX[]
	  
	  缓冲器属性 
	  	capacity 最大可存储的内容 
	  	position 当前位置
	  	limit	 当前有内容的最大位置，超过limit的地方是空的，limit必小于capacity
	  	mark	 书签位置
	  	
	  缓冲器方法
	  	clear() 清空缓冲区，position=0，limit=capacity
	  	flip() 准备好读，原本因为buf从channel里取数据，position指向末尾，此时调用次，limit=position position=0，方便从头读取
	  	limit() 返回limit
	  	mark	将mark值设置为当前position
	  	position() 当前position
	  	remaining() 返回limit-position
	   hasRemaining() limit与position间是否有数据
	   rewind() position设置为0 取消mark，相当于可从头开始读/写缓冲器
	   
	 内存映射文件，当在读写大文件时，老io会非常慢，新io中使用内存映射文件，不用进行频繁读写
	 这是因为内存映射文件首先将外存上的文件映射到内存中的一块连续区域，
	 被当成一个字节数组进行处理，读写操作直接对内存进行操作，
	 而后再将内存区域重新映射到外存文件，这就节省了中间频繁的对外存进行读写的时间，大大降低了读写时间。
	 但如果文件太大的话，就会一部分放入内存，一部分交换出去wrap，反正最终都是在操作内存中的数组







	  