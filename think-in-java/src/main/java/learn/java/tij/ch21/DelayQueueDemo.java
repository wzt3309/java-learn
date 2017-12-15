package learn.java.tij.ch21;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 不实现Runnable都行，因为直接通过一个线程从DelayQueue中take任务，调用任务run()方法来执行
 * 而非使用启动器或者线程方式
 * @author wzt
 *
 */
class DelayedTask implements Runnable, Delayed {
	private static int counter = 0;
	protected static List<DelayedTask> delayedTasks = new ArrayList<>();
	private final int id = ++counter;
	private final int delay;
	private final long trigger;
	public DelayedTask(int delay) {
		this.delay = delay;
		trigger = System.nanoTime() + 
				TimeUnit.NANOSECONDS.convert(delay, 
						TimeUnit.MILLISECONDS);
		delayedTasks.add(this);
	}
	@Override
	public int compareTo(Delayed o) {
		DelayedTask other = (DelayedTask) o;
		if(this.trigger < other.trigger) return -1;
		if(this.trigger > other.trigger) return 1;
		return 0;
	}
	/**
	 * 计算还差多久到期
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(System.nanoTime() - this.trigger, 
				TimeUnit.NANOSECONDS);
	}

	@Override
	public void run() {
		System.out.println(this + " ");
	}
	
	@Override
	public String toString() {
		return String.format("Run([%-4d]-task[%d])", delay, id);
	}
}
public class DelayQueueDemo {
	public static void main(String[] args) {
		// JKD1.8K可以不加final语法糖
		final DelayQueue<DelayedTask> queue = new DelayQueue<>();
		final ExecutorService exec =Executors.newCachedThreadPool();
		Random random = new Random();
		for(int i = 0;i < 10;i++) {
			queue.put(new DelayedTask(random.nextInt(5000)));
		}
		// 添加最后一个任务来结束启动器
		queue.put(new DelayedTask(5000) {
			@Override
			public void run() {
				System.out.println("queue is over");
				exec.shutdownNow();
			}
		});	
		// 用一个线程按优先队列顺序运行
		exec.execute(new Runnable() {			
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					try {
						queue.take().run();
					} catch (InterruptedException e) {						
						e.printStackTrace();
					}
				}
			}
		});
	}
}
