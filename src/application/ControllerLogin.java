package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ControllerLogin extends Main implements Initializable {
	@FXML
	private Label loginAs;
	@FXML
	private Button vocab, learn, goals;
	@FXML
	ImageView logo,shadow;
	@FXML
	VBox shadowBox, navBar, mainContentFrame;
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

		//User Info for User
		Gson g = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		try {
			User u = g.fromJson(new FileReader("logInfo.json"), User.class);
			loginAs.setText(u.getEmail());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		//Button feedback:
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

	public void logout() throws Exception {
		Gson g = new GsonBuilder()
				.setPrettyPrinting()
				.create();

		//Make User Info empty
		User u = new User("", "");
		String s = g.toJson(u);

		//login Info To JSon
		FileWriter file = new FileWriter("logInfo.json");
										file.write(s);
										file.close();
		changeScene("FXLogin.fxml", 350,470, false, true);
	}
}
