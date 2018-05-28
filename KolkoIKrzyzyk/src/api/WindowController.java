package api;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class WindowController {
	private GameController gameController;
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

	@FXML
	void hovered(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		if (lbl.getText() == "" && gameController.isMyTurn())
			lbl.setStyle("-fx-background-color: rgba(255,165,0,1);");
	}

	@FXML
	void exited(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		if (lbl.getText() == "" && gameController.isMyTurn())
			lbl.setStyle("-fx-background-color: rgba(0,0,0,0);");

	}

	@FXML
	void clicked(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		if (lbl.getText() == "" && gameController.isMyTurn()) {
			lbl.setText(gameController.getName() == null ? "X" : gameController.getName());
			lbl.setStyle("-fx-background-color: rgba(0,0,0,0);");
			int x = Integer.parseInt(lbl.getId().substring(1, 2));
			int y = Integer.parseInt(lbl.getId().substring(2, 3));
			gameController.myMove(x, y);
		}
	}

	void drawOponent(int x, int y) {
		ArrayList<Label> list = new ArrayList<Label>(Arrays.asList(l00, l10, l20, l01, l11, l21, l02, l12, l22));
		Platform.runLater(() -> list.get(3 * y + x).setText(gameController.getName() == "X" ? "O" : "X"));
	}
	void setGameController(GameController cont) {
		this.gameController = cont;
	}
	void clearScreen () {
		ArrayList<Label> list = new ArrayList<Label>(Arrays.asList(l00, l10, l20, l01, l11, l21, l02, l12, l22));
		list.forEach(x -> Platform.runLater(()-> x.setText("")));
	}

}
