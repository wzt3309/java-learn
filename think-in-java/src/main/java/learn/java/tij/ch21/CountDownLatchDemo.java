package learn.java.tij.ch21;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TaskPortion implements Runnable{
	private static int countTasks = 0;
	private final int taskId = ++countTasks;
	private CountDownLatch count;
	public TaskPortion(CountDownLatch count) {
		this.count = count;
	}
	public void doWork() {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
			System.out.println(this + " complete");
			count.countDown();
		} catch (InterruptedException e) {
			System.out.println(this + " be interrupted");
		}		
	}
	@Override
	public void run() {
		doWork();
	}
	@Override
	public String toString() {
		return String.format("Task(%d)", taskId);
	}
}

class WaitingTask implements Runnable {
	private static int counter = 0;
	private final int id = ++counter;
	private CountDownLatch latch;
	public WaitingTask(CountDownLatch latch) {
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			latch.await();
			System.out.println(this + " pass");
		} catch (InterruptedException e) {
			System.out.println(this + " wait is Interrupted");
		}
		
	}
	@Override
	public String toString() {
		return String.format("Waiting(%d)", id);
	}
	
	
}
public class CountDownLatchDemo {
	static final int SIZE = 10;
	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(SIZE);
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;i++) {
			exec.execute(new WaitingTask(latch));
		}
		for(int i = 0;i < SIZE;i++) {
			exec.execute(new TaskPortion(latch));
		}
		exec.shutdown();
	}
}
