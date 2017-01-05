package com.wzt.unit11;
import java.util.*;
import java.util.Map.Entry;
/**
 * unit11 exercise17
 */
public class Exercise17{
	public static void main(String[] args){
		Map<String,Gerbi> map=new HashMap<String,Gerbi>();
		map.put("test1",new Gerbi());
		map.put("test2",new Gerbi());
		map.put("test3",new Gerbi());
		Iterator<Entry<String,Gerbi>> it=map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,Gerbi> entry=it.next();
			System.out.print(entry.getKey()+":"+entry.getValue().hop()+" ");
		}
		System.out.println();
	}
}
class Gerbi{
	private static long count=0;
	private final long id=count++;
	public String hop(){
		return "Gerbi_"+id;
	}
}