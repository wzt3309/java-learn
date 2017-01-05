package tij.wzt.u9interface;

/**
 * unit9 exercise18
 */
public class Exercise18{
	public static void forward(CycleFactory factory){
		Cycle cycle=factory.getCycle();
		cycle.forward();
	}
	public static void main(String[] args){
		CycleFactory[] factories=new CycleFactory[]{
			new UnicycleFactory(),
			new BicycleFactory(),			
			new TricycleFactory(),
		};
		for(CycleFactory factory:factories)
			forward(factory);
	}
}
interface Cycle{
	public void forward();
}
interface CycleFactory{
	public Cycle getCycle();
}
class Unicycle implements Cycle{
	public void forward(){
		System.out.println("Unicycle forward");
	}
}
class Bicycle implements Cycle{
	public void forward(){
		System.out.println("Bicycle forward");
	}
}
class Tricycle implements Cycle{
	public void forward(){
		System.out.println("Tricycle forward");
	}
}
class UnicycleFactory implements CycleFactory{
	public Cycle getCycle(){
		return new Unicycle();
	}
}
class BicycleFactory implements CycleFactory{
	public Cycle getCycle(){
		return new Bicycle();
	}
}
class TricycleFactory implements CycleFactory{
	public Cycle getCycle(){
		return new Tricycle();
	}
}