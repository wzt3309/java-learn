package tij.wzt.unit8;
/**
 * example Sandwich.java
 * Sup must be constructed before Sub
 */
public class Sandwich extends PortableLunch{
	private Cheese cheese=new Cheese();
	public Sandwich(){
		System.out.println("6 Sandwich constructed");
	}
	public static void main(String[] args){
		new Sandwich();
	}
}
class Meal{
	public Meal(){
		System.out.println("1 Meal constructed");
	}
}
class Cheese{
	public Cheese(){
		System.out.println("5 Cheese constructed");
	}
}
class Bread{
	private Meal meal=new Meal();
	public Bread(){
		System.out.println("2 Bread constructed");
	}
}
class Lunch extends Bread{
	public Lunch(){
		System.out.println("3 Lunch constructed");
	}
}
class PortableLunch extends Lunch{
	public PortableLunch(){
		System.out.println("4 PortableLunch constructed");
	}
}
/** output
1 Meal constructed
2 Bread constructed
3 Lunch constructed
4 PortableLunch constructed
5 Cheese constructed
6 Sandwich constructed

 */