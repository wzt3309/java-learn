package com.wzt.unit11;
import java.util.*;

/**
 * unit11 exercise16
 */
public class Exercise16{
	public static void main(String[] args){
		String[] strs="Thinking java in java".split(" ");		
 		Set<Character> set=new HashSet<Character>(Arrays.asList(new Character[]{
 			'a','e','i','o','u',
 			'A','E','I','O','U',
 		}));
 		Set<String> words=new HashSet<String>();
		int total=0;
		for(int i=0;i<strs.length;i++){
			char[] chars=strs[i].toCharArray();
			int single=0;
			for(int j=0;j<chars.length;j++){
				if(set.contains(chars[j])){
					single++;
					total++;
				}				
			}
			/**
			 * if has same words just output once
			 */
			if(!words.contains(strs[i])){
				words.add(strs[i]);
				System.out.println(strs[i]+" has:"+single);
			}
			
		}
		System.out.println("total "+"has:"+total);
	}
}