package com.wzt.unit8;
/**
 * Unit8 Exercise2 and 3
 */
import java.util.*;

public class Exercise3{
	private static Random rand=new Random(47);
	public static Shap generator(){		
		switch (rand.nextInt(3)) {
			default:return new Shap();
			case 0:return new Circle();
			case 1:return new Square();
			case 2:return new Triangle();
		}
	}
	public static void main(String[] args){
		Shap[] s=new Shap[9];
		for(int i=0;i<9;i++){
			s[i]=generator();
			s[i].printMsg();
		}
	}
}
class Shap{
	public void draw(){

	}
	public void erase(){

	}
	public void printMsg(){
		System.out.println("msg from Shap");
	}
}
class Circle extends Shap{
	@Override
	public void draw(){

	}
	@Override
	public void erase(){

	}
	@Override
	public void printMsg(){
		System.out.println("msg from Circle");
	}

}
class Square extends Shap{
	@Override
	public void draw(){

	}
	@Override
	public void erase(){
		
	}

}
class Triangle extends Shap{
	@Override
	public void draw(){

	}
	@Override
	public void erase(){
		
	}

}

