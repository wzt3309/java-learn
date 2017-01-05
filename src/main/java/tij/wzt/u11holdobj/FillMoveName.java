package tij.wzt.u11holdobj;
import java.util.*;

import tij.wzt.help.*;
public class FillMoveName{
	private static final MoveNameGeneator generator=new MoveNameGeneator();
	public static String[] fill(String[] array){
		for(int i=0;i<array.length;i++)
			array[i]=generator.next();
		return array;

	}
	public static Collection<String> fill(Collection<String> collection){
		for(int i=0;i<5;i++)
			collection.add(generator.next());
		return collection;
	}
}
class MoveNameGeneator implements Generator<String>{
	String[] moves={"Grumpy", "Happy", "Sleepy", "Dopey", "Doc", "Sneezy",
					"Bashful", "Snow White", "Witch Queen", "Prince",};
	int next;
	public String next(){
		String result=moves[next];
		next=(next+1)%moves.length;
		return result;
	}
}
