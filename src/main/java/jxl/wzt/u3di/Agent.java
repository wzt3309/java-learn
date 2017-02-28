package jxl.wzt.u3di;
/**   
 *  
 * @author wzt3309 
 */
public class Agent {
	private static int count = 0;
	private int id = ++count;
	private String type;
	
	public Agent(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
