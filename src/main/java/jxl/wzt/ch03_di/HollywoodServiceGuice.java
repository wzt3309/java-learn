package jxl.wzt.ch03_di;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

/**   
 * 使用Guice进行依赖注入 
 * @author wzt3309 
 */
public class HollywoodServiceGuice {
	private AgentFinder finder;
	
	/**
	 * 使用Guice进行AgentFinder对象的依赖注入
	 * @param finder
	 * @author wzt3309
	 */
	@Inject
	public HollywoodServiceGuice(AgentFinder finder) {
		this.finder = finder;
	}
	
	public List<Agent> getFriendlyAgents() {
		List<Agent> allAgents = finder.findAllAgents();
		List<Agent> friendlyAgents = filterAgents(allAgents,AgentType.JAVA_DEVELOPER);
		return friendlyAgents;
	}
	
	public List<Agent> filterAgents(List<Agent> agents, AgentType type) {
		if(agents != null && agents.size() > 0) {
			List<Agent> filterAgents = new ArrayList<>();
			for(Agent agent : agents) {
				String agentType = null;
				if(agent != null){
					agentType = agent.getType();
				}
				if(type != null){
					if(type.getDescription().equals(agentType)){
						filterAgents.add(agent);
					}
				}
			}
			return filterAgents;
		}
		return null;
	}
}
