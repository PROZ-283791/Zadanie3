package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class GameController {
	private ArrayList<String> buffer = new ArrayList<String>(9);
	private WindowController windowController;
	private Stage primaryStage;
	private Producer producer;
	private Consumer consumer;
	private String name = null;
	private String justSend;
	private boolean myTurn = true;
	private int moves = 0;
	private int playedGames = 0;

	public GameController() {
		for (int i = 0; i < 9; ++i)
			buffer.add("");
	}

	public GameController(WindowController windowController, Stage primaryStage) {
		this.windowController = windowController;
		this.primaryStage = primaryStage;
		for (int i = 0; i < 9; ++i)
			buffer.add("");
	}

	private void resetGame() {
		for (int i = 0; i < 9; ++i)
			buffer.set(i, "");
		moves = 0;
		windowController.clearScreen();
	}

	private void specialSituation(String oponentName, int x) {
		switch (x) {
		case 4:
			if (moves > 1 || playedGames > 0) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Przeciwnik opuścił grę!");
					alert.setHeaderText(null);
					alert.setContentText("Gra zostanie zresetowana");
					alert.showAndWait();
				});
			}
			resetGame();
			playedGames = 0;
			myTurn = true;
			break;
		}
	}

	private int gameFinished(String symbol, int x, int y) { // zwraca wartosc ujemna jesli remis, dodatnia jesli gra
															// rozstala rozstrzygnieta i 0 jesli trwa
		boolean finish = true;
		for (int i = 0; i < 3; ++i)
			finish = finish && (buffer.get(3 * y + i) == symbol);
		if (finish)
			return 1;
		finish = true;
		for (int i = 0; i < 3; ++i)
			finish = finish && (buffer.get(3 * i + x) == symbol);
		if (finish)
			return 1;

		if (x == y) {
			finish = true;
			for (int i = 0; i < 3; ++i)
				finish = finish && (buffer.get(3 * i + i) == symbol);
		}
		if (finish)
			return 1;
		if (x == 2 - y) {
			finish = true;
			for (int i = 0; i < 3; ++i)
				finish = finish && (buffer.get(3 * i + (2 - i)) == symbol);
		}
		if (finish)
			return 1;
		if (moves > 8)
			return -1;
		return 0;
	}

	void setName(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}

	String getJustSend() {
		return justSend;
	}

	void setProducer() {
		this.producer = new Producer();
	}

	Producer getProducer() {
		return producer;
	}

	void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	void myMove(int x, int y) {
		myTurn = false;
		++moves;
		if (name == null)
			name = "X";
		if (!consumer.filterIsSet()) {
			consumer.getJMSConsumer().close();
			consumer.receiveQueueMessagesAsynch();
		}
		justSend = name + x + y;
		producer.sendQueueMessages(name, x, y);
		buffer.set(3 * y + x, name);
		int finished = gameFinished(name, x, y);
		if (finished != 0) {
			myTurn = false;
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Koniec gry! " + (finished < 0 ? "Remis" : "Wygrałeś!"));
			alert.setHeaderText(null);
			alert.setContentText("Czy chcesz zagrać kolejną partię?");
			ButtonType buttonYes = new ButtonType("Tak", ButtonData.YES);
			ButtonType buttonNo = new ButtonType("Nie", ButtonData.NO);
			alert.getButtonTypes().setAll(buttonYes, buttonNo);
			Optional<ButtonType> result = alert.showAndWait();
			++playedGames;
			if (result.get() == buttonYes) {
				resetGame();
				if (playedGames % 2 == 0 && name == "X" || playedGames % 2 != 0 && name == "O")
					myTurn = true;
			} else {
				primaryStage.close();
				consumer.getJMSConsumer().close();
				consumer.getJMSContext().close();
			}
		}
	}

	void oponentMove(String oponentName, int x, int y) throws IOException {
		if (x > 3) {
			specialSituation(oponentName, x);
			return;
		}
		if (name == null) 
			name = oponentName.equals("O") ? "X" : "O";
		buffer.set(3 * y + x, name == "X" ? "O" : "X");
		myTurn = true;
		++moves;
		windowController.drawOponent(x, y);
		int finished = gameFinished(name == "X" ? "O" : "X", x, y);
		if (finished != 0) {
			Platform.runLater(() -> {
				myTurn = false;
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Koniec gry! " + (finished < 0 ? "Remis" : "Przegrałeś!"));
				alert.setHeaderText(null);
				alert.setContentText("Czy chcesz zagrać kolejną partię?");
				ButtonType buttonYes = new ButtonType("Tak", ButtonData.YES);
				ButtonType buttonNo = new ButtonType("Nie", ButtonData.NO);
				alert.getButtonTypes().setAll(buttonYes, buttonNo);
				Optional<ButtonType> result = alert.showAndWait();
				++playedGames;
				if (result.get() == buttonYes) {
					resetGame();
					if (playedGames % 2 == 0 && name == "X" || playedGames % 2 != 0 && name == "O")
						myTurn = true;
				} else {
					producer.sendQueueMessages(name, 4, 4);
					primaryStage.close();
					consumer.getJMSConsumer().close();
					consumer.getJMSContext().close();
				}
			});
		}
	}

	public boolean isMyTurn() {
		return myTurn;
	}
	
	public int getMoves() {
		return moves;
	}
	public int getPlayedGames() {
		return playedGames;
	}
}
