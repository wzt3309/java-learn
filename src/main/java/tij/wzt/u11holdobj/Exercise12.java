package tij.wzt.u11holdobj;
import java.util.*;

/**
 * unit11 exercis12
 */
public class Exercise12{
	public static void main(String[] args){
		Integer[] nums={1,2,3,4,5};
		List<Integer> listASC=Arrays.asList(nums);
		List<Integer> listDESC=new ArrayList<Integer>();

		ListIterator<Integer> listIt=listASC.listIterator(listASC.size());
		while(listIt.hasPrevious()){
			listDESC.add(listIt.previous());
		}
		System.out.println("ASC:"+listASC);
		System.out.println("DESC:"+listDESC);

		listIt=listASC.listIterator(0);
		while(listIt.hasPrevious()){
			System.out.println(listIt.previous());
			listIt.set(10);
		}
		System.out.println("Change ASC:"+listASC);
	}
}