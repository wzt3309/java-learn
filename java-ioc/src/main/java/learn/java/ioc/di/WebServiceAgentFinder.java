package jxl.wzt.ch03_di;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**   
 *  
 * @author wzt3309 
 */
public class WebServiceAgentFinder implements AgentFinder {
	private final AgentType[] agentTypes = AgentType.values();
	private final Random random = new Random(25);
	
	@Override
	public List<Agent> findAllAgents() {
		int listSize = 10;
		List<Agent> agents = new ArrayList<>(10);
		for(int i = 0;i < listSize;i++) {
			agents.add(createAgent());
		}
		return agents;
	}
	
	private Agent createAgent() {		
		int randomNum = random.nextInt(agentTypes.length);
		Agent agent = new Agent(agentTypes[randomNum].getDescription());
		return agent;
	}

}
