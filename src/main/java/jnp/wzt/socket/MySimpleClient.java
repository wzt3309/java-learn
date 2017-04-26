package jnp.wzt.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * SimpleClient 只接受来自服务器的数据
 * @author wzt
 *
 */
public class MySimpleClient {
	public static void main(String[] args) {
		try(Socket client = new Socket(InetAddress.getLocalHost(),
				MyConstants.SIMPLE_CONNECTION_PORT)) {
			System.out.println(getResponse(client, "ASCII"));
		}catch (UnknownHostException e) {
			System.out.println("unknown host");
		}catch (IOException e) {
			System.out.println("socket connection fail");
		}	
	}
	
	public static String getResponse(Socket conn, String charset) {
		try(InputStream in = conn.getInputStream()) {
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(in, charset));
			StringBuilder builder = new StringBuilder();
			for(String line = bufReader.readLine();
					line != null;line = bufReader.readLine()) {
				builder.append(line);
			}
			return builder.toString();
		}catch (IOException e) {
			System.out.println("Get Response IOException");
		}
		return null;
	}
	
	public static void sendRequest(Socket conn, String request, String charset) {
		try(OutputStream out = conn.getOutputStream()) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, charset));
			writer.write(request);
			writer.flush();
		}catch (IOException e) {
			System.out.println("Send Request IOException");
		}
	}
}
