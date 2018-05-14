package api;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Consumer implements MessageListener {
	WindowController con;

	public Consumer(WindowController con) {
		this.con = con;
		System.out.println(con);
	}

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.printf("Odebrano wiadomość:'%s' \n", textMessage.getText());
			int x = Integer.parseInt(textMessage.getText().substring(1, 2));
			int y = Integer.parseInt(textMessage.getText().substring(2, 3));
			System.out.println(textMessage.getText() + con.getJustSend() + textMessage.getText() == con.getJustSend());
			System.out.println(textMessage.getText() + con.getJustSend() + textMessage.getText().equals(con.getJustSend()));
			if (textMessage.getText().equals(con.getJustSend()))
				con.getProducer().sendQueueMessages(textMessage.getText().substring(0, 1), x, y);
			else
				con.drawOponent(x, y);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
