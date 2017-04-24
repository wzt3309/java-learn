package jnp.wzt.inetaddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.junit.After;
import org.junit.Test;

public class URLTest {
	private URL url;
	
	@After
	public void setDown() {
		url = null;
	}
	
//	@Test
	public void getResByURL() {
		try {
			url = new URL("file:/home/wzt/Documents/wizetmp/java网络编程——Internet地址.md");
			try(InputStream in = url.openStream()) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in, "utf-8"));
				for(String line = reader.readLine();
						line != null;line = reader.readLine()) {
					System.out.println(line);
				}
			}catch (IOException e) {
				System.out.println("IO Exception");
			}
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
		}	
	}
	
	@Test
	public void URLEncoderAndDecoder() {
		try {
			System.out.println(URLEncoder.encode("中国人", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			System.out.println("URL encoding Exception");
		}
	}
}
