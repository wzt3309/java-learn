package jnp.wzt.jms.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

/**   
 *  
 * @author wzt3309 
 */
public class Chat implements MessageListener{
	private static final URL BASE_PATH = Chat.class.getClassLoader().getResource("");
	private URL JNDI_PROPERTIES;
	private TopicSession pubSession;
	private TopicPublisher publisher;
	private TopicConnection connection;
	private String username;
	
	public Chat(String topicFactory, String topicName, String username) 
			throws Exception {
		//使用jndi.properties 文件获取JNDI连接
		Properties env = new Properties();
		JNDI_PROPERTIES = new URL(BASE_PATH, "jnp/wzt/jms/jndi.properties");
		env.load(JNDI_PROPERTIES.openStream());
		InitialContext ctx = new InitialContext(env);
		
		//查找一个JMS连接工厂，并创建连接
		TopicConnectionFactory conFactory = (TopicConnectionFactory)ctx.lookup(topicFactory);
		TopicConnection connection = conFactory.createTopicConnection();
		
		//创建两个JMS会话对象
		TopicSession pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicSession subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//查找一个JMS主题
		Topic chatTopic = (Topic)ctx.lookup(topicName);
		
		TopicPublisher publisher = pubSession.createPublisher(chatTopic);
		TopicSubscriber subscriber = subSession.createSubscriber(chatTopic, null, true);
		
		//设置JMS消息监听器
		subscriber.setMessageListener(this);
		
		//初始化Chat应用程序变量
		this.connection = connection;
		this.pubSession = pubSession;
		this.publisher = publisher;
		this.username = username;
		
		connection.start();
		
		
	}
	
	/**
	 * 接受来自TopicSubscriber的消息 
	 * @param message
	 * @author wzt3309
	 */
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage)message;
		try {
			System.out.println(textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 使用发布者创建并发布消息
	 * @param text
	 * @throws JMSException
	 * @author wzt3309
	 */
	protected void writeMessage(String text) throws JMSException {
		TextMessage message = pubSession.createTextMessage();
		message.setText(username + ": " + text);
		publisher.publish(message);
	}
	
	public void close() throws JMSException {
		connection.close();
	}
	
	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Usage: Factory,Topic,username");
		}
		Chat chat =  null;
		try {
			chat = new Chat(args[0], args[1], args[2]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("App Chat init fail");
		}
		if(chat != null) {
			try(BufferedReader bReader = 
					new BufferedReader(new InputStreamReader(System.in))) {
				String line = null;
				while((line = bReader.readLine()) != null) {
					if(line.equalsIgnoreCase("exit")) {
						chat.close();
						System.exit(0);
					}else {
						chat.writeMessage(line);
					}
				}
			}catch (IOException e) {
				System.out.println("IOException occure");
			}catch (JMSException e) {
				System.out.println("chat close fail");
			}			
		}
	}
}
