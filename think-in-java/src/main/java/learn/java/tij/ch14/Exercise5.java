package tij.wzt.ch14_type_info;

public class Exercise5{
	public static void rotate(Shape shape){
		if(shape.getClass()==Circle.class)
			System.out.println(shape.getClass().getSimpleName()+" rotate");
	}
	public static void main(String[] args){
		rotate(new Circle());
		rotate(new Square());
		rotate(new Triangle());
	} 
}