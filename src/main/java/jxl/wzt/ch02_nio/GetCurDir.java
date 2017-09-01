package jxl.wzt.ch02_nio;

import java.io.File;

public class GetCurDir {

	/**
	 * 在shell中加载本类，必须在本类的根目录下：
	 * $cd /home/wzt/workspace/think-in-java/target/classes/
	 * $java jxl.wzt.u2nio.GetCurDir
	 * 此时执行程序的工作目录是/home/wzt/workspace/think-in-java/target/classes/
	 * 因此有关的相对路径就是基于此，相对目录只和执行程序的当前工作目录有关，跟可执行代码放在哪里都没关系
	 * 
	 * 由于在环境变量里加入了当前目录为java查找类的目录，因此需要cd到类所在的目录才可以执行程序
	 * 但如果cd到/home/wzt/workspace/think-in-java/target/classes/jxl/wzt/u2nio呢？
	 * $java jxl.wzt.u2nio.GetCurDir或者$java GetCurDir都是错的，找不到类
	 * 因为package 指定了java类的命名空间，前者执行的时候会去当前目录下找jxl/wzt/u2nio/GetCurDir 自然是找不到
	 * 后者执行的时候是在当前目录下找GetCurDir 但想要找的GetCurDir.class应该要package ；（default包的）
	 * 与当前目录下jxl.wzt.u2nio.GetCurDir不符合，因此也会找不到类，因为找不到default包下的GetCurDir
	 * 这也就说明了不同包，不同命名空间可以有同名类，java不会把它当成同一个
	 * 
	 * 一般在非ide环境下，
	 * cd到源码目录，源码放置的时候可以不按package 指定的文件夹放置，全部放在一起（当前目录）都行	 
	 * 编译.java的时候，会根据package 自动生成文件夹，将.class放到文件夹下，然后直接在
	 * 执行javac编译的目录下，执行程序java xx.xx.xx 就可以了
	 * cd workspace
	 * ls 
	 * A.java(package com.wzt.a;) B.java(package com.wzt.b;)
	 * javac A.java B.java
	 * ls
	 * A.java B.java com/wzt/a/A.class com/wzt/b/B.class 
	 * java com.wzt.a.A (程序中用到相对目录时，都是相对于当前工作目录)
	 * getClass().getResource("/") 获取当前class所在的根目录，
	 * 就是当前.class文件所在返回package指示路径所到的目录
	 * getClass().getResource("."或""或"./")都是当前.class文件所在的目录
	 * getClass().getResource("../").class文件上一级目录
	 * getClass().getResource("../../").class文件上上一级目录，直到最后根目录
	 * 
	 * 总结：
	 * 1、在ide中使用相对目录，其根目录为项目所在的目录，因为在ide中运行程序时，当期工作目录为项目目录
	 * 其实际执行时所用的命令是
	 * java -classpath target/classes jxl.wzt.u2nio.GetCurDir
	 * 2、程序执行是的相对路径有的时候是不可靠的，因此需要用到getClass().getResource()等方法来获取绝对路径信息等
	 * 3、不要依靠ide中可用的相对路径来编写代码，这种代码往往不可靠的
	 * @param args
	 */
	public static void main(String[] args) {
		//不管在ide里还是shell里都是输出同样的值
		//用此可以明确、完整控制路径信息，不用为相对路径使用成不成功更当前工作目录有关的问题烦恼了
		System.out.println(GetCurDir.class.getResource(""));
		
		//只有在ide环境下才可以使用，其实能不能用根程序运行的当前工作目录有关
		File file=new File("README.md");
		if(file.exists()){
			System.out.println("1."+file.getAbsolutePath());
		}
		//这个在非ide环境（命令行环境下才有用），其实能不能用根程序运行的当前工作目录有关
		//使用target/classes/tij/wzt/help/Stack.class ide中才行
		file=new File("tij/wzt/help/Stack.class");
		if(file.exists()){
			System.out.println("2."+file.getAbsolutePath());
		}
	}

}
