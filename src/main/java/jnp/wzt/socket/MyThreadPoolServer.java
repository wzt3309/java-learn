package jnp.wzt.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPoolServer {
	private static final int THREAD_NUM = 50;
	private static final ExecutorService exec = Executors.newFixedThreadPool(THREAD_NUM);
	
	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(MyConstants.SIMPLE_CONNECTION_PORT)) {
			while(true) {
				// 不能放入try-with-resource中，因为可能线程还没启动，就运行到try-with块的finally部分
				// conn直接被关闭
				// conn要交给处理线程自己关闭
				Socket conn = server.accept();
				exec.execute(new DateRunnable(conn));
			}
		}catch (IOException e) {
			System.out.println("Can't Start Server");
		}
	}
	
	private static class DateRunnable implements Runnable {
		private Socket conn;
		public DateRunnable(Socket conn) {
			this.conn = conn;
		}
		@Override
		public void run() {
			try {
				OutputStream out = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "ASCII"));
				Date nowDate = new Date(System.currentTimeMillis());
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
				writer.write("[" + Thread.currentThread().getName() + "] ");
				writer.write("Date: ");
				writer.write(dateFmt.format(nowDate));
				writer.write("\r\n");
				writer.flush();
			}catch (IOException e) {
				System.out.println("request conn IOException");
			}finally {
				try {
					conn.close();
				} catch (IOException e) {
					System.out.println("request conn has been closed");
				}
			}
		}		
	}
}
