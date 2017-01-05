package jxl.wzt.u2nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DirAndDirTree {

	public static void main(String[] args){
		findAll1("/home/wzt/workspace/think-in-java/"
				+ "src/main/java/jxl/wzt/u2nio","*.java");
		findAll2("/home/wzt/workspace/think-in-java/"
				+ "src/main/java","*.java");
	}
	/**
	 * 找到指定目录下的文件
	 * @param path
	 * @param file
	 */
	public static void findAll1(String path,String file){
		System.out.println("findAll "+file+" in "+path);
		try(DirectoryStream<Path> stream=Files.newDirectoryStream(Paths.get(path),file)){
			for(Path entry:stream){
				System.out.println("-"+entry.getFileName()+" ");
			}			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}		
	}
	/**
	 * 便利目录寻找文件
	 * @param path
	 * @param file
	 */
	public static void findAll2(String path,String file){
		Path startDir=Paths.get(path);
		System.out.println("findAll "+file+" in "+path);
		try {
			Files.walkFileTree(startDir, new FindJavaVisitor());			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	static class FindJavaVisitor extends SimpleFileVisitor<Path>{
		
		public FileVisitResult visitFile(Path file,BasicFileAttributes attrs){
			if(file.toString().endsWith(".java"))
				System.out.println("-"+file.getFileName()+" ");
			return FileVisitResult.CONTINUE;
		}
	}
}
