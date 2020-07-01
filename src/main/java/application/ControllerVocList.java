package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
 * @author Lukas Radermacher
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

    private ObservableList<VocabList> list;

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

    /**
     * Initializes the controller class.
     * This method is automatically called after the login is finished(the corresponding fxml file: VocList.fxml is loaded)
     *
     * Initializes the search function,
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //execute language selection
        LocalizationManager.Init();
        setLang();

        //Create Columns
        TableColumn <VocabList, Integer> numberColumn = new TableColumn("Nummer");
        TableColumn <VocabList, String> language1Column  = new TableColumn("Deutsch");
        TableColumn <VocabList, String> language2Column = new TableColumn("Englisch");
        TableColumn <VocabList, Integer> phaseColumn = new TableColumn("Phase");
        TableColumn <VocabList, Boolean> selectColumn = new TableColumn("Auswaehlen");


        //Fill Cells with content
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        language1Column.setCellValueFactory(new PropertyValueFactory<>("language1"));
        language2Column.setCellValueFactory(new PropertyValueFactory<>("language2"));
        phaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        //selectColumn.setEditable(false); //Makes selectColumn not editable
        selectColumn.setVisible(false); //Makes selectColumn invisible by default !!

        //Get Data from list
        ObservableList<VocabList> list = getVocList();

        //Set Data to list and add Columns
        VocTableList.setItems(list);
        VocTableList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Tmp
        for (int i= 0; i< 20 ;i++){
            list.add(new VocabList(i+3 ,"test"+i,"result"+i,0));
        }

        VocTableList.setItems(list);
        VocTableList.getColumns().addAll(numberColumn, language1Column, language2Column, phaseColumn, selectColumn);
        VocTableList.setEditable(true);  //ENABLED FOR TESTING

        //

        //Doppelklick zum editieren 0.1
        VocTableList.setRowFactory( tv -> {
            TableRow<VocabList> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    VocabList rowData = row.getItem();
                    System.out.println(rowData);
                }
            });
            return row ;
        });

        //Listener for Search (should work but doesn't)
        /*
        FilteredList<VocabList> filteredVocList = new FilteredList<>(list, p -> true);

        SearchBarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            SearchBarTextField.setPredicate (VocabList-> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (VocabList.getLanguage1.contains(lowerCaseFilter)){
                    return true;
                }
                else if (String.valueOf(VocabList.getLanguage2()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                    });
                });
        SortedList<VocabList> sortedVocList = new SortedList<>(filteredVocList);
        sortedVocList.comparatorProperty().bind(VocTableList.comparatorProperty());
        VocTableList.setItems(sortedVocList);
        //TODO clear Field when clicked at
        */

        //Old Code
        /*
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    ObservableList<VocabList> vocabListsRemove = FXCollections.observableArrayList();

                    for (VocabList voc : list) {
                        if (voc.getSelect()==true) {
                            vocabListsRemove.add(voc);
                        }
                    }
                    list.removeAll(vocabListsRemove);
                }
            };
*/
        //Toggle Edit (does not work, what so ever)
        final ToggleGroup editGroup = new ToggleGroup();
        editGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue.isSelected()==true){
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(true); //Makes selectColumn invisible by default !!
                }
                else {
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(false); //Makes selectColumn invisible by default !!
                }
            }
        });

    }

    /**
     * When called, starts searching for the String entered in SearchBarTextField (Wahrscheinlich) obsolet!
     */

    public void searchButtonPressed(){

    }

    /**
     * Enables/ disables edit
     */

    public void editSwitchPressed (){
        /*
        //Toggle Edit (does not work, what so ever)
        final ToggleGroup editGroup = new ToggleGroup();
        editGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue.isSelected()==true){
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(true); //Makes selectColumn invisible by default !!
                }
                else {
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(false); //Makes selectColumn invisible by default !!
                }
            }
        });
*/
    }

    /**
     * delete vocabulary
     */
    public void deleteSelected (){
        ObservableList<VocabList> vocabListsRemove = FXCollections.observableArrayList();

        for(VocabList voc : list) {
            if (voc.getSelect()==true) {
                vocabListsRemove.add(voc);
            }
        }
        list.removeAll(vocabListsRemove);
        //TODO API CALL über postToVoc
        }


    /**
     * Filters after some arguments
     */
    public void filterButtonPressed() {
        //TODO implement
    }

    /**
     *Changes the language when changes are applied at Settings menu
     *    public VBox VBoxMain, VBoxMenu, VBoxMenuFrameLeft, VBoxMenuFrameRight, VBoxList;
     *     @FXML
     *     public HBox HBoxMenuFrame;
     *     @FXML
     *     public TextField SearchBarTextField;
     *     @FXML
     *     public Button  SearchButton,AddButton,DeleteButton;
     *     @FXML
     *     public ToggleButton EditSwitch;
     *     @FXML
     *     public ButtonBar ButtonBar;
     *     @FXML
     *     public TableView<VocabList> VocTableList;
     */
    public void setLang(){
        SearchBarTextField.setText(LocalizationManager.get("searchField"));
        SearchButton.setText(LocalizationManager.get("search"));
        AddButton.setText(LocalizationManager.get("add"));
        DeleteButton.setText(LocalizationManager.get("delete"));
        EditSwitch.setText(LocalizationManager.get("switch"));

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
