package learn.java.tij.ch21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 多线程协作，想waxOn再buffer，同时有多个waxOn或buffer线程
 * @author wzt
 *
 */
class Car {
	private boolean waxOn = false;
	public synchronized void waxed(int id) 
			throws InterruptedException {
		if(waxOn == true) {
			waitForBuffered();
		}
		waxOn = true;
		System.out.println("#id-" + id + "waxOn");
		TimeUnit.MILLISECONDS.sleep(100);
		notifyAll();
	}
	public synchronized void buffered(int id) 
			throws InterruptedException {
		if(waxOn == false) {
			waitForWaxed();
		}
		waxOn = false;
		System.out.println("#id-" + id + "bufferOn");
		TimeUnit.MILLISECONDS.sleep(100);
		notifyAll();
	}
	public synchronized void waitForWaxed() 
			throws InterruptedException {
		while(waxOn == false) {
			wait();
		}
	}
	public synchronized void waitForBuffered()
			throws InterruptedException {
		while(waxOn == true) {
			wait();
		}
	}
}
class WaxOn implements Runnable {
	private static int taskNum = 0;
	private final int id = ++taskNum;
	private Car car;
	public WaxOn(Car car) {
		this.car = car;
	}
	@Override
	public void run() {
		try{
			while(!Thread.interrupted()) {				
				car.waxed(id);		
				Thread.yield(); //加速问题产生，如果有的话
				car.waitForBuffered();
			}
			System.out.println("waxOn out of while");
		}catch(InterruptedException ignore) {
			System.out.println("waxOn be interrupted");
		}
		System.out.println("Stop waxOn");
	}
}
class BufferOn implements Runnable {
	private static int taskNum = 0;
	private final int id = ++taskNum;
	private Car car;
	public BufferOn(Car car) {
		this.car = car;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				car.waitForWaxed();				
				car.buffered(id);
			}
			System.out.println("bufferOn out of while");
		} catch (InterruptedException ignore) {
			System.out.println("waxOn be interrupted");
		}
		System.out.println("Stop bufferOn");
	}	
}
public class WaxOMatic {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Car car = new Car();
		for(int i = 0;i < 3;i++) {
			exec.execute(new WaxOn(car));
			exec.execute(new BufferOn(car));
		}		
		TimeUnit.SECONDS.sleep(3);
		exec.shutdownNow();
		if(!exec.awaitTermination(1, TimeUnit.SECONDS))
			System.out.println("threads haven't stop");
		System.out.println("WaxOMatic off");
//		System.exit(0);
	}
}
















