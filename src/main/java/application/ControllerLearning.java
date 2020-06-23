package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Maximilian Engelmann
 */
public class ControllerLearning extends AnchorPane implements Initializable {

    @FXML
    private Button solveButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField selectedVocable;
    @FXML
    private TextField vocTranslation;


    int i=0;


    ObservableList<VocabSelection> list;

    public ControllerLearning()
    {
        FXMLLoader goLog = new FXMLLoader(getClass().getResource("/Learning.fxml"));
        goLog.setRoot(this);
        goLog.setController(this);
        try {
            goLog.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextVocable();
    }

    public void solveVocable(){
        nextButton.setVisible(true);
        vocTranslation.setText("transTest"+i);
    }

    public void nextVocable(){
        i++;
        nextButton.setVisible(false);
        selectedVocable.setText("test"+i);
        vocTranslation.setText(" ");
    }

    public void selectVocable(String setting1 , String setting2){
        //TODO get list


    }
}
