package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


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
    public TableView<Vocab> VocTableList;

    public ObservableList<Vocab> list;

    private final ControllerLogin controllerLogin;

    public ControllerVocList(ControllerLogin controllerLogin) {        //
        this.controllerLogin = controllerLogin;

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
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //execute language selection
        LocalizationManager.Init();
        setLang();

        //ID-Column stuff
        TableColumn <Vocab, Integer> idColumn = new TableColumn(LocalizationManager.get("id"));
        idColumn.setMinWidth(40);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Answer-Column stuff
        TableColumn <Vocab, String> answerColumn  = new TableColumn(LocalizationManager.get("answer"));
        answerColumn.setMinWidth(80);
        answerColumn.setCellValueFactory(
                new PropertyValueFactory<>("answer"));
        answerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        answerColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vocab, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Vocab, String> str) {
                (str.getTableView().getItems().get(
                        str.getTablePosition().getRow())
                ).setAnswer(str.getNewValue());
            }

        });


        //Question-Column stuff
        TableColumn <Vocab, String> questionColumn = new TableColumn(LocalizationManager.get("question"));
        questionColumn.setMinWidth(80);
        questionColumn.setCellValueFactory(
                new PropertyValueFactory<>("question"));
        questionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        questionColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vocab, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Vocab, String> str) {
                (str.getTableView().getItems().get(
                        str.getTablePosition().getRow())
                ).setQuestion(str.getNewValue());
            }

        });

        //Language-Column stuff
        TableColumn <Vocab, String> languageColumn = new TableColumn<>(LocalizationManager.get("language"));
        languageColumn.setMinWidth(80);
        languageColumn.setCellValueFactory(
                new PropertyValueFactory<>("language"));
        languageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        languageColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vocab, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Vocab, String> str) {
                (str.getTableView().getItems().get(
                        str.getTablePosition().getRow())
                ).setLanguage(str.getNewValue());
            }

        });


        //Phase-Column stuff
        TableColumn <Vocab, Integer> phaseColumn = new TableColumn(LocalizationManager.get("phase"));
        phaseColumn.setMinWidth(40);
        phaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));


        //Select-Column stuff
        TableColumn <Vocab, Boolean> selectColumn = new TableColumn(LocalizationManager.get("select"));
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        //selectColumn.setEditable(false); //Makes selectColumn not editable
        //selectColumn.setVisible(false); //Makes selectColumn invisible by default



        //Get Data from list
        list = getVocList();

        //Set Data to list and add Columns
        VocTableList.getColumns().addAll(idColumn, answerColumn, questionColumn,languageColumn, phaseColumn, selectColumn);
        VocTableList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getData();

        VocTableList.setItems(list);

        VocTableList.setEditable(true);  //ENABLED FOR TESTING

       /*
        //Toggle Edit (does not work, what so ever)
        final ToggleGroup editGroup = new ToggleGroup();
        editGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue.isSelected()==true){
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(true); //Makes selectColumn visible
                }
                else {
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(false); //Makes selectColumn invisible
                }
            }
        });
*/
    }

    public void textEntered() {
        String input = SearchBarTextField.getText();
        list = getVocList();
        if (input == null || input.isEmpty()) {
            VocTableList.setItems(list);
            return;
        }
        String lowerCaseFilter = input.toLowerCase();
        ObservableList<Vocab> tmp = FXCollections.observableArrayList();

        for (Vocab voc : list) {

            if (voc.getAnswer().toLowerCase().contains(lowerCaseFilter)) {
                tmp.add(voc);
                System.out.println("test"+tmp.toString() );
            }
            else if (voc.getQuestion().toLowerCase().contains(lowerCaseFilter)) {
                tmp.add(voc);
                System.out.println("test2"+tmp.toString() );
            }
        }
        list = tmp;
        System.out.println("erg"+ list);
        VocTableList.setItems(list);
    }
//TODO empty search field when clicked at
    /**
     * When called, starts searching for the String entered in SearchBarTextField (Wahrscheinlich) obsolet!
     */

    public void searchButtonPressed(){

    }

    /**
     * When called, opens a new window an asks for new Vocabulary
     */
    public void addButtonPressed() {
        try {
            Stage addStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/AddVoc.fxml"));

            Scene scene = new Scene(root, 600, 400);
            addStage.setTitle(LocalizationManager.get("addVoc"));
            addStage.setResizable(false);
            addStage.centerOnScreen();
            addStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo-512.png")));
            addStage.setScene(scene);
            addStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (newValue.isSelected()){
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(true); //Makes selectColumn visible
                }
                else {
                    //selectColumn.setEditable(false); //Makes selectColumn not editable
                    selectColumn.setVisible(false); //Makes selectColumn invisible
                }
            }
        });
*/
    }

    /**
     * delete vocabulary
     */

    public void deleteSelectedVoc (){
        for (Vocab voc : VocTableList.getItems()){
            if (voc.isSelect()){
                VocTableList.getItems().remove(voc);
            }
            //TODO API CALL über postToVoc
        }
    }

    /**
     * Filters after some arguments
     */
    public void filterButtonPressed() {
        //TODO implement
    }


    public void setLang(){
        SearchBarTextField.setPromptText(LocalizationManager.get("searchField"));
        SearchButton.setText(LocalizationManager.get("search"));
        AddButton.setText(LocalizationManager.get("add"));
        DeleteButton.setText(LocalizationManager.get("delete"));
        EditSwitch.setText(LocalizationManager.get("switch"));


    }

    //Temp data for testing
    private  ObservableList<Vocab> getVocList () {
        Vocab voc0 = new Vocab(0, "Folter", "torture","Deutsch", 0,true);
        Vocab voc1 = new Vocab(1, "Schmerz", "pain","Deutsch", 0);
        Vocab voc2 = new Vocab(2, "Kuh", "cow","Deutsch", 0);

        ObservableList<Vocab> vocList = FXCollections.observableArrayList(voc0,voc1, voc2);
        return vocList;
    }

    private void getData(){
        for(Vocab v : Variables.getUsersVocab()){

            list.add(new Vocab(v.getId(),v.getAnswer(),v.getQuestion(),v.getLanguage(),v.getPhase()));
            //list.add(new Vocab(0, "Folter", "torture","Deutsch", 0,true));
            //list.add(new Vocab(1, "Schmerz", "pain","Deutsch", 0));
            //list.add(new Vocab(2, "Kuh", "cow","Deutsch", 0));

        }
        //list = FXCollections.observableList(list);
    }
}
