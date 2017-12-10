package learn.java.jnp;
/**   
 *  
 * @author wzt3309 
 */
public class CompareBit {

	public static void main(String[] args) {
		int flag = 0;
		for(int i = 0;i < 4;i++) {
			System.out.println(Integer.toBinaryString((flag = flag | 1 << i)));
		}
	}

}
