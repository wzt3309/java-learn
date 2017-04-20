package tij.wzt.u8polymorphism;
/**
 * unit8 exercise 1
 *
 * uppercase Unicycle Tricycle Bicycle to Cycle by ride()
 */
public class Exercise1{
	public static void test(Cycle cycle){
		cycle.ride();
	}
	public static void main(String[] args){
		test(new Unicycle());
	}
}

class Cycle{
	public void ride(){

	}
}

class Unicycle extends Cycle{
	public void ride(){
		System.out.println("Unicycle ride");
	}
}
class BiCycle extends Cycle{
	public void ride(){
		System.out.println("Bicycle ride");
	}
}
class Tricycle extends Cycle{
	public void	ride(){
		System.out.println("Tricycle ride");
	}
}