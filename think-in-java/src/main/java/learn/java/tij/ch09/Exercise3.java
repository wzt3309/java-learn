package tij.wzt.ch09_interface;
/**
 * Unit9 Exercise3
 */
public class Exercise3{
	public static void main(String[] args){
		new Exe3_Child();
	}
}
abstract class Exe3_Base{
	public Exe3_Base(){
		System.out.println("Exe3_Base start");
		print();
		System.out.println("Exe3_Base end");
	}
	public abstract void print();
}
class Exe3_Child extends Exe3_Base{
	private int i=5;
	public Exe3_Child(){
		print();
	}
	public void print(){
		System.out.println(i);
	}
}