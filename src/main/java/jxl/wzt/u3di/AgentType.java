package jxl.wzt.u3di;
/**   
 *  
 * @author wzt3309 
 */
public enum AgentType {	
	
	JAVA_DEVELOPER("java developer"),
	CPP_DEVELOPER("cpp developer"),
	PYTHON_DEVELOPER("pythond developer");
	
	private String description;
	
	private AgentType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
}
