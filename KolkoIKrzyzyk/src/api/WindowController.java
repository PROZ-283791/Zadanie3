package api;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class WindowController {
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
		if (lbl.getText() == "")
			lbl.setStyle("-fx-background-color: rgba(255,165,0,1);");
	}

	@FXML
	void exited(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		lbl.setStyle("-fx-background-color: rgba(0,0,0,0);");

	}

	@FXML
	void clicked(MouseEvent e) {
		Label lbl = (Label) e.getSource();
		lbl.setText("X");
		lbl.setStyle("-fx-background-color: rgba(0,0,0,0);");
	}
}
