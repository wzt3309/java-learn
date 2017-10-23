package learn.java.jnp.demo;

import java.util.Scanner;

import javax.jms.JMSException;

/**   
 *  
 * @author wzt3309 
 */
public class Student {
	private static final String TOPIC = "topic1"; 
	public Student() throws JMSException {
		ChatServeice serveice = new ChatServeice();
		serveice.createDefaultTopicSession();
		serveice.joinTopicAsSubscriber(TOPIC, 
				new TeacherMessageListener());
		serveice.start();
	}
	public static void main(String[] args) throws JMSException {
		System.out.println("Student get from teacher: ");
		new Student();
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		while(true) {
			if(line == null || "exit".equals(line)) {
				break;
			}
			line = sc.nextLine();
		}
		sc.close();
	}
}
