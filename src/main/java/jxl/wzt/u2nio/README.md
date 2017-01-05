##来源《Java程序员修炼之道》

>简介：java7 nio2.0的特性，提供对异步io、目录遍历、文件系统非阻塞操作 老io见tij.wzt.u18io

###IO基石Path

* java编译运行、及代码中路径相关问题

>详情见jxl.wzt.u2nio.GetCurDir.java

	  在shell中加载本类，必须在本类的根目录下：
	  $cd /home/wzt/workspace/think-in-java/target/classes/
	  $java jxl.wzt.u2nio.GetCurDir
	  此时执行程序的工作目录是/home/wzt/workspace/think-in-java/target/classes/
	  因此有关的相对路径就是基于此，相对目录只和执行程序的当前工作目录有关，跟可执行代码放在哪里都没关系
	  
	  由于在环境变量里加入了当前目录为java查找类的目录，因此需要cd到类所在的目录才可以执行程序
	  但如果cd到/home/wzt/workspace/think-in-java/target/classes/jxl/wzt/u2nio呢？
	  $java jxl.wzt.u2nio.GetCurDir或者$java GetCurDir都是错的，找不到类
	  因为package 指定了java类的命名空间，前者执行的时候会去当前目录下找jxl/wzt/u2nio/GetCurDir 自然是找不到
	  后者执行的时候是在当前目录下找GetCurDir 但想要找的GetCurDir.class应该要package ；（default包的）
	  与当前目录下jxl.wzt.u2nio.GetCurDir不符合，因此也会找不到类，因为找不到default包下的GetCurDir
	  这也就说明了不同包，不同命名空间可以有同名类，java不会把它当成同一个
	  
	  一般在非ide环境下，
	  cd到源码目录，源码放置的时候可以不按package 指定的文件夹放置，全部放在一起（当前目录）都行	 
	  编译.java的时候，会根据package 自动生成文件夹，将.class放到文件夹下，然后直接在
	  执行javac编译的目录下，执行程序java xx.xx.xx 就可以了
	  cd workspace
	  ls 
	  A.java(package com.wzt.a;) B.java(package com.wzt.b;)
	  javac A.java B.java
	  ls
	  A.java B.java com/wzt/a/A.class com/wzt/b/B.class 
	  java com.wzt.a.A (程序中用到相对目录时，都是相对于当前工作目录)
	  getClass().getResource("/") 获取当前class所在的根目录，
	  就是当前.class文件所在返回package指示路径所到的目录
	  getClass().getResource("."或""或"./")都是当前.class文件所在的目录
	  getClass().getResource("../").class文件上一级目录
	  getClass().getResource("../../").class文件上上一级目录，直到最后根目录
	  
	  总结：
	  1、在ide中使用相对目录，其根目录为项目所在的目录，因为在ide中运行程序时，当期工作目录为项目目录
	  其实际执行时所用的命令是
	  java -classpath target/classes jxl.wzt.u2nio.GetCurDir
	  2、程序执行是的相对路径有的时候是不可靠的，因此需要用到getClass().getResource()等方法来获取绝对路径信息等
	  但其获得字符串会有file: 字样，实际使用时需要注意
	  3、不要依靠ide中可用的相对路径来编写代码，这种代码往往不可靠的
	  
* java.nio.file.Path 是抽象表示文件系统的位置

>可用来替代java.io.File类，比其更强大，相互之间可以用`toPath()`,`toFile()`来转换

>Path可以获取根目录、父目录、两个Path间的相对路径、本Path的绝对路径、`toString()`输出路径str、路径拼接等等

###处理目录和目录树

* +`DirectoryStream<T>` 过滤文件流，获取想要的文件在文件系统的位置Path

* +`Files.walkFileTree(Path,FileVisitor<? super Path>)` 遍历目录树

>`FileVisitor<? super Path>`是一个遍历目录时要做某些操作的接口，`SimpleFileVisitor<T>` 是一个其的简单实现

###文件属性与文件操作

* +Files工具类对文件基本操作、文件属性操作等进行了完整封装，只要和文件相关的都可以用到Files

>*`Files.readAttributes(Path,String/Class[type],options):type 返回值类型为type指定`	//读文件属性

> 包括特殊文件系统POSIX的属性`BasicFileAttributes`基本属性	`PosixFileAttributes`特定posix系统属性	

>*`Files.createFile(Path,attr):Path`

>*`Files.copy(Path,Path,options...):Path` options `StandardCopyOption.REPLACE_EXISTING ATOMIC_MOVE COPY_ATTRIBUTES`

>*`Files.move(Path,Path,options...):Path`

>*`PosixFilePermission` enum类，unix操作系统文件权限

>*`PosixFilePermissions` 权限工具类

>*`PosixFileAttributes` unix操作系统文件属性	



