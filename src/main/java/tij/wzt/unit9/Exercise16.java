package tij.wzt.unit9;
import java.util.Random;
import java.util.Scanner;
import java.nio.CharBuffer;
/**
 * unit9 exercise16
 */
public class Exercise16{
	public static void main(String[] args){
		Scanner sc=new Scanner(new RandCharAdaptor(5));		
		while(sc.hasNext())
			System.out.println(sc.next());
	}
}
class RandomChar{
	private Random random=new Random(47);
	public char next(){
		int flag=random.nextInt(2);
		char c=0;
		switch(flag){
			case 0:c=(char)(random.nextInt(26)+65);break;
			case 1:c=(char)(random.nextInt(26)+97);break;
		}		
		return c;
	}	
}
class RandCharAdaptor extends RandomChar implements Readable{
	private int count;
	public RandCharAdaptor(int count){
		this.count=count;
	}
	public int read(CharBuffer charbuf){
		if(count--==0)
			return -1;	
		for(int i=0;i<10;i++)			
			charbuf.append(next());		
	 	charbuf.append('\n');
		return 10;
	} 
}