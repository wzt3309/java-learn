package tij.wzt.u21concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**   
 *  
 * @author wzt3309 
 */
public class LifeOffMain {

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;i++) {
			exec.execute(new LifeOff());
		}
		exec.shutdown();
		exec.execute(new LifeOff());
	}

}

class LifeOff implements Runnable {
	private int countdown = 10;
	private static int taskNum = 0;
	private final int id = taskNum++;
	
	private String showMe() {
		return String.format("#%d(%s) ", id,
				(countdown > 0 ? countdown : "off"));
	}
	@Override
	public void run() {
		while(countdown-- > 0) {
			System.out.print(showMe());
			Thread.yield();
		}
	}
	
}
