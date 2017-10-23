package learn.java.nio.path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * 快速读写文件
 * @author wzt
 *
 */
public class FastRdWr {

	public static void main(String[] args) {
		/**
		 * 与老IO进行交互方式的读写
		 */
		try(BufferedReader br = 
				Files.newBufferedReader(
						Paths.get("data1"),
						StandardCharsets.UTF_8))
		{
			String line;
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		try(BufferedWriter bw = 
				Files.newBufferedWriter(
						Paths.get("data2"),
						StandardCharsets.UTF_8,
						StandardOpenOption.APPEND))
		{
			bw.write("hello world");
		}catch(IOException e){
			e.printStackTrace();
		}
		
		/**
		 * 新io快速读写
		 */
		try {
			List<String> lines = 
					Files.readAllLines(Paths.get("data2"),
									StandardCharsets.UTF_8);
			byte[] bytes = 
					Files.readAllBytes(Paths.get("data2"));
			System.out.println(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
