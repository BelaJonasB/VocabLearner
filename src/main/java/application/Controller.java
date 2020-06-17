package application;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

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
	double x,y;
	Gson g = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	Crypt c;

	public void initialize(URL arg0, ResourceBundle arg1) {
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
		AnchorPane.setRightAnchor(loginBox, 0.0);
		loginBox.prefHeightProperty().bind(outer.heightProperty());
		login.disableProperty().bind(mail.textProperty().isEmpty());
		reg.disableProperty().bind(mail.textProperty().isEmpty());
		wrongLogin.setVisible(false);

		//set the Remembered values
		try {
			//User u = g.fromJson(new FileReader(getClass().getResource("/logInfo.json").getFile()), User.class);
			User u = g.fromJson(new FileReader("src/main/resources/logInfo.json"), User.class);
			//accesses the file in the resources not root dir
			if(!(u.getEmail().isEmpty()||u.getPassword().isEmpty())) {
				mail.setText(u.getEmail());
				pw.setText(c.decrypt(u.getPassword()));
				if(!u.getEmail().equals("")) {
					remember.setSelected(true);
				}
				login();
			}
		} catch (FileNotFoundException e) {
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
			};
		});
	}
	public void login() {
		Variables.setMail(mail.textProperty().get());
		Variables.setPw(pw.textProperty().get());

		OkHttpClient o = new OkHttpClient();
		Request req = new Request.Builder()
				.header("email", mail.textProperty().get())
				.header("password", pw.textProperty().get())
				.url(serverAdress.textProperty().get()+"login")
				.build();
		call(o, req, "Wrong eMail and/or password");

	}
	public void register() {
		Variables.setMail(mail.textProperty().get());
		Variables.setPw(pw.textProperty().get());

		//User data to Object
		User u = new User(mail.textProperty().get(), pw.textProperty().get());
		Gson g = new Gson();
		String s = g.toJson(u);
		System.out.println(s);

		//For Media Type in request body
		final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		//Call
		OkHttpClient o = new OkHttpClient();
		RequestBody regBody = RequestBody.create(s, JSON);
		Request req = new Request.Builder()
				.post(regBody)
				.url(serverAdress.textProperty().get()+"register")
				.build();
		call(o, req, "User already registered");

	}

	private void call(OkHttpClient o, Request req, String warn) {
		o.newCall(req).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Platform.runLater(() -> {
					wrongLogin.setText("Server error!");
					wrongLogin.setVisible(true);}
				);
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) {
				System.out.println(response.code());
				Platform.runLater(() -> {
							if(response.code()==200) {
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

									changeScene("LoginBox.fxml", 900, 520, true, false, x, y);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								wrongLogin.setText(warn);
								wrongLogin.setVisible(true);
							}
						}
				);
			}
		});
	}


}
