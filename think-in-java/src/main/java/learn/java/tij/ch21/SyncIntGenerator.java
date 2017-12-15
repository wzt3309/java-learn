package learn.java.tij.ch21;

public class SyncIntGenerator extends IntGenerator {
	private int currentEvenVal = 0;
	@Override
	public synchronized int next() {
		currentEvenVal++;
		// 如果有问题，就让问题快点出现
		Thread.yield();
		currentEvenVal++;
		return currentEvenVal;
	}

	

}
