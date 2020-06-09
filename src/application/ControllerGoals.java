package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ControllerGoals extends AnchorPane {
    public ControllerGoals() {
        FXMLLoader goGoals = new FXMLLoader(getClass().getResource("Goals.fxml"));
        goGoals.setRoot(this);
        goGoals.setController(this);
        try {
            goGoals.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
