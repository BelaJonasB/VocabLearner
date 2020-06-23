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

    static final String ALLLANGLABLE = "All";
    private ObservableList<VocabSelection> shownVocabList; // local copy of the vocab from database

    @FXML
    private Label title;
    @FXML
    private Button startLearningButton;
    @FXML
    private Button returnButton;
    @FXML
    private TableView<VocabSelection> vocabTableView;
    @FXML
    private ComboBox languageFilterComboBox;



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

        getData();

        answerColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, String>("answer"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, String>("question"));
        langColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, String>("language"));
        phaseColumn.setCellValueFactory(new PropertyValueFactory<VocabSelection, Integer>("phase"));
        selectCol.setCellValueFactory(new PropertyValueFactory<VocabSelection, CheckBox>("selectVocab"));

        vocabTableView.setItems(shownVocabList);

        List<String> s = new ArrayList<String>();
        s.add(ALLLANGLABLE);
        s.addAll(searchLanguages(shownVocabList));
        languageFilterComboBox.setItems(FXCollections.observableList(s));
    }

    public void startLearning(){
        System.out.println("Start Learning pressed!");
        List vocabToLearn = new ArrayList<Vocab>();
        for(VocabSelection v : shownVocabList){
            if(v.getSelectVocab().isSelected()){
                vocabToLearn.add(v);
                System.out.println(v);
            }
        }
        // TODO call Learning Frame with vocabToLearn
    }

    public void returnToMainMenu(){
        System.out.println("returnToMainMenu pressed!");
        // TODO implement
    }

    public void selectAllButtonClicked(){
        for(VocabSelection v : shownVocabList){
            v.getSelectVocab().setSelected(true);
        }
    }

    public void languageSelected(){
        String s = languageFilterComboBox.getSelectionModel().getSelectedItem().toString();
        filterVocabListByLang(s);
        vocabTableView.setItems(shownVocabList);
    }

    /**
     * inits the displayed vocab
     */
    private void getData(){
        // TODO pull from database

        // dummy List for testing
        shownVocabList = FXCollections.observableArrayList(
                new VocabSelection(1, "test answer", "quest", "ger", 5, true),
                new VocabSelection(1, "test answer1", "quest1", "ger", 3, true),
                new VocabSelection(1, "test anzwort", "quest", "eng", 4, true),
                new VocabSelection(1, "wort1", "frage", "eng", 1, true),
                new VocabSelection(1, "unnecessary", ";", "python", 2, true)
        );
    }

    /**
     * @returns the unique languages in the list as String
     */
    private List<String> searchLanguages(ObservableList<VocabSelection> list) {
        List<String> s = new ArrayList<String>();
        for(Vocab v : list){
            if(!(s.contains(v.getLanguage()))) s.add(v.getLanguage());
        }
        return s;
    }

    /**
     * filters shown vocab list to entries with the defined String.
     */
    private void filterVocabListByLang(String lang){
        getData();
        if(lang.equals(ALLLANGLABLE)) return;   // "all" selected, show all available vocab
        ObservableList<VocabSelection> tmp = FXCollections.observableArrayList();
        for(VocabSelection vocab : shownVocabList){
            if(vocab.getLanguage().equals(lang)) tmp.add(vocab);
        }
        this.shownVocabList = tmp;
        return;
    }
}