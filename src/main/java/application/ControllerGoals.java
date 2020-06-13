package application;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerGoals extends AnchorPane implements Initializable {
    public ControllerGoals() {
        FXMLLoader goGoals = new FXMLLoader(getClass().getResource("../../resources/Goals.fxml"));
        goGoals.setRoot(this);
        goGoals.setController(this);
        try {
            goGoals.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
