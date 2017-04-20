package jxl.wzt.u2nio;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Introduce java.nio.file.Path
 * @author wzt
 *
 */
public class PathItdc {

	public static void main(String[] args) {

		Path path1=Paths.get("/usr/bin/zip");
		System.out.format("FileName %s\n"
				+ "NumOfEleInPath %s\n"
				+ "ParentPath %s\n"
				+ "RootPath %s\n"
				+ "SubpathFromRoot %s\n", 
				path1.getFileName(),path1.getNameCount(),
				path1.getParent(),path1.getRoot(),
				path1.subpath(0, 2));
		
		Path path2=Paths.get("README.md");
		path2=path2.toAbsolutePath();
		System.out.println(path2);
		Path p1Top2=path1.relativize(path2);
		System.out.println(p1Top2);
	}

}
