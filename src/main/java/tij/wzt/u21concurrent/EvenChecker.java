package tij.wzt.u21concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenChecker implements Runnable{
	private IntGenerator generator;
	private final int id;
	
	public EvenChecker(IntGenerator generator, int id) {
		this.generator = generator;
		this.id = id;
	}
	@Override
	public void run() {
		while (!generator.isCanceled()) {
			int val = generator.next();
			if(val % 2 != 0) {
				System.out.println(
						id + "--" + val + " is not even");
				//就像是开火车，越过val % 2的火车全部停在了这个
				//没越过while的火车，因为次补操作，全部终止了
				// 此处操作为原子性的，及时阻止其他检查器再进行获取数字
				generator.canceled();
			}
		}
	}
	
	public static void test(IntGenerator generator, int count) {
		System.out.println("Please Ctrl + c to exit!");
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < count; i++) {
			exec.execute(new EvenChecker(generator, i));
		}
		exec.shutdown();
	}
	
	public static void test(IntGenerator generator) {
		test(generator, 10);
	}
	
	public static void main(String[] args) {
//		EvenChecker.test(new IntGenerator() {
//			int currentEvenVal = 0;
//			@Override
//			public int next() {
//				currentEvenVal++;	// 危险操作
//				currentEvenVal++;
//				return currentEvenVal;
//			}
//		});
		EvenChecker.test(new SyncIntGenerator());
	}
}
