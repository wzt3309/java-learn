package com.wzt.unit14;

public abstract class Shapes{	

}

abstract class Shape{
	public void draw(){
		System.out.println(this+" draw()");
	}
	public abstract String toString();
}
class Circle extends Shape{
	public String toString(){
		return "Circle";
	}
}
class Square extends Shape{
	public String toString(){
		return "Square";
	}
}
class Triangle extends Shape{
	public String toString(){
		return "Triangle";
	}
}
