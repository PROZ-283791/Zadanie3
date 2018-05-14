package api;

import java.util.ArrayList;
import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class WindowController {
	Producer producer;
	String name = "X";
	String justSend;
	boolean myTurn = true;
	@FXML
	GridPane grid;
	@FXML
	Label l00;
	@FXML
	Label l10;
	@FXML
	Label l20;
	@FXML
	Label l01;
	@FXML
	Label l11;
	@FXML
	Label l21;
	@FXML
	Label l02;
	@FXML
	Label l12;
	@FXML
	Label l22;

	ArrayList<String> buffer = new ArrayList<String>(9);

	@FXML
	private void initialize() {
		for (int i = 0; i < 9; ++i)
			buffer.add("");
	}

	@FXML
	void hovered(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		if (lbl.getText() == "" && myTurn)
			lbl.setStyle("-fx-background-color: rgba(255,165,0,1);");
	}

	@FXML
	void exited(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		if (lbl.getText() == "" && myTurn)
			lbl.setStyle("-fx-background-color: rgba(0,0,0,0);");

	}

	@FXML
	void clicked(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		if (lbl.getText() == "" && myTurn) {
			int x = Integer.parseInt(lbl.getId().substring(1, 2));
			int y = Integer.parseInt(lbl.getId().substring(2, 3));
			lbl.setText(name);
			myTurn = false;
			lbl.setStyle("-fx-background-color: rgba(0,0,0,0);");
			justSend = name + x + y;
			producer.sendQueueMessages(name, x, y);
			buffer.set(3 * y + x, name);
			if (gameFinished(name, x, y)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Koniec gry!");
				alert.setHeaderText(null);
				alert.setContentText("Wygrałeś!");
				alert.showAndWait();
			}
		}
	}

	void drawOponent(int x, int y) {
		ArrayList<Label> list = new ArrayList<Label>(Arrays.asList(l00, l10, l20, l01, l11, l21, l02, l12, l22));
		Platform.runLater(() -> list.get(3 * y + x).setText(name == "X" ? "O" : "X"));
		buffer.set(3 * y + x, name == "X" ? "O" : "X");
		myTurn = true;
		if (gameFinished(name == "X" ? "O" : "X", x, y)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Koniec gry!");
			alert.setHeaderText(null);
			alert.setContentText("Przegrałeś!");
			alert.showAndWait();
		}
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

	// @Override
	// public void onMessage(Message message) {
	// TextMessage textMessage = (TextMessage) message;
	// try {
	// System.out.printf("Odebrano wiadomość:'%s' \n", textMessage.getText());
	// int x = Integer.parseInt(textMessage.getText().substring(1, 2));
	// int y = Integer.parseInt(textMessage.getText().substring(2, 3));
	// drawO(x,y);
	// } catch (JMSException e) {
	// e.printStackTrace();
	// }
	// }
}
