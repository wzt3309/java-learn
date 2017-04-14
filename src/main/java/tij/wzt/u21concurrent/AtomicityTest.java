package tij.wzt.u21concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicityTest implements Runnable{
	private int val = 0;
	
	public int getVal() {
		return val;
	}
	
	public synchronized void evenIncrement() {
		val++;
		val++;
	}

	@Override
	public void run() {
		while(true) {
			evenIncrement();
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		AtomicityTest at = new AtomicityTest();
		exec.execute(at);
		
		while(true) {
			int val = at.getVal();
			if(val % 2 != 0) {
				System.out.println("not even " + val);
				System.exit(0);
			}
		}
	}
}
