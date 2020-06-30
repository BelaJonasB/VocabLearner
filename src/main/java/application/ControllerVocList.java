package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.event.ActionEvent;



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
    public Button  SearchButton,AddButton,DeleteButton;
    @FXML
    public ToggleButton EditSwitch;
    @FXML
    public ButtonBar ButtonBar;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Create Columns
<<<<<<< Updated upstream
        TableColumn <VocabList, String> numberColumn = new TableColumn<>("Nummer");
        TableColumn<VocabList, String> language1Column = new TableColumn<>("Deutsch");
        TableColumn<VocabList, String> language2Column = new TableColumn<>("Englisch");
        TableColumn<VocabList, String> phaseColumn = new TableColumn<>("Phase");
        TableColumn<VocabList, String> selectColumn = new TableColumn<>("Auswählen");
        selectColumn.setVisible(false);

        //VocTableList.getColumns().addAll(numberColumn, language1Column, language2Column, phaseColumn, selectColumn);
=======
        TableColumn <VocabList, Integer> numberColumn = new TableColumn("Nummer");
        TableColumn <VocabList, String> language1Column  = new TableColumn("Deutsch");
        TableColumn <VocabList, String> language2Column = new TableColumn("Englisch");
        TableColumn <VocabList, Integer> phaseColumn = new TableColumn("Phase");
        TableColumn <VocabList, Boolean> selectColumn = new TableColumn("Auswaehlen");
>>>>>>> Stashed changes

        //Fill all Cells
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        language1Column.setCellValueFactory(new PropertyValueFactory<>("language1"));
        language2Column.setCellValueFactory(new PropertyValueFactory<>("language2"));
        phaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        //selectColumn.setEditable(false); //Makes selectColumn not editable
        //selectColumn.setVisible(false); //Makes selectColumn invisible by default !!

        //Get Data from list
        ObservableList<VocabList> list = getVocList();
<<<<<<< Updated upstream
        VocTableList.setItems(list);
        VocTableList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
=======
>>>>>>> Stashed changes

        for (int i= 0; i< 20 ;i++){
            list.add(new VocabList(i+3 ,"test"+i,"result"+i,0));
        }

        VocTableList.setItems(list);
        VocTableList.getColumns().addAll(numberColumn, language1Column, language2Column, phaseColumn, selectColumn);

        VocTableList.setEditable(true);  //ENABLED FOR TESTING

        //Listener for Search
        //SearchBarTextField.textProperty.addListener((observable, oldValue, newValue) -> {
         //   String search = "test";
        //});
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    ObservableList<VocabList> vocabListsRemove = FXCollections.observableArrayList();

                    for (VocabList voc : list) {
                        if (voc.getSelect()) {
                            vocabListsRemove.add(voc);
                        }
                    }
                    list.removeAll(vocabListsRemove);
                }
            };

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
      //  EditSwitch.onActionProperty(e -> selectColumn.setVisible(true));

    }

    /**
     * delete vocabulary
     */
/*
    public void deleteSelected (ActionEvent event){
        ObservableList<VocabList> vocabListsRemove = FXCollections.observableArrayList();

        for (VocabList voc : list) {
            if (voc.getSelect().isSelected()) {
                vocabListsRemove.add(voc);
            }
        }
        list.removeAll(vocabListsRemove);
        }
*/

    /**
     * Filters after some arguments
     */
    public void filterButtonPressed() {
        //TODO implement
    }

    //Temp data for testing
    private  ObservableList<VocabList> getVocList () {
        VocabList voc0 = new VocabList(0, "Folter", "torture", 0,true);
        VocabList voc1 = new VocabList(1, "Schmerz", "pain", 0);
        VocabList voc2 = new VocabList(2, "Kuh", "cow", 0);

        ObservableList<VocabList> vocList = FXCollections.observableArrayList(voc0,voc1, voc2);
        return vocList;
    }
}
