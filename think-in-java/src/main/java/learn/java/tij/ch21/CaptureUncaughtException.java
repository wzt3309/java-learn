package learn.java.tij.ch21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 未在线程run中处理的异常由Thread.UncaughtException子类处理
 * @author wzt
 *
 */
class ExceptionThread implements Runnable {

	@Override
	public void run() {
		Thread t = Thread.currentThread();
		System.out.println("run by " + t);
		System.out.println(
				"en = " + t.getUncaughtExceptionHandler());
		throw new RuntimeException();
	}	
}

class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("catch " + e);
	}
	
}

class HandlerThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		System.out.println(this + " creating new Thread");
		Thread t = new Thread(r);
		System.out.println(t + "is created");
		t.setUncaughtExceptionHandler(new UncaughtExceptionHandler());
		System.out.println(
				"en = " + t.getUncaughtExceptionHandler());
		return t;
	}
	
}

public class CaptureUncaughtException {

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
		exec.execute(new ExceptionThread());
		exec.shutdown();
	}

}
