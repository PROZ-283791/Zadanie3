package api;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;

public class Producer {
	
	public void sendQueueMessages(String name, int x, int y) {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		if (name == null)
			return;
		try {
			// [hostName][:portNumber][/serviceName]
			// 7676 numer portu, na którym JMS Service nasłuchuje połączeń
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			JMSContext jmsContext = connectionFactory.createContext();
			JMSProducer jmsProducer = jmsContext.createProducer();
			Queue queue = new com.sun.messaging.Queue("Queue");
			String msg = name+x+y;
			jmsProducer.send(queue, msg);
			jmsContext.close();
			System.out.printf("Wiadomość '%s' została wysłana.\n", msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
