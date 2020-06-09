package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static Stage primarStage;

	public void start(Stage primaryStage) throws Exception {
		//First Window
		Parent root = FXMLLoader.load(getClass().getResource("FXLogin.fxml"));
		primarStage = primaryStage;
		primarStage.setResizable(false);
		primarStage.setScene(new Scene(root, 350, 320));
		primarStage.setTitle("Login");
		primarStage.getIcons().add(new Image(getClass().getResourceAsStream("logo-512.png")));
		primarStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void changeScene(String fxml, int w, int h, boolean resize) throws Exception {
		 Parent loader = FXMLLoader.load(getClass().getResource(fxml));
		 primarStage.setScene(new Scene(loader, w, h));
		 primarStage.setTitle("Login");
		 primarStage.setResizable(resize);
		 primarStage.show();
	}
}
