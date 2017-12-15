package learn.java.tij.ch21;

import java.util.concurrent.TimeUnit;

/**   
 *  
 * @author wzt3309 
 */
public class DaemonMain {

	public static void main(String[] args) {
		Thread daemon = new Thread(new MyDaemon());
		daemon.setDaemon(true);
		daemon.start();
		System.out.println(daemon + " " + daemon.isDaemon());
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class MyDaemon implements Runnable {

	@Override
	public void run() {
		Thread[] ts = new Thread[5];
		System.out.println("MyDaemon is start");
		for(int i = 0;i < 5;i++) {
			ts[i] = new Thread(new ChildDaemon());
			ts[i].start();
			System.out.println("child Daemon "+ i + " strated");
		}
		for(Thread t: ts) {
			System.out.println(t + ":" +t.isDaemon());
		}
		while(true)
			Thread.yield();
	}
	
}

class ChildDaemon implements Runnable {

	@Override
	public void run() {
		while(true)
			Thread.yield();
	}
	
}
