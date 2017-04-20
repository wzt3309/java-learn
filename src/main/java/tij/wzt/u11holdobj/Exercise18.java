package tij.wzt.u11holdobj;
import java.util.*;
/**
 * unit11 exercise18
 */
public class Exercise18{
	public static void main(String[] args){
		Map<String,Integer> map=new HashMap<String,Integer>();
		Random rand=new Random(47);
		for(int i=0;i<10;i++)
			map.put(String.valueOf(i),rand.nextInt(10));
		// Set<String> keySet=map.keySet();
		// List<String> keys=new ArrayList<String>(keySet);
		// Collections.sort(keys);
		
		String[] keys=map.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		Map<String,Integer> result=new LinkedHashMap<String,Integer>();
		for(String key:keys)
			result.put(key,map.get(key));
		System.out.println(result);
	}
}