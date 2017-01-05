package com.wzt.unit11;
import java.util.*;
/**
 * unit11 exercise12
 */
public class Exercise11{
	public static void print(Iterator<?> it){
		while(it.hasNext()){
			System.out.print(it.next());
			if(it.hasNext())
				System.out.print(",");
			else
				System.out.println();
		}
	}
	public static void main(String[] args){
		List<Collection<String>> list=Arrays.<Collection<String>>asList(
			new ArrayList<String>(),
			new LinkedList<String>(),
			new HashSet<String>(),
			new TreeSet<String>()
		);
		for(Collection<String> collection:list){
			FillMoveName.fill(collection);
			print(collection.iterator());
		}
	}
}
