package tij.wzt.u21concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLock {
	//显示定义锁，与对象绑定
	private ReentrantLock lock = new ReentrantLock();
	
	public void utime() {
		boolean captured = lock.tryLock();
		try{
			System.out.println("utime captured: " + captured);
		}finally {
			if(captured) {
				lock.unlock();
			}
		}
	}
	
	public void time() {
		boolean captured = false;
		
		try {
			captured = lock.tryLock(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("time captured: " + captured);
		} finally {
			if(captured) {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		// 传入匿名内部类，需要定义为final
		final AttemptLock aLock = new AttemptLock();
		aLock.utime();
		aLock.time();
		
		new Thread() {			
			@Override
			public void run() {
				aLock.lock.lock();
			}
		}.start();
		//通知主线程可以切换，并阻塞，从而让2nd线程运行，获取锁
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		aLock.utime();
		aLock.time();		
	}
}
