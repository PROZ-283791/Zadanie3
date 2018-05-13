package api;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/api/Window.fxml"));
			GridPane root = fxmlLoader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Kółko i Krzyżyk");
			primaryStage.show();
			primaryStage.setOnCloseRequest(x -> primaryStage.close());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
