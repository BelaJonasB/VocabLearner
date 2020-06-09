package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

public class Controller extends Main implements Initializable {
	public PasswordField pw;
	@FXML
	private TextField mail,serverAdress;
	@FXML
	private Button reg,login;
	@FXML
	private Label warning,wrongLogin;
	@FXML
	private CheckBox remember;
	Gson g = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	public void initialize(URL arg0, ResourceBundle arg1) {
		//default enabling
		login.disableProperty().bind(mail.textProperty().isEmpty());
		reg.disableProperty().bind(mail.textProperty().isEmpty());
		warning.setVisible(false);
		wrongLogin.setVisible(false);

		//set the Remembered values
		try {
			User u = g.fromJson(new FileReader("logInfo.json"), User.class);
			mail.setText(u.getEmail());
			pw.setText(u.getPassword());
			if(!u.getEmail().equals("")) {
				remember.setSelected(true);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//Listener for Mail
		mail.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.contains("@")) {
				//Warning and Button
				warning.setVisible(false);
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
				warning.setVisible(true);
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
										User u = new User(mail.textProperty().get(), pw.textProperty().get());
										s = g.toJson(u);
									} else {
										User u = new User("", "");
										s = g.toJson(u);
									}

									//login Info To JSon
									FileWriter file = new FileWriter("logInfo.json");
									file.write(s);
									file.close();

									changeScene("LoginBox.fxml", 900, 520, true);
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
