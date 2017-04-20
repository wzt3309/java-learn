package tij.wzt.u11holdobj;
import java.util.*;
public class SimpleLinkedList{
	private static LinkedList<Integer> list=new LinkedList<Integer>(Arrays.asList(1));
	public static void main(String[] args){
		/*insert*/
		list.add(2);
		System.out.println("after add()"+list);
		list.addFirst(0);
		System.out.println("after add()"+list);
	}
}