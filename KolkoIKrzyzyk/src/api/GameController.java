package api;

import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class GameController {
	ArrayList<String> buffer = new ArrayList<String>(9);
	WindowController cont;
	Stage stage;
	Producer producer;
	String name = "X";
	String justSend;
	boolean myTurn = true;

	public GameController(WindowController cont, Stage stage) {
		this.cont = cont;
		this.stage = stage;
		for (int i=0; i<9;++i)
			buffer.add("");
	}

	public void myMove(int x, int y) {
		myTurn = false;
		justSend = name + x + y;
		producer.sendQueueMessages(name, x, y);
		buffer.set(3 * y + x, name);
		if (gameFinished(name, x, y)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Koniec gry!");
			alert.setHeaderText(null);
			alert.setContentText("Wygrałeś!");
			alert.showAndWait();
			stage.close();
		}
	}

	public void oponentMove(int x, int y) {
		buffer.set(3 * y + x, name == "X" ? "O" : "X");
		myTurn = true;
		cont.drawOponent(x, y);
		if (gameFinished(name == "X" ? "O" : "X", x, y)) {
			myTurn = false;
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Koniec gry!");
			alert.setHeaderText(null);
			alert.setContentText("Przegrałeś!");
			alert.showAndWait();
			stage.close();
		}
	}

	public GameController() {
		for (int i = 0; i < 9; ++i)
			buffer.add("");
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

	Producer getProducer() {
		return producer;
	}

	boolean isMyTurn() {
		return myTurn;
	}

	void setProducer() {
		this.producer = new Producer();
	}

	boolean gameFinished(String symbol, int x, int y) {
		boolean finish = true;
		for (int i = 0; i < 3; ++i)
			finish = finish && (buffer.get(3 * y + i) == symbol);
		if (finish)
			return finish;
		finish = true;
		for (int i = 0; i < 3; ++i)
			finish = finish && (buffer.get(3 * i + x) == symbol);
		if (finish)
			return finish;

		if (x == y) {
			finish = true;
			for (int i = 0; i < 3; ++i)
				finish = finish && (buffer.get(3 * i + i) == symbol);
		}
		if (finish)
			return finish;
		if (x == 3 - y) {
			finish = true;
			for (int i = 0; i < 3; ++i)
				finish = finish && (buffer.get(3 * i + (3 - i)) == symbol);
		}
		return finish;
	}
}
