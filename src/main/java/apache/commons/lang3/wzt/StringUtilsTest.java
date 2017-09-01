package apache.commons.lang3.wzt;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;


public class StringUtilsTest {
	
	@Test
	public void testStrBlank() {
		System.out.println(StringUtils.isBlank(" \r\n"));
		System.out.println(StringUtils.isAnyBlank(" ", "bar"));
	}
}
