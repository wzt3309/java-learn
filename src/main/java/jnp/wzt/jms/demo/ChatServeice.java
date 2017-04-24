package jnp.wzt.jms.demo;

import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/**   
 *  
 * @author wzt3309 
 */
public class ChatServeice {
	private static final String PROPERTIES_PATH = "jndi.properties";
	private static final String FACTORY = "TopicCF";
	private static InitialContext ctx;
	private static TopicConnectionFactory factory;
	
	private TopicConnection connection;
	private TopicSession session;
	static {
		Properties env = new Properties();		
		try {
			env.load(ChatServeice.class.getResourceAsStream(PROPERTIES_PATH));
			ctx = new InitialContext(env);
			factory = lookupTopicConnectionFactory(FACTORY);
		} catch (IOException | NamingException e) {
			e.printStackTrace();
		}
		
	}
	public void joinTopicAsSubscriber(String topic, MessageListener listener) {		
		try {			
			TopicSubscriber subscriber = 
					session.createSubscriber((Topic)ctx.lookup(topic), null, false);
			subscriber.setMessageListener(listener);
		} catch (JMSException | NamingException e) {
			e.printStackTrace();
		}
	}
	
	public TopicPublisher joinTopicAsPublisher(String topic) {		
		TopicPublisher publisher = null;
		try {			
			publisher = session.createPublisher((Topic)ctx.lookup(topic));
		} catch (JMSException | NamingException e) {
			e.printStackTrace();
		}
		
		return publisher;
	}
	public void start() throws JMSException {
		connection.start();
	}
	public void createDefaultTopicSession() 
			throws JMSException {
		createDefaultTopicConnection();
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	public TopicSession getDefaultTopicSession() throws JMSException {
		createDefaultTopicSession();
		return session;
	}
	
	public void createDefaultTopicConnection() 
			throws JMSException {
		if(connection == null) {
			connection = factory.createTopicConnection();
		}
	}
	
	public TopicConnection getDefaultTopicConnection() throws JMSException {
		createDefaultTopicConnection();
		return connection;
	}
	
	public static TopicConnectionFactory lookupTopicConnectionFactory(String factoryName) 
			throws NamingException {
		return (TopicConnectionFactory) ctx.lookup(factoryName);
	}	
}
