package learn.java.tij.ch21;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Counter {
	private int count = 0;
	public synchronized int increment() {
		return count++;
	}
	public synchronized int getCount() {
		return count;
	}
}

class Entrance implements Runnable{
	private static Counter counter = new Counter();
	// 不需要同步，不存在多个线程对其的访问
	private static List<Entrance> entrances = new ArrayList<>();
	private static volatile boolean canceled = false;
	
	private final int id;
	// number是每个线程的本地变量，不存在多个线程对它的访问，不需要同步
	private int number = 0;
	
	public Entrance(int id) {
		this.id = id;
		entrances.add(this);
	}
	
	public static void canceled() {
		canceled = true;
	}

	@Override
	public void run() {		
		while(!canceled){
//			synchronized (this) {
				number++;
				Thread.yield();
//			}
			System.out.println(this + " total: " + counter.increment());
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException ignore) {
			}
		}
		
	}
	
	public int getNumber() { return number; }
	public static int getTotal() {	return counter.getCount();	}
	public static int sumEntrances() {
		int sum = 0;
		for(Entrance tEntrance: entrances) {
			sum += tEntrance.getNumber();
		}
		return sum;
	}
	@Override
	public String toString() {
		return "#ID " + id + ": " + getNumber();
	}
	
	
}
public class OrnamentalGarden {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 4;i++) {
			exec.execute(new Entrance(i + 1));
		}
		TimeUnit.SECONDS.sleep(2);
		Entrance.canceled();
		exec.shutdown();
		// 部分线程可能存在于休眠阻塞中，需要等休眠结束后，判断canceled才可结束
		if(!exec.awaitTermination(500, TimeUnit.MILLISECONDS))
			System.out.println("some tasks may not terminated!");
		System.out.println("Sum Entrances: " + Entrance.sumEntrances() +
				" Total: " + Entrance.getTotal());
	}

}
