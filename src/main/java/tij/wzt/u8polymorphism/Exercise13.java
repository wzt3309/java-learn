package tij.wzt.u8polymorphism;
/**
 * Unit8 Exercise13
 */
public class Exercise13{
	public static void main(String[] args){
		Share share=new Share();
		Composing[] comps={
			new Composing(share),new Composing(share),
			new Composing(share),new Composing(share),
			new Composing(share),new Composing(share),
		};
		for(Composing comp:comps)
			comp.dispose();

		share=null;
		System.gc();

		//recover reference
		share=new Share();
		new Composing(share);
		share=null;
		System.gc();
	}
}
class Share{
	private static long count=0;
	private final long id=count++;
	private int refCount=0;

	public Share(){
		System.out.println(this+" Is Constructed");
	}
	public String toString(){
		return "Share ["+id+"]";
	}
	public void addRef(){
		refCount++;
	}
	public void dispose(){
		if(--refCount==0)
			System.out.println(this+" dispose");
	}
	public void finalize(){
		if(refCount!=0)
			System.out.println("Error: "+this+" has reference("+refCount+")");
	}
}
class Composing{
	private static long count=0;
	private final long id=count++;
	private Share share;
	public Composing(Share share){
		this.share=share;
		this.share.addRef();
		System.out.println(this+" Is Constructed");
	}
	public void dispose(){
		System.out.println(this+" dispose");
		this.share.dispose();
	}
	public String toString(){
		return "Composing ["+id+"]";
	}

}