package learn.java.jnp.demo;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

/**   
 *  
 * @author wzt3309 
 */
public class Teacher {
	private static final String TOPIC = "topic1"; 
	private String name;
	private ChatServeice serveice;
	private TopicSession session;
	private TopicPublisher publish;
	
	public Teacher(String name) throws Exception {
		this.name = name;
		this.serveice = new ChatServeice();
		this.session = serveice.getDefaultTopicSession();
		this.publish = serveice.joinTopicAsPublisher(TOPIC);
		serveice.start();
	}
	
	public void writeMsg(String msg) {
		try {
			TextMessage message = session.createTextMessage();
			message.setText(name + ": " + msg);
			publish.publish(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) throws Exception {
		Teacher teacher = new Teacher("wzt");
		Scanner sc = new Scanner(System.in);
		for(String line = sc.nextLine(); 
				line != null || !"exit".equals(line);
				line = sc.nextLine()) {
			teacher.writeMsg(line);
		}
		sc.close();
		
	}
}
