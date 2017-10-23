package jxl.wzt.ch03_di;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**   
 *  
 * @author wzt3309 
 */
public class HollywoodServiceClient {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AgentFinderModule());
		HollywoodServiceGuice hollywoodServiceGuice = 
				injector.getInstance(HollywoodServiceGuice.class);
		List<Agent> agents = hollywoodServiceGuice.getFriendlyAgents();	
		pAgents(agents);
		
	}
	
	public static void pAgents(List<Agent> agents){
		System.out.printf("%8s %15s\n", "agent_id","agent_type");
		System.out.printf("%8s %15s\n", "--------","--------------");
		if(agents != null && agents.size() > 0) {
			for(Agent agent : agents){
				if(agent != null) {
					System.out.printf("%8d %15s\n",agent.getId(),agent.getType());
				}
				
			}
		}
	}

}
