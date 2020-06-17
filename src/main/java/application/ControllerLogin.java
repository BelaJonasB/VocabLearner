package application;

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
	double x,y;

	public void initialize(URL arg0, ResourceBundle arg1) {
		primarStage.xProperty().addListener((observable, oldValue, newValue) -> x = newValue.doubleValue());
		primarStage.yProperty().addListener((observable, oldValue, newValue) -> y = newValue.doubleValue());

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
		mainContentFrame.prefHeightProperty().bind(primarStage.heightProperty().subtract(125));
		mainContent.prefHeightProperty().bind(mainContentFrame.heightProperty());
		navBox.prefHeightProperty().bind(primarStage.heightProperty());
		AnchorPane.setTopAnchor(navBar, 0.0);

		//User Info for User
		loginAs.setText(Variables.getMail());

		//Button feedback:
		feedbackButton(vocab);
		feedbackButton(learn);
		feedbackButton(goals);

	}
	public void feedbackButton(Button b) {
		b.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				b.setStyle("-fx-background-color: #ffdc00");
			} else if(oldV) {
				b.setStyle("-fx-background-color: #ffe600");
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
		FileWriter file = new FileWriter("src/main/resources/logInfo.json");
										file.write(s);
										file.close();
		changeScene("FXLogin.fxml", 850,520, false, false, x, y);
	}
}
