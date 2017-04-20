package jxl.wzt.u2nio;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.attribute.PosixFilePermission.GROUP_READ;
import static java.nio.file.attribute.PosixFilePermission.OTHERS_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class FileOperator {

	public static void main(String[] args){
//		createFile(".test");
//		deleteFile(".test");
//		
//		createFile("test");
//		copyFile("test",".test");
//		readFileAttr(".test");
//		posixFileAttr(".test");
		
		readSymbolicLink("szip");
	}
	
	
	public static void createFile(String path){
		Path target=Paths.get(path);
		Set<PosixFilePermission> perms=PosixFilePermissions.fromString("rwxrwxr-x");
		FileAttribute<Set<PosixFilePermission>> attr=PosixFilePermissions.asFileAttribute(perms);
		try {
			Files.createFile(target, attr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFile(String path){
		Path target=Paths.get(path);
		try {
			Files.delete(target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFile(String src,String dest){
		Path srcPath=Paths.get(src);
		Path destPath=Paths.get(dest);
		try {
			Files.copy(srcPath,destPath,REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void moveFile(String src,String dest){
		//同copyFile
	}
	
	public static void readFileAttr(String path){
		Path target =Paths.get(path);
		try {
			System.out.println(Files.getLastModifiedTime(target));
			System.out.println(Files.size(target));
			System.out.println(Files.isSymbolicLink(target));
			System.out.println(Files.isDirectory(target));
			System.out.println(Files.readAttributes(target, "*"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void posixFileAttr(String path){
		Path target=Paths.get(path);
		try{
			PosixFileAttributes attr=Files.readAttributes(target, PosixFileAttributes.class);
			Set<PosixFilePermission> posixPermissions=attr.permissions();
			posixPermissions.clear();
			String owner=attr.owner().getName();
			String perms=PosixFilePermissions.toString(posixPermissions);
			System.out.format("%s %s \n", owner,perms);
			
			posixPermissions.add(OWNER_READ);
			posixPermissions.add(GROUP_READ);
			posixPermissions.add(OTHERS_READ);
			posixPermissions.add(OWNER_WRITE);
			
			Files.setPosixFilePermissions(target, posixPermissions);
			perms=PosixFilePermissions.toString(posixPermissions);
			System.out.format("%s %s \n", owner,perms);
		}catch(IOException e){
			
		}
	}
	
	public static void readSymbolicLink(String path){
		Path target=Paths.get(path);
		BasicFileAttributes attr=null;
		if(Files.isSymbolicLink(target)){
			System.out.println(target.toAbsolutePath()+"is symbolicLink");			
			try {
				//默认跟随符号链接，访问实际文件的attr
				attr=Files.readAttributes(target, BasicFileAttributes.class);
				System.out.println("ordinary read:"+attr.creationTime());
				//指定LinkOption.NOFOLLOW_LINKS不跟随符号链接，访问符号文件本身的attr
				attr=Files.readAttributes(target, BasicFileAttributes.class,LinkOption.NOFOLLOW_LINKS);
				System.out.println("LinkOption.NOFLLOW_LINKS read:"+attr.creationTime());
				//跟随符号链接，访问实际文件的attr
				target=Files.readSymbolicLink(target);
				attr=Files.readAttributes(target, BasicFileAttributes.class);
				System.out.println("readSymbolicLin:"+attr.creationTime());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
