package com.wzt.unit11;
import com.wzt.help.Stack;
import java.util.*;
/**
 * unit11 exercise15
 */
public class Exercise15{
	public static void main(String[] args){
		String express="+U+n+c---+e+r+t---+a-+i-+n+t+y---+ -+r+u--+l+e+s---";
		char[] chars=express.toCharArray();
		Stack<Character> stack=new Stack<Character>();
		for(int i=0;i<chars.length;i++){
			if(chars[i]=='+')
				stack.push(chars[i+1]);
			if(chars[i]=='-'){
				System.out.print(stack.pop());
			}
		}
	}
}