package application;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;


public class ControllerLogin extends Main implements Initializable {
	@FXML
	private Label loginAs,chuck;
	@FXML
	private Button back, vocab, learn, goals;
	Gson g = new Gson();
	@FXML
	ImageView logo,shadow;
	@FXML
	VBox shadowBox, navBar, mainContentFrame, navBarBot;
	@FXML
	BorderPane mainContent;
	@FXML
	AnchorPane navBox;
	@FXML
	HBox userInfo;


	public void initialize(URL arg0, ResourceBundle arg1) {
		//SetDefaultmainContent
		ControllerVocList VocListCont = new ControllerVocList();
		mainContent.setCenter(VocListCont);

		//Sizes of Elements:
		logo.setFitWidth(120);
		logo.setPreserveRatio(true);
		shadow.setFitWidth(160);
		shadow.setFitHeight(10);
		shadow.setPreserveRatio(false);
		userInfo.prefWidthProperty().bind(primarStage.widthProperty());
		mainContentFrame.prefHeightProperty().bind(primarStage.heightProperty().subtract(130));
		mainContent.prefHeightProperty().bind(mainContentFrame.heightProperty());
		navBox.prefHeightProperty().bind(primarStage.heightProperty());
		AnchorPane.setTopAnchor(navBar, 0.0);
		AnchorPane.setBottomAnchor(navBarBot, 0.0);

		loginAs.setText(Variables.getMailShow());

		//Button feedback:
		feedbackButton(back);
		feedbackButton(vocab);
		feedbackButton(learn);
		feedbackButton(goals);

	}
	public void feedbackButton(Button b) {
		b.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				b.setStyle("-fx-background-color: #ffd500");
			} else if(oldV) {
				b.setStyle("-fx-background-color: #ffdd00");
			}
		});
	}

	//Switch to VocList
	public void gotoVocList() {
		ControllerVocList VocListCont = new ControllerVocList();
		mainContent.setCenter(VocListCont);
	}

	//Switch to Learning
	public void gotoLearn() {
		ControllerLearning learnCont = new ControllerLearning();
		mainContent.setCenter(learnCont);
	}

	//Switch to Goals (what to learn)
	public void gotoGoals() {
		ControllerGoals goalsCont = new ControllerGoals();
		mainContent.setCenter(goalsCont);
	}

	public void back() throws Exception {
		changeScene("FXLogin.fxml", 350,320, false);
	}
}
