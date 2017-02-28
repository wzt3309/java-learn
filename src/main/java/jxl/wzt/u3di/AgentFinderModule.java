package jxl.wzt.u3di;

import com.google.inject.AbstractModule;

/**   
 *  创建声明绑定关系的模块
 * @author wzt3309 
 */
public class AgentFinderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AgentFinder.class).to(WebServiceAgentFinder.class);
	}

}
