package jnp.wzt.ch03_thread;
/**   
 *  
 * @author wzt3309 
 */
public class RunThread {

	public static void main(String[] args) {
		Thread thread1 = new Thread(new MyTask());
		thread1.run();
		System.exit(1);
	}

}

class MyTask implements Runnable{

	@Override
	public void run() {
		while(true) {
			System.out.println("aaa");
		}
	}
	
}
