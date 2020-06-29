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
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * handles the goal pane (used to select Vocabulary to learn)
 * @author Sebastian Rassmann
 */
public class ControllerGoals extends AnchorPane implements Initializable {

    static final String ALLLANGLABLE = "All";   // label shown in dropdown-menu to select vocab from all languages
    private ObservableList<VocabSelection> shownVocabList; // local copy of the vocab from database with checkbox

    @FXML
    private Label title;
    @FXML
    private Button startLearningButton, startLearningRandom;
    @FXML
    private Button returnButton, selectAllButton;
    @FXML
    private TableView<VocabSelection> vocabTableView;
    @FXML
    private ComboBox<String> languageFilterComboBox;
    @FXML
    private AnchorPane cont, mainGoals;

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
        AnchorPane.setTopAnchor(vocabTableView, 50.0);
        AnchorPane.setBottomAnchor(startLearningButton, 40.0);
        AnchorPane.setBottomAnchor(startLearningRandom, 40.0);

        TableColumn<VocabSelection, String> answerColumn = new TableColumn<>("answer");
        TableColumn<VocabSelection, String> questionColumn = new TableColumn<>("question");
        TableColumn<VocabSelection, String> langColumn = new TableColumn<VocabSelection, String>("language");
        TableColumn<VocabSelection, Integer> phaseColumn = new TableColumn<>("phase");
        TableColumn<VocabSelection, CheckBox> selectCol = new TableColumn<>("Learn");

        phaseColumn.setId("phase");
        selectCol.setId("select");
        phaseColumn.setStyle("-fx-alignment: center");
        selectCol.setStyle("-fx-alignment: center");

        vocabTableView.getColumns().addAll(answerColumn, questionColumn, langColumn, phaseColumn, selectCol);
        vocabTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getData();

        answerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        langColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        phaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));
        selectCol.setCellValueFactory(new PropertyValueFactory<>("selectVocab"));

        vocabTableView.setItems(shownVocabList);

        List<String> s = new ArrayList<String>();
        s.add(ALLLANGLABLE);
        s.addAll(searchLanguages(shownVocabList));
        languageFilterComboBox.setItems(FXCollections.observableList(s));
    }

    /**
     * Starts loads learning scene and starts learning with the selected Vocab in order
     */
    public void startLearningPressed(){
        System.out.println("Start Learning pressed!");
        List<Vocab> vocabToLearn = getSelectedVocab();
        ControllerLearning.startLearning(vocabToLearn);
    }

    /**
     * Starts loads learning scene and starts learning with the selected Vocab in random order
     */
    public void startLearningRandomPressed(){
        System.out.println("Start Learning random order pressed!");
        List<Vocab> vocabToLearn = getSelectedVocab();
        Collections.shuffle(vocabToLearn);
        ControllerLearning.startLearning(vocabToLearn);
    }

    /**
     * updates the screen after vocabs are filtered by languages (or all)
     */
    public void languageSelected(){
        String s = languageFilterComboBox.getSelectionModel().getSelectedItem().toString();
        filterVocabListByLang(s);
        vocabTableView.setItems(shownVocabList);
    }

    /**
     * enables the user to select all vocabs
     */
    public void selectAllButtonClicked(){
        for(VocabSelection v : shownVocabList){
            v.getSelectVocab().setSelected(true);
        }
    }

    /**
     * @return list of selected Vocab
     */
    private List<Vocab> getSelectedVocab(){
        List<Vocab> list = new ArrayList<Vocab>();
        for(VocabSelection v : shownVocabList){
            if(v.getSelectVocab().isSelected()){
                list.add(v);
                System.out.println(v);
            }
        }
        return list;
    }

    /**
     * Access pulled list of vocab from the API and inits the list of shown vocabs.
     * Only shows Vocab of phase 1-4
     */
    private void getData(){

        ArrayList<VocabSelection> tmp = new ArrayList<>();
        for(Vocab v : Variables.getUsersVocab()){
            if(v.getPhase() < 5){
                tmp.add(new VocabSelection(v, true));
            }
        }
        shownVocabList = FXCollections.observableList(tmp);
//
//        // dummy List for testing
//        shownVocabList = FXCollections.observableArrayList(
//                new VocabSelection(1, "test answer", "quest", "ger", 5, true),
//                new VocabSelection(1, "test answer1", "quest1", "ger", 3, true),
//                new VocabSelection(1, "test anzwort", "quest", "eng", 4, true),
//                new VocabSelection(1, "wort1", "frage", "eng", 1, true),
//                new VocabSelection(1, "unnecessary", ";", "python", 2, true)
//        );
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
     * filters shown vocab list to entries with the defined Language as String
     */
    private void filterVocabListByLang(String lang){
        getData();
        if(lang.equals(ALLLANGLABLE)) return;   // "all" selected, show all available vocab
        ObservableList<VocabSelection> tmp = FXCollections.observableArrayList();
        for(VocabSelection vocab : shownVocabList){
            if(vocab.getLanguage().equals(lang)) tmp.add(vocab);
        }
        this.shownVocabList = tmp;
    }
}