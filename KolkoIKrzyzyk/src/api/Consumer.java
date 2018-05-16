package api;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Consumer implements MessageListener {
	GameController con;

	public Consumer(GameController con) {
		this.con = con;
	}

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.printf("Odebrano wiadomość:'%s' \n", textMessage.getText());
			int x = Integer.parseInt(textMessage.getText().substring(1, 2));
			int y = Integer.parseInt(textMessage.getText().substring(2, 3));
			if (textMessage.getText().equals(con.getJustSend()))
				con.getProducer().sendQueueMessages(textMessage.getText().substring(0, 1), x, y);
			else
				con.oponentMove(x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
