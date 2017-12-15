package learn.java.tij.ch21;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**   
 *  
 * @author wzt3309 
 */
public class CallFibMain {

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<String>> result = new ArrayList<>();
		for(int i = 0;i < 5;i++) {
			result.add(exec.submit(new CallFib(10)));
		}
		exec.shutdown();
		for(Future<String> re: result) {
			try {
				System.out.print(re.get()+" ");
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
}

class CallFib implements Callable<String> {
	private static int taskNum = 0;
	private final int id = taskNum++;
	private int a = 0;
	private int b = 1;
	private int n;
	
	CallFib(int n) {
		this.n = n;
	}
	private int fib() {
		int c = a+b;
		a = b;
		b = c;
		return a;
	}
	@Override
	public String call() throws Exception {
		int sum = 0;
		for(int i = 0;i < n;i++) {
			sum += fib();
		}
		return String.format("task%d(%d)", id, sum);
	}

}
