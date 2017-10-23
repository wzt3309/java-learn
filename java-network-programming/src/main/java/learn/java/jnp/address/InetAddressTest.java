package learn.java.jnp.address;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class InetAddressTest {
	private InetAddress address;
	
	@Before
	public void setUp() {
		address = null;
	}
	@After
	public void setDown() {
		address = null;
	}
	@Test
	public void InetAddressByName() {		
		try {
			address = InetAddress.getByName("www.oreilly.com");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(address);
		
		try {
			address = InetAddress.getByName("222.201.145.145");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(address.getCanonicalHostName());
	}
	@Test
	public void InetAddressByByte() {
		byte[] ip = {(byte)222, (byte)201, (byte) 145, (byte) 145};
		System.out.println(ip[2] + "," + (ip[2] & 0xff));
		try {
			address = InetAddress.getByAddress(ip);
			if (address.isReachable(1000)) {
				System.out.println("reachable");
			}else {
				System.out.println("unreachable");
			}
//			address = InetAddress.getByAddress("maybewrong", ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		System.out.println(address);
 	}
}