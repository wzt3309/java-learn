package tij.wzt.help;
import java.util.*;
public class Stack<T>{
	private LinkedList<T> list=new LinkedList<T>();
	public void push(T t){
		list.addFirst(t);
	}
	public T peek(){
		return list.getFirst();
	}
	public T pop(){
		return list.removeFirst();
	}
}