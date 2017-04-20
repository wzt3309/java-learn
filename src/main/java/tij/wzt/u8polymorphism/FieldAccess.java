package tij.wzt.u8polymorphism;
/**
 * example FieldAccess.java
 */
public class FieldAccess{
	public static void main(String[] args){
		Sup sup=new Sub();
		/*对直接访问域，多态是关闭的*/
		System.out.println("Sup field:"+sup.field+"\tSub field:"+sup.getField());
	}
}
class Sup{
	public int field=0;
	public int getField(){
		return field;
	}
}
class Sub extends Sup{
	public int field=1;
	public int getField(){
		return field;
	}
	public int getBaseField(){
		return super.field;
	}
}