package learn.java.jnp.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleServer 只向client发送时间数据
 * @author wzt
 *
 */
public class MySimpleServer {
	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(MyConstants.SIMPLE_CONNECTION_PORT)) {
			while(true) {				
				try(Socket conn = server.accept()) {			
					OutputStream out = conn.getOutputStream();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "ASCII"));
					Date nowDate = new Date(System.currentTimeMillis());
					SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
					writer.write("Date: ");
					writer.write(dateFmt.format(nowDate));
					writer.write("\r\n");
					writer.flush();									
				}catch (IOException e) {
					System.out.println("Connection to Request IOException");
				}
			}
		}catch (IOException e) {
			System.out.println("Server Socket IOException");
		}
	}
}
