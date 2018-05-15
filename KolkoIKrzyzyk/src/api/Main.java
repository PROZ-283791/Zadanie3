package api;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;

public class Main extends Application {
	WindowController winController;
	GameController gamController;
	JMSConsumer consumer;
	JMSContext context;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/api/Window.fxml"));
			GridPane root = fxmlLoader.load();
			winController = fxmlLoader.getController();
			gamController = new GameController(winController, primaryStage);
			gamController.setProducer();
			winController.setGameController(gamController);
			receiveQueueMessagesAsynch();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Kółko i Krzyżyk");
			primaryStage.show();
			primaryStage.setOnCloseRequest(x -> {primaryStage.close(); finish();});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveQueueMessagesAsynch() {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		JMSContext jmsContext = connectionFactory.createContext();
		context = jmsContext;
		try {
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			Queue queue = new com.sun.messaging.Queue("Queue");
			JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
			consumer = jmsConsumer;
			jmsConsumer.setMessageListener(new Consumer(gamController));

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void finish() {
		context.close();
		if (consumer != null)
			consumer.close();
	}

}
