package tij.wzt.ch09_interface;

public class OutterOrInnerImpl{
	public static void main(String[] args){
		A a=new A();
		A.Bimpl b=a.new Bimpl();
		b.f();
		a.exec(a.getB());
		/**
		 * error,getB() cannot cast to A.Bimpl 
		 * but just A.B which is privaet and cannot be indecated
		 */
		//b=getB();
	}
}
class A{
	private interface B{
		/**
		 * inner interface only public elements
		 */
		public void f();
	}
	public class Bimpl implements B{
		public void f(){
			System.out.println("fo");
		}
	}
	public B getB(){
		return new Bimpl();
	}
	public void exec(B b){
		b.f();
	}
	

}