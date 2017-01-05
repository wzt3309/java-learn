package tij.wzt.unit8;
/**
 * Unit8 Exercise10 
 */
public class Exercise10{
	public static void main(String[] args){
		Base base=new Child();
		base.method1();
	}
}

class Base{
	public void method1(){
		method2();
	}
	public void method2(){
		System.out.println("Base method2");
	}
}
class Child extends Base{
	@Override
	public void method2(){
		System.out.println("Child method2");
	}
}
/**	output
Child method2
*/