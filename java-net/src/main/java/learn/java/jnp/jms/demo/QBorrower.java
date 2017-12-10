package learn.java.jnp.jms.demo;

import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

/**
 * 贷款申请方
 * @author wzt
 *
 */
public class QBorrower {
	private QueueConnection qConnect;
	private QueueSession qSession;
	private Queue requestQ;
	private Queue responseQ;
	
	public QBorrower(String qFactory, String loanRequestQ, String loanResponseQ) throws Exception {
		Properties env = new Properties();
		env.load(this.getClass().getResourceAsStream("jndi.properties"));
		InitialContext ctx = new InitialContext(env);
		QueueConnectionFactory qConnectionFactory = (QueueConnectionFactory)ctx.lookup(qFactory);
		qConnect = qConnectionFactory.createQueueConnection();
		qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		requestQ = (Queue)ctx.lookup(loanRequestQ);
		responseQ = (Queue)ctx.lookup(loanResponseQ);
		qConnect.start();
	}
	
	public void sendLoanRequest(double salary, double loanAmt) {
		try {
			MapMessage mapMsg = qSession.createMapMessage();
			mapMsg.setDouble("Salary", salary);
			mapMsg.setDouble("LoanAmount", loanAmt);
			mapMsg.setJMSReplyTo(responseQ);
			
			QueueSender qSender = qSession.createSender(requestQ);
			qSender.send(mapMsg);
			
			String filter = "JMSCorrelationID = '"+ mapMsg.getJMSMessageID()+"'";
			QueueReceiver qRecevier = qSession.createReceiver(responseQ, filter);
			TextMessage tMsg = (TextMessage) qRecevier.receive(30000); 
			if(tMsg == null) {
				System.out.println("QLender not responding");
			}else {
				System.out.println("Loan Request was " + tMsg.getText());
			}
		} catch (JMSException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	
	public void exit() {
		try {
			qConnect.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		Thread.currentThread().interrupt();
	}
	public static void main(String[] args) throws Exception {
		String queuecf = null;
		String requestQ = null;
		String responseQ = null;
		if(args != null && args.length == 3) {
			queuecf = args[0];
			requestQ = args[1];
			responseQ = args[2];
		}else {
			System.out.println("Usage java QBorrower <QueueFactory> <RequestQueue> <ResponseQueue>");
			System.exit(0);
		}
		
		QBorrower borrower = new QBorrower(queuecf, requestQ, responseQ);
		Scanner sc = new Scanner(System.in);
		System.out.println ("QBorrower Application Started");
		System.out.println ("Press enter to quit application");
		System.out.println ("Enter: Salary, Loan_Amount");
		System.out.println("\ne.g. 50000, 120000");
		
		while(true) {
			if("exit".equals(sc.next())) {
				sc.close();
				break;
			}
			double salary = sc.nextDouble();
			double loanAmt = sc.nextDouble();
			borrower.sendLoanRequest(salary, loanAmt);
		}
		
		borrower.exit();
	}

}
