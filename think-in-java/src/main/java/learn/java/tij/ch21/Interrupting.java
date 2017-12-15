package learn.java.tij.ch21;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class SleepBlock implements Runnable {

	@Override
	public void run() {
		try {
			System.out.println("Block by sleep");
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException ignore) {
			System.out.println("Interrupted Exception");
		}
		System.out.println("Interrupted from block sleep");
	}
	
}

class IOBlock implements Runnable {
	private InputStream in;
	public IOBlock(InputStream in) {
		this.in = in;
	}
	@Override
	public void run() {
		try {
			System.out.println("block by I/O");
			in.read();
		} catch (IOException e) {
			// 通过关闭底层资源的方式中断阻塞
			if(Thread.currentThread().isInterrupted()) {
				System.out.println("IO be Interrupted");
			}else {
				throw new RuntimeException();
			}			
		}
		System.out.println("Exiting block I/O");
		
	}
	
}
class SyncBlock implements Runnable {
	public synchronized void f() {
		while(true) {
			Thread.yield();
		}
	}
	public SyncBlock() {
		new Thread() {
			public void run() {
				f();
			}
		}.start();
	}
	@Override
	public void run() {
		System.out.println("Block by sync");
		f();
		System.out.println("Interrupted from block sync");
	}
	
}
public class Interrupting {
	private static ExecutorService exec = Executors.newCachedThreadPool();
	public static void test(Runnable r) throws InterruptedException {
		Future<?> future = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getName());
		future.cancel(true);
		System.out.println("Interrupt sent to " + r.getClass().getName());
	}

	public static void main(String[] args) throws Exception {
//		test(new SleepBlock());		
		test(new IOBlock(System.in));
//		test(new SyncBlock());
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("close in");
		System.in.close();
//		System.exit(0);
	}

}
