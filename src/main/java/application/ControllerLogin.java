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
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ControllerLogin extends Main implements Initializable {
	@FXML
	private Label loginAs;
	@FXML
	private Button vocab, learn, goals, logout;
	@FXML
	ImageView logo;
	@FXML
	VBox navBar, mainContentFrame;
	@FXML
	BorderPane mainContent;
	@FXML
	AnchorPane navBox;
	@FXML
	HBox userInfo;
	double x = primarStage.getX(), y = primarStage.getY();
	//Standard values for buttons
	String colorVoc = "-fx-background-color: #ffec00 ; -fx-border-width: 0 0 0 5; -fx-border-color: white",colorGoals = "-fx-background-color: #ffe600",colorLearn = "-fx-background-color: #ffe600";

	public void initialize(URL arg0, ResourceBundle arg1) {
		logout.setTooltip(new Tooltip("Logout"));
		primarStage.xProperty().addListener((observable, oldValue, newValue) -> x = newValue.doubleValue());
		primarStage.yProperty().addListener((observable, oldValue, newValue) -> y = newValue.doubleValue());

		//SetDefaultmainContent
		ControllerVocList VocListCont = new ControllerVocList();
		mainContent.setCenter(VocListCont);

		//Sizes of Elements:
		logo.setFitWidth(120);
		logo.setPreserveRatio(true);
		userInfo.prefWidthProperty().bind(primarStage.widthProperty());
		mainContentFrame.prefHeightProperty().bind(primarStage.heightProperty().subtract(125));
		mainContent.prefHeightProperty().bind(mainContentFrame.heightProperty());
		navBox.prefHeightProperty().bind(primarStage.heightProperty());
		AnchorPane.setTopAnchor(navBar, 0.0);

		//User Info for User
		loginAs.setText(Variables.getMail());

		//Button feedback:
		vocab.setStyle(colorVoc);
		vocab.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				vocab.setStyle("-fx-font-size: 17;"+colorVoc);
			} else if(oldV) {
				vocab.setStyle("-fx-font-size: 15;"+colorVoc);
			}
		});
		learn.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				learn.setStyle("-fx-font-size: 17;"+colorLearn);
			} else if(oldV) {
				learn.setStyle("-fx-font-size: 15;"+colorLearn);
			}
		});
		goals.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				goals.setStyle("-fx-font-size: 17;"+colorGoals);
			} else if(oldV) {
				goals.setStyle("-fx-font-size: 15;"+colorGoals);
			}
		});

	}

	//Switch to VocList
	public void gotoVocList() {
		ControllerVocList VocListCont = new ControllerVocList();
		mainContent.setCenter(VocListCont);
		vocab.setStyle("-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white");
		goals.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		learn.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		colorVoc = "-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white";
		colorGoals = "-fx-background-color: #ffe600";
		colorLearn = "-fx-background-color: #ffe600";
	}

	//Switch to Learning
	public void gotoLearn() {
		ControllerLearning learnCont = new ControllerLearning();
		mainContent.setCenter(learnCont);
		learn.setStyle("-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white");
		vocab.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		goals.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		colorVoc = "-fx-background-color: #ffe600";
		colorGoals = "-fx-background-color: #ffe600";
		colorLearn = "-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white";
	}

	//Switch to Goals (what to learn)
	public void gotoGoals() {
		ControllerGoals goalsCont = new ControllerGoals();
		mainContent.setCenter(goalsCont);
		goals.setStyle("-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white");
		vocab.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		learn.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		colorVoc = "-fx-background-color: #ffe600";
		colorGoals = "-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white";
		colorLearn = "-fx-background-color: #ffe600";
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
