package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class Controller extends Main implements Initializable {
	public TextField pw;
	@FXML
	private TextField mail;
	@FXML
	private Button reg,login;
	@FXML
	private Label warning;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		//default enabling
		login.setDisable(true);
		warning.setVisible(false);

		//Listener for Mail
		mail.textProperty().addListener((observable, oldValue, newValue) -> {
			Variables.setMailShow(newValue);
			if(newValue.contains("@")) {
				//Warning and Button
				warning.setVisible(false);
				login.disableProperty().bind(pw.textProperty().isEmpty());

				//Enter=login
				primarStage.getScene().setOnKeyPressed(keyEvent -> {
					if (keyEvent.getCode() == KeyCode.ENTER && pw.textProperty().isNotEmpty().get()) {
						try {
							login();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} else if(!newValue.isEmpty()) {
				//show warning
				warning.setVisible(true);
			}
		});
		//Register enabling
		reg.disableProperty().bind(mail.textProperty().isNotEmpty().or(pw.textProperty().isNotEmpty()));

	}
	public void login() throws Exception {
		changeScene("LoginBox.fxml", 900, 520, true);
	}

}
