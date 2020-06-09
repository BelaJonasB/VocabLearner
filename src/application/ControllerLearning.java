package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class ControllerLearning extends AnchorPane {
    public ControllerLearning() {
        FXMLLoader goLog = new FXMLLoader(getClass().getResource("Learning.fxml"));
        goLog.setRoot(this);
        goLog.setController(this);
        try {
            goLog.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
