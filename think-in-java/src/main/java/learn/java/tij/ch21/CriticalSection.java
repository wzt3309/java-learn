package learn.java.tij.ch21;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程不安全的Pair
 * @author wzt
 *
 */
class Pair {
	private int x;
	private int y;
	
	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Pair() {
		this(0, 0);
	}
	
	public void incrementX() { x++; }
	public void incrementY() { y++; }
	public int getX() { return x; }
	public int getY() { return y; }
	
	public String toString() {
		return String.format("(%d, %d)",x ,y);
	}
	
	public class PairValuesNotEqualException 
	extends RuntimeException {
		public PairValuesNotEqualException() {
			super("Pair values not equal: " + Pair.this);
		}
	}
	public void checkState() {
		if (x != y) {
			throw new PairValuesNotEqualException();
		}
	}	
}
// protected Pair in a thread-safe class
abstract class PairManager {
	AtomicInteger checkCount = new AtomicInteger();
	protected Pair pair = new Pair();
	private List<Pair> storage =
			Collections.synchronizedList(new ArrayList<>());
	
	public synchronized Pair getPair() {
		//make a copy to keep the original safe
		return new Pair(pair.getX(), pair.getY());
	}
	
	protected void store(Pair pair) {
		storage.add(pair);
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException ignore) {
		}
	}
	
	public abstract void increment();
}

// synchronized the entire method
class PairManager1 extends PairManager {
	@Override
	public synchronized void increment() {
		pair.incrementX();
		pair.incrementY();
		store(getPair());
	}
}

//use critical section
class PairManager2 extends PairManager {
	@Override
	public void increment() {
		Pair temp;
		synchronized(this) {
			pair.incrementX();
			pair.incrementY();
			temp = getPair();
		}
		store(temp);
	}
}

class PairManager3 extends PairManager {
	private ReentrantLock lock = new ReentrantLock();
	
	@Override
	public Pair getPair() {
		//make a copy to keep the original safe
		lock.lock();
		try {
			return new Pair(pair.getX(), pair.getY());
		} finally {
			lock.unlock();
		}		
	}
	@Override
	public void increment() {
		Pair temp;
		lock.lock();
		try {
			pair.incrementX();
			pair.incrementY();
			temp = getPair();
		}finally {
			lock.unlock();
		}
		store(temp);
	}
	
}
class PairManipulator implements Runnable {
	private PairManager pm;
	public PairManipulator(PairManager pm) {
		this.pm = pm;
	}
	
	@Override
	public void run() {
		while(true) {
			pm.increment();
		}
	}
	
	@Override
	public String toString() {
		return String.format("Pair: %s checkcCounter = %d",
				pm.getPair(), pm.checkCount.get());
	}
}

class PairChecker implements Runnable {
	private PairManager pm;
	public PairChecker(PairManager pm) {
		this.pm = pm;
	}
	
	@Override
	public void run() {
		while(true) {
			pm.checkCount.incrementAndGet();
			pm.getPair().checkState();
		}
	}	
}

public class CriticalSection {
	
	public static void 
	testApproaches(PairManager pman1, PairManager pman2) {
		ExecutorService exec = Executors.newCachedThreadPool();
		PairManipulator pm1, pm2;
		pm1 = new PairManipulator(pman1);
		pm2 = new PairManipulator(pman2);
		PairChecker pc1, pc2;
		pc1 = new PairChecker(pman1);
		pc2 = new PairChecker(pman2);
		exec.execute(pm1);
		exec.execute(pm2);
		exec.execute(pc1);
		exec.execute(pc2);
		
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException ignore) {
			System.out.println("Sleep Interrupted");
		}
		System.out.format("pm1:%s, pm2:%s\n", pm1, pm2);
		System.exit(0);
	}
	
	public static void main(String[] args) {
		testApproaches(new PairManager1(), new PairManager3());
	}
}














