package learn.java.jnp.demo;

import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class QLender implements MessageListener{
	private QueueConnection qConnection;
	private QueueSession session;
	
	public QLender(String queuecf, String requestQ) throws Exception {
		Properties env = new Properties();
		env.load(this.getClass().getResourceAsStream("jndi.properties"));
		
		InitialContext ctx = new InitialContext(env);
		QueueConnectionFactory qConnectionFactory = (QueueConnectionFactory)ctx.lookup(queuecf);
		
		qConnection = qConnectionFactory.createQueueConnection();
		session = qConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		session.createReceiver((Queue)ctx.lookup(requestQ)).setMessageListener(this);
		
		qConnection.start();
		System.out.println("Waiting for loan requests...");
	}
	
	@Override
	public void onMessage(Message arg0) {
		MapMessage mMsg = (MapMessage)arg0;
		boolean accepted = false;
		try {
			double salary = mMsg.getDouble("Salary");
			double loanAmt = mMsg.getDouble("LoanAmount");
			
			if(loanAmt < 200000) {
				accepted = (salary / loanAmt) > .25;
			}else {
				accepted = (salary / loanAmt) > .33;
			}
			
			System.out.println(
					String.format("%f%%, loan is %s", 
									salary / loanAmt, 
									accepted ? "Accepted":"Declined")
			);
			
			TextMessage tmsg = session.createTextMessage();
			tmsg.setText(
					(accepted ? "Accepted" : "Declined")
			);
			tmsg.setJMSCorrelationID(mMsg.getJMSMessageID());
			
			QueueSender qSender = session.createSender((Queue)mMsg.getJMSReplyTo());
			qSender.send(tmsg);
			
		} catch (JMSException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}		
	}
	
	public void exit() {
		try {
			qConnection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
			Thread.currentThread().interrupt();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String queuecf =null;
		String requestQ = null;
		if(args.length == 2) {
			queuecf = args[0];
			requestQ = args[1];
		}
		
		Scanner sc = new Scanner(System.in);
		QLender qLender = new QLender(queuecf, requestQ);
		while(true) {
			System.out.println("Please input \'Enter\' to exit");
			sc.nextLine();
			break;
		}
		
		qLender.exit();
	}

}
