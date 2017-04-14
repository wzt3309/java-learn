package tij.wzt.u21concurrent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Accessor implements Runnable {
	private final int id;
	public Accessor(int id) {
		this.id = id;
	}
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			ThreadLocalVariableHolder.increment();
			System.out.println(this);
			Thread.yield();
		}
	}
	
	@Override
	public String toString() {
		return "ID: " + id + ", " +ThreadLocalVariableHolder.get();
	}
	
}
public class ThreadLocalVariableHolder {
	private static ThreadLocal<Integer> vaLocal =
			new ThreadLocal<Integer>() {
		private Random rand = new Random();
		// 每个线程第一次调用ThreadLocal对象set时，想要调用这个
		// 方法，将返回值存入本地副本中，为了保证读取时，不被打断，不读到不稳定的数据
		// 此处加上同步
		// 之后的get，set方法，都是从本地获取，因此不需要同步
		@Override
		protected synchronized Integer initialValue() {
			return rand.nextInt(10000);
		}
	};
	
	public static void increment() {
		vaLocal.set(vaLocal.get() + 1);
	}
	
	public static Integer get() { return vaLocal.get(); }
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;i++) {
			exec.execute(new Accessor(i));
		}
		TimeUnit.SECONDS.sleep(1);
		exec.shutdown();
	}
	
}
