package com.wzt.unit11;
import java.util.*;
public class SimpleIterator{
	public static void main(String[] args){
		List<Integer> list=new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6));
		Iterator<Integer> it=list.iterator();
		display(it);
		it=list.iterator();
		for(int i=0;i<3;i++){
			it.next();
			it.remove();
		}		
		display(it);

	} 
	public static void display(Iterator<Integer> it){
		while(it.hasNext()){
			System.out.print(it.next());
			if(it.hasNext())	
				System.out.print(",");
			else
				System.out.println();
		}
	}
}