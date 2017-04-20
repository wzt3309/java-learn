package tij.wzt.u9interface;
/**
 * unit9 exercise11
 */
public class Exercise11{
	public static void main(String[] args){
		Apply.process(new StringAdaptor(new StringFilter()),"ABCD");
	}
}
class Apply{
	public static void process(Processor p,Object o){
		System.out.println(p.name());
		System.out.println(p.process(o));
	}
}
interface Processor{
	String name();
	Object process(Object o);
}
class StringAdaptor implements Processor{
	StringFilter f;
	public StringAdaptor(StringFilter f){
		this.f=f;
	}
	public String name(){
		return f.getClass().getSimpleName();
	}
	public String process(Object o){
		return f.swapString((String)o);
	}
}
class StringFilter{
	public String swapString(String s){		
		char[] arr=s.toCharArray();		
		int len=arr.length;
		int half=len/2;
		for(int i=0;i<half;i++){
			char temp=arr[i];
			arr[i]=arr[len-i-1];
			arr[len-i-1]=temp;
		}
		return String.valueOf(arr);
	}
}