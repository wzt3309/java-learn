package tij.wzt.ch21_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**   
 *  
 * @author wzt3309 
 */
public class SleepTaskMain {

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;i++) {
			exec.execute(new SleepTask());
		}
		exec.shutdown();
	}
}

class SleepTask implements Runnable{
	private static int taskNum = 0;
	private final int id = taskNum++;
	@Override
	public void run() {
		int sleep = (int)(Math.random() * 100 + 1);
		try {
			TimeUnit.MILLISECONDS.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.format("taskNum%d sleep(%d) ", id, sleep);
	}
	
}
