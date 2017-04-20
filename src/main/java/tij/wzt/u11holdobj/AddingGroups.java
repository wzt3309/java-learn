package tij.wzt.u11holdobj;
import java.util.*;
public class AddingGroups{
	public static void main(String[] args){
		Collection<Integer> collection=new ArrayList<Integer>();
		collection.addAll(Arrays.asList(1,2,3));
		collection.addAll(Arrays.asList(4,5,6));
		Collections.addAll(collection,7,8,9);
		System.out.println(collection);

		List<Food> list=Arrays.asList(new Apple(),new Bannal(),new Fruit());
		for(Food f:list)
			System.out.println(f);
	}
}
class Food{

}
class Fruit extends Food{

}
class Apple extends Fruit{

}
class Bannal extends Fruit{

}