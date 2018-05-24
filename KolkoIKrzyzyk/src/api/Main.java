package api;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
	private WindowController winController;
	private GameController gameController;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/api/Window.fxml"));
			GridPane root = fxmlLoader.load();
			winController = fxmlLoader.getController();
			gameController = new GameController(winController, primaryStage);
			gameController.setProducer();
			winController.setGameController(gameController);
			Consumer cons = new Consumer(gameController);
			gameController.setConsumer(cons);
			cons.receiveQueueMessagesAsynch();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Kółko i Krzyżyk");
			primaryStage.setOnCloseRequest(x -> {primaryStage.close(); cons.getJMSConsumer().close();cons.getJMSContext().close();});
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
