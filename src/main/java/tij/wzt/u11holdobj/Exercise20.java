package tij.wzt.u11holdobj;
import java.util.*;
/**
 * unit11 exercise20
 */
public class Exercise20{
	public static void main(String[] args){
		String[] strs="Thinking Think in java".split(" ");
		char[] aeios="aeiouAEIOU".toCharArray();
		Map<Character,Integer> aeio=new LinkedHashMap<Character,Integer>();
		for(int i=0;i<aeios.length;i++)
			aeio.put(aeios[i],0);
		for(int i=0;i<strs.length;i++){
			char[] chars=strs[i].toCharArray();
			for(int j=0;j<chars.length;j++){
				if(aeio.containsKey(chars[j])){
					int num=aeio.get(chars[j]);
					aeio.put(chars[j],++num);
				}
			}
		}
		System.out.println(aeio);
	}
}