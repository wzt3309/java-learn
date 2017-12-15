package learn.java.tij.ch21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

class LiftOff {
	private final int id;
	public LiftOff(int id) {
		this.id = id;
	}
	public void run() {
		System.out.println(this + " run");
	}
	@Override
	public String toString() {
		return String.format("LiftOff(%d)", id);
	}
}

class LiftOffRunner implements Runnable {
	private BlockingQueue<LiftOff> rockets;
	public LiftOffRunner(BlockingQueue<LiftOff> rockets) {
		this.rockets = rockets;
	}
	
	public void add(LiftOff elem) {
		try {
			rockets.put(elem);
		} catch (InterruptedException e) {
			System.out.println("Interrupted during put()");
		}
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				rockets.take().run();
			}
		} catch (InterruptedException e) {
			System.out.println("walking from take()");
		}
		System.out.println("Exiting LiftOffRunner");
	}
}
public class TestBlockQueues {
	static void getKey() {
		try {
			new BufferedReader(
					new InputStreamReader(
							System.in)).readLine();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	static void getKey(String msg) {
		System.out.println(msg);
		getKey();
	}
	static void test(String msg, BlockingQueue<LiftOff> queue) {
		System.out.println(msg);
		LiftOffRunner runner = new LiftOffRunner(queue);
		Thread t = new Thread(runner);
		t.start();
		for(int i = 0;i < 5;i++) {
			runner.add(new LiftOff(i));
		}
		getKey("Press Entry ( " + msg + ")");
		t.interrupt();
		System.out.println("Finished " + msg + " test");
	}
	
	public static void main(String[] args) {
		test("LinkedBlockQueue", new LinkedBlockingQueue<LiftOff>());
		test("ArrayBlockQueue", new ArrayBlockingQueue<LiftOff>(3));
		test("SynchronousQueue", new SynchronousQueue<LiftOff>());
	}
 }






