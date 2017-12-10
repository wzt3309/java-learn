package learn.java.jnp.jms.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**   
 *  
 * @author wzt3309 
 */
public class TeacherMessageListener implements MessageListener{
	
	@Override
	public void onMessage(Message msg) {
		TextMessage textMsg = (TextMessage)msg;
		try {
			System.out.println(textMsg.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
