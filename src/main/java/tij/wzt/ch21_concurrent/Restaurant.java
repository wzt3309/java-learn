package tij.wzt.ch21_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Meal {
	private final int orderNum;
	public Meal(int orderNum) { this.orderNum = orderNum; }
	@Override
	public String toString() {
		return "Meal(" + orderNum + ")";
	}
}
class WaitPerson implements Runnable {
	private final Restaurant restaurant;
	public WaitPerson(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized (this) {
					while(restaurant.meal == null) {
						wait();
					}
				}
				System.out.println("WaitPerson got " + restaurant.meal);
				synchronized (restaurant.chief) {
					restaurant.meal = null;
					restaurant.chief.notify();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("WaitPerson is Interrupted");
		}
	}	
}
class Chief implements Runnable {
	private final Restaurant restaurant;
	private int count = 0;
	public Chief(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized (this) {
					while(restaurant.meal != null) {
						wait();
					}
				}
				if(++count > 10) {
					System.out.println("Out of order");
					restaurant.exec.shutdownNow();
				}
				System.out.print("order up ");
				synchronized (restaurant.waitPerson) {
					restaurant.meal = new Meal(count);
					restaurant.waitPerson.notify();
				}
				synchronized (restaurant) {
					restaurant.notify();
				}
				TimeUnit.MILLISECONDS.sleep(200);
			}
		} catch (InterruptedException e) {
			System.out.println("Chief is interrupted");
		}
	}
}
public class Restaurant {
	Meal meal;
	WaitPerson waitPerson = new WaitPerson(this);
	Chief chief = new Chief(this);
	ExecutorService exec = Executors.newCachedThreadPool();
	public Restaurant() {
		exec.execute(chief);
		exec.execute(waitPerson);
	}
	public static void main(String[] args) {
		Restaurant r = new Restaurant();
		try {
			synchronized (r) {
				while(!r.exec.isShutdown()) {
					r.wait();
				}
			}
		} catch (InterruptedException ignore) {
		}
		System.out.println(r.meal);
		
		
	}
}
