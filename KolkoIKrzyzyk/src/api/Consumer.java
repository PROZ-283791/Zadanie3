package api;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;

public class Consumer implements MessageListener {
	private GameController gameController;
	private JMSContext context;
	private JMSConsumer consumer;
	
	public Consumer(GameController con) {
		this.gameController = con;
	}
	
	void receiveQueueMessagesAsynch() {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		if (context == null)
			context = connectionFactory.createContext();
		try {
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			Queue queue = new com.sun.messaging.Queue("Queue");
			if (gameController.getName() == null)
				consumer = context.createConsumer(queue);
			else
				consumer = context.createConsumer(queue,"ID <> '"+gameController.getName()+"'");
			consumer.setMessageListener(this);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			String textMessage = message.getStringProperty("msg");
			if (textMessage.startsWith("init"))
				return;
			System.out.printf("Odebrano wiadomość:'%s' \n", textMessage);
			int x = Integer.parseInt(textMessage.substring(1, 2));
			int y = Integer.parseInt(textMessage.substring(2, 3));
			gameController.oponentMove(x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JMSConsumer getJMSConsumer () {
		return consumer;
	}
	
	public JMSContext getJMSContext () {
		return context;
	}

}
