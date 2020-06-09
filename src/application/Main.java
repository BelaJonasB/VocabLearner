package application;
	
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.FileReader;

public class Main extends Application {
	public static Stage primarStage;
	Gson g = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	public void start(Stage primaryStage) throws Exception {
		//First Window
		Parent root;
		primarStage = primaryStage;

		//if Remembered -> direkt zum main Window
		User u = g.fromJson(new FileReader("logInfo.json"), User.class);
		if(!(u.getEmail().isEmpty()||u.getPassword().isEmpty())) {
			root = FXMLLoader.load(getClass().getResource("LoginBox.fxml"));
			primarStage.setResizable(true);
			primarStage.setScene(new Scene(root, 900, 520));
		} else {
			root = FXMLLoader.load(getClass().getResource("FXLogin.fxml"));
			primarStage.setResizable(false);
			primarStage.setScene(new Scene(root, 350, 470));

		}
		primarStage.setTitle("Vocabulary");
		primarStage.getIcons().add(new Image(getClass().getResourceAsStream("logo-512.png")));
		primarStage.show();


	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void changeScene(String fxml, int w, int h, boolean resize) throws Exception {
		 Parent loader = FXMLLoader.load(getClass().getResource(fxml));
		 primarStage.setScene(new Scene(loader, w, h));
		 primarStage.setTitle("Vocabulary");
		 primarStage.setResizable(resize);
		 primarStage.show();
	}
}
