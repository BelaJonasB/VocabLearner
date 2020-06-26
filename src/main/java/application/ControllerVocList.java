package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;



/**
 * Generates the Vocabulary list and implements the functions search,edit,delete and filter
 */
public class ControllerVocList extends AnchorPane implements Initializable {
    public VBox VBoxMain, VBoxMenu, VBoxMenuFrameLeft, VBoxMenuFrameRight, VBoxList;
    @FXML
    public HBox HBoxMenuFrame;
    @FXML
    public TextField SearchBarTextField;
    @FXML
    public Button FilterButton, SearchButton;
    @FXML
    public ToggleButton EditSwitch;
    @FXML
    public TableView<VocabList> VocTableList;


    public ControllerVocList() {
        FXMLLoader goList = new FXMLLoader(getClass().getResource("/VocList.fxml"));
        goList.setRoot(this);
        goList.setController(this);
        try {
            goList.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Create Columns
        TableColumn <VocabList, String> numberColumn = new TableColumn("Nummer");
        TableColumn language1Column = new TableColumn("Deutsch");
        TableColumn language2Column = new TableColumn("Englisch");
        TableColumn phaseColumn = new TableColumn("Phase");
        TableColumn selectColumn = new TableColumn("Auswählen");
        selectColumn.setVisible(false);

        //VocTableList.getColumns().addAll(numberColumn, language1Column, language2Column, phaseColumn, selectColumn);

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        language1Column.setCellValueFactory(new PropertyValueFactory<>("language1"));
        language2Column.setCellValueFactory(new PropertyValueFactory<>("language2"));
        phaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));

        ObservableList<VocabList> list = getVocList();
        VocTableList.setItems(list);

        VocTableList.getColumns().addAll(numberColumn, language1Column, language2Column, phaseColumn, selectColumn);

        VocTableList.setEditable(false);

        //Listener for Search
        //SearchBarTextField.textProperty.addListener((observable, oldValue, newValue) -> {
         //   String search = "test";
        //});
    }



    /**
     * When called, starts searching for the String entered in SearchBarTextField
     */
    public void searchButtonPressed(){

    }

    /**
     * Enables/ disables edit
     */
    public void editSwitchSwitch (){
       // EditSwitch.onActionProperty(e -> selectColumn.setVisible(true));

    }

    /**
     * delete vocabulary
     */
    public void delete(){

    }
    /**
     * Filters after some arguments
     */
    public void filterButtonPressed() {
        //TODO implement
    }

    //Temp data for testing
    private ObservableList<VocabList> getVocList () {
        VocabList voc0 = new VocabList(0, "Folter", "torture", 0);
        VocabList voc1 = new VocabList(1, "Schmerz", "pain", 0);
        VocabList voc2 = new VocabList(2, "Kuh", "cow", 0);

        ObservableList<VocabList> vocList = FXCollections.observableArrayList(voc0,voc1, voc2);
        return vocList;
    }
}
