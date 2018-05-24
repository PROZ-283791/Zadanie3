package api;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

public class Producer {
	
	public void sendQueueMessages(String name, int x, int y) {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		if (name == null)
			return;
		try {
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			JMSContext jmsContext = connectionFactory.createContext();
			JMSProducer jmsProducer = jmsContext.createProducer();
			Queue queue = new com.sun.messaging.Queue("Queue");
			Message msg = jmsContext.createTextMessage();
			msg.setStringProperty("msg", name+x+y);
			msg.setStringProperty("ID", name);
			jmsProducer.send(queue, msg);
			jmsContext.close();
			System.out.printf("Wiadomość '%s' została wysłana.\n", msg.getStringProperty("msg"));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
