package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * handles the goal pane (used to select Vocabulary to learn)
 * @author Sebastian Rassmann
 */
public class ControllerGoals extends AnchorPane implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Button startLearningButton;
    @FXML
    private Button returnButton;
    @FXML
    private TableView<VocabSelection> vocabTableView;

    ObservableList<VocabSelection> list;

    public ControllerGoals() {
        FXMLLoader goGoals = new FXMLLoader(getClass().getResource("/Goals.fxml"));
        goGoals.setRoot(this);
        goGoals.setController(this);
        try {
            goGoals.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TableColumn answerColumn = new TableColumn("answer");
        TableColumn questionColumn = new TableColumn("question");
        TableColumn langColumn = new TableColumn("language");
        TableColumn phaseColumn = new TableColumn("phase");
        TableColumn selectCol = new TableColumn("Learn");


        vocabTableView.getColumns().addAll(answerColumn, questionColumn, langColumn, phaseColumn, selectCol);

        // dummy List for testing
        list = FXCollections.observableArrayList(
                new VocabSelection(1, "test answer", "quest", "ger", 5)
        );

        // TODO pull from Server

        answerColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, String>("answer"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, String>("question"));
        langColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, String>("language"));
        phaseColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, Integer>("phase"));
        selectCol.setCellValueFactory(new PropertyValueFactory<VocabSelection, CheckBox>("selectVocab"));

        vocabTableView.setItems(list);

    }

    public void startLearning(){
        System.out.println("Start Learning pressed!");
        List vocabToLearn = new ArrayList<Vocab>();
        for(VocabSelection v : list){
            if(v.getSelectVocab().isSelected()){
                vocabToLearn.add(v);
                System.out.println("Selected: ID #" + v.getId());
            }
        }
        // call Learning Frame with vocabToLearn
    }

    public void returnToMainMenu(){
        System.out.println("returnToMainMenu pressed!");
    }

}