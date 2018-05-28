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
	private boolean filterSet = false;

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
			else {
				consumer = context.createConsumer(queue, "ID <> '" + gameController.getName() + "'");
				filterSet = true;
			}
			consumer.setMessageListener(this);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		try {
			String textMessage = message.getStringProperty("msg");
			System.out.printf("Odebrano wiadomość:'%s' \n", textMessage);
			String oponentName = textMessage.substring(0, 1);
			int x = Integer.parseInt(textMessage.substring(1, 2));
			int y = Integer.parseInt(textMessage.substring(2, 3));
			gameController.oponentMove(oponentName, x, y);
		} catch (Exception e) {
			System.out.println("Wyjatek z onMessage Consumenta");
			e.printStackTrace();
		}
	}

	public JMSConsumer getJMSConsumer() {
		return consumer;
	}

	public JMSContext getJMSContext() {
		return context;
	}
	
	public boolean filterIsSet() {
		return filterSet;
	}

}
