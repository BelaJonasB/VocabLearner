package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ControllerVocList extends AnchorPane {
    public ControllerVocList() {
        FXMLLoader goList = new FXMLLoader(getClass().getResource("VocList.fxml"));
        goList.setRoot(this);
        goList.setController(this);
        try {
            goList.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
