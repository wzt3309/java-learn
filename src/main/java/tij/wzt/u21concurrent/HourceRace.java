package tij.wzt.u21concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Hource implements Runnable{
	private static int counter = 0;
	private static Random rand = new Random(47);	//线程安全的，可以不继续同步
	private final int id = ++counter;
	private CyclicBarrier barrier;
	private int moveStep = 0;	//因为是等全部跑马进程阻塞后，才执行获取该数字的进程，因此可以不继续同步
	public Hource(CyclicBarrier barrier) {
		this.barrier = barrier;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				moveStep += rand.nextInt(3);
				barrier.await();
			}
		} catch (InterruptedException | 
				BrokenBarrierException ignore) {
			System.out.println(this + " be interrupted ");
		}		
	}
	public int getMoveStep() {
		return moveStep;
	}
	public void trace() {		
		for(int i = 0;i < moveStep;i++) {
			System.out.print("*");
		}
		System.out.print(this + " " + moveStep);
	}
	
	@Override
	public String toString() {
		return "Hource(" + id + ")";
	}	
}
public class HourceRace {
	public static void main(String[] args) {
		final List<Hource> hources = new ArrayList<>();
		final int MAX = 75;
		final ExecutorService exec = Executors.newCachedThreadPool();
		int hourceNum = 7;
		CyclicBarrier barrier = new CyclicBarrier(hourceNum, new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0;i < MAX;i++) {
					System.out.print("=");
				}
				System.out.println();
				boolean raceOver = false;
				for(Hource hource: hources) {
					hource.trace();
					if(hource.getMoveStep() >= MAX) {
						raceOver = true;
						System.out.print(" WIN !");
					}
					System.out.println();
				}
				if(raceOver) {
					exec.shutdownNow();
					System.out.println("GAME OVER!");
				}				
			}
		});
		
		for(int i = 0;i < hourceNum;i++) {
			hources.add(new Hource(barrier));
		}
		for(Hource hource: hources) {
			exec.execute(hource);
		}
	}
}
