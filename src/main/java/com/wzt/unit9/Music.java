package com.wzt.unit9;

import com.wzt.unit8.MusicNote;
public class Music{
	public static void tune(Instrument one){
		one.play(MusicNote.MIDDLE_C);
	}
	public static void tuneAll(Instrument[] all){
		for(Instrument one:all)
			tune(one);
	}
	/**
	 * [main description]
	 * @param args [description]
	 */
	public static void main(String[] args){	
		
		Instrument[] all=new Instrument[]{
			new Wind(),
			new Percussion(),
			new Stringed(),
			new Woodwind(),
			new Brass(),
		};
		tuneAll(all);
	}
}
abstract class Instrument{
	public abstract void play(MusicNote n);
	public void what(){
		System.out.println("This Is Instrument");
	}
	public abstract void adjust();
}
class Wind extends Instrument{
	public void play(MusicNote n){
		System.out.println("Wind play "+n);
	}
	public void what(){
		System.out.println("This Is Wind");
	}
	public void adjust(){
		System.out.println("Wind adjust");
	}
}
class Percussion extends Instrument{
	public void play(MusicNote n){
		System.out.println("Percussion play "+n);
	}
	public void what(){
		System.out.println("This Is Percussion");
	}
	public void adjust(){
		System.out.println("Percussion adjust");
	}
}
class Stringed extends Instrument{
	public void play(MusicNote n){
		System.out.println("Stringed play "+n);
	}
	public void what(){
		System.out.println("This Is Stringed");
	}
	public void adjust(){
		System.out.println("Stringed adjust");
	}
}
class Woodwind extends Wind{
	public void play(MusicNote n){
		System.out.println("Woodwind play "+n);
	}
	public void what(){
		System.out.println("This Is Woodwind");
	}
	
}
class Brass extends Wind{
	public void play(MusicNote n){
		System.out.println("Brass play "+n);
	}
	public void what(){
		System.out.println("This Is Brass");
	}
	
}