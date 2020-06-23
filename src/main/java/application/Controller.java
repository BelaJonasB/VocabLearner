package application;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import okhttp3.Response;

public class Controller extends Main implements Initializable {
	public PasswordField pw;
	@FXML
	private TextField mail,serverAdress;
	@FXML
	private Button reg,login;
	@FXML
	private Label wrongLogin;
	@FXML
	private CheckBox remember;
	@FXML
	private VBox visualBox, loginBox;
	@FXML
	private AnchorPane outer;
	@FXML
	private BorderPane mainLogCont;

	double x = primarStage.getX(), y = primarStage.getY();
	Gson g = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	Crypt c;

	public void initialize(URL arg0, ResourceBundle arg1) {
		//Position saving for other scenes
		primarStage.xProperty().addListener((observable, oldValue, newValue) -> x = newValue.doubleValue());
		primarStage.yProperty().addListener((observable, oldValue, newValue) -> y = newValue.doubleValue());


		//Enryption key with mac adress
		String mac = "DiddiKong"; //Default, if no mac adress
		try {
			NetworkInterface net = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			mac = new String(net.getHardwareAddress());
		} catch (SocketException | UnknownHostException e) {
			System.out.println("No Mac-Address found");
		}
		c = new Crypt(mac);

		//default enabling
		AnchorPane.setLeftAnchor(visualBox, 0.0);
		AnchorPane.setRightAnchor(mainLogCont, 0.0);
		loginBox.prefHeightProperty().bind(outer.heightProperty());
		login.disableProperty().bind(mail.textProperty().isEmpty());
		reg.disableProperty().bind(mail.textProperty().isEmpty());
		wrongLogin.setVisible(false);

		//set the Remembered values
		try {
			User u = g.fromJson(new FileReader("src/main/resources/logInfo.json"), User.class);
			//accesses the file in the resources not root dir
			if (!(u.getEmail().isEmpty() || u.getPassword().isEmpty())) {
				mail.setText(u.getEmail());
				pw.setText(c.decrypt(u.getPassword()));
				if (!u.getEmail().equals("")) {
					remember.setSelected(true);
				}
				login();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		//Listener for Mail
		mail.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.contains("@")) {
				//Warning and Button
				wrongLogin.setVisible(false);
				login.disableProperty().bind(pw.textProperty().isEmpty());
				reg.disableProperty().bind(pw.textProperty().isEmpty());

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
			} else {
				login.disableProperty().unbind();
				reg.disableProperty().unbind();
				login.setDisable(true);
				reg.setDisable(true);
				//show warning
				wrongLogin.setText("Not a valid Email");
				wrongLogin.setVisible(true);
			}
		});

		//Hover on Buttons
		buttonFeedback(login);
		buttonFeedback(reg);


	}
	public void buttonFeedback(Button b) {
		b.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue) {
				b.setStyle("-fx-font-size: 14; -fx-padding: 4 0 4 0");
			} else {
				b.setStyle("");
			}
		});
	}


	//Login and register by using the Methods from APICalls:

	public void login() {
		Variables.setMail(mail.textProperty().get());

		//loading animation
		ControllerLoading load = new ControllerLoading();
		mainLogCont.setCenter(load);


		Thread t = new Thread(() -> {
			APICalls resp = new APICalls(mail.textProperty().get(), pw.textProperty().get(), serverAdress.textProperty().get());
			try {
				Response regResp = resp.login();
				Platform.runLater(() -> logReg(regResp, "Wrong eMail and/or password"));
				//resp.postToVoc(new Vocab("Fungus", "Fuchs", "ENG"));
				resp.getUsersVocab();

			} catch (UnknownHostException|IllegalArgumentException i) {
				Platform.runLater(() -> {
					mainLogCont.setCenter(loginBox);
					wrongLogin.setText("Server error!");
					wrongLogin.setVisible(true);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
	}

	public void register() {
		Variables.setMail(mail.textProperty().get());

		//loading animation
		ControllerLoading load = new ControllerLoading();
		mainLogCont.setCenter(load);


		Thread t = new Thread(() -> {
			APICalls resp = new APICalls(mail.textProperty().get(), pw.textProperty().get(), serverAdress.textProperty().get());
			try {
				Response regResp = resp.register();
				Platform.runLater(() -> logReg(regResp, "eMail already registered"));
				resp.getUsersVocab();
			} catch (UnknownHostException h) {
				Platform.runLater(() -> {
					mainLogCont.setCenter(loginBox);
					wrongLogin.setText("Server error!");
					wrongLogin.setVisible(true);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
	}

	//what to do with Response code:
	private void logReg(Response regResp, String warn) {
		if(regResp.code()==200) {
			try {
				//Object form User if remember is selected, else delete user
				String s;

				if(remember.selectedProperty().get()) {
					//encrypt PW
					String encryptetPW = c.encrypt(pw.textProperty().get());

					User u = new User(mail.textProperty().get(), encryptetPW);
					s = g.toJson(u);
				} else {
					User u = new User("", "");
					s = g.toJson(u);
				}

				//login Info To JSon
				FileWriter file = new FileWriter("src/main/resources/logInfo.json");
				file.write(s);
				file.close();
				changeScene("MainScene.fxml", 1080, 620, true, false, x, y);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mainLogCont.setCenter(loginBox);
			wrongLogin.setText(warn);
			wrongLogin.setVisible(true);
		}

	}


}
