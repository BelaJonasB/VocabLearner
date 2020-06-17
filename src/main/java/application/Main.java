package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static Stage primarStage;



	public void start(Stage primaryStage) throws Exception {
		//First Window
		Parent root;
		primarStage = primaryStage;

		root = FXMLLoader.load(getClass().getResource("/FXLogin.fxml"));
		primarStage.setResizable(false);
		primarStage.setScene(new Scene(root, 850, 520));
		primarStage.setTitle("Vocabulary");
		primarStage.centerOnScreen();
		primarStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo-512.png")));
		primarStage.show();


	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void changeScene(String fxml, int w, int h, boolean resize, boolean centerScreen, double x, double y) throws Exception {
		 Parent loader = FXMLLoader.load(getClass().getResource("/"+fxml));
		 primarStage.hide();
		 if(centerScreen) {
			 Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			 primarStage.setX((screenBounds.getWidth() - w)/2);
			 primarStage.setY((screenBounds.getHeight() - h)/2);
		 } else {
		 	primarStage.setX(x);
		 	primarStage.setY(y);
		 }
		 primarStage.setScene(new Scene(loader, w, h));
		 primarStage.setTitle("Vocabulary");
		 primarStage.setResizable(resize);
		 primarStage.show();
	}
}
