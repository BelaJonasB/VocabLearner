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

    static String ALLLANGLABLE = "All";   // label shown in dropdown-menu to select vocab from all languages
    private ObservableList<VocabSelection> shownVocabList; // local copy of the vocab from database with checkbox

    @FXML
    private Label title;
    @FXML
    private Button startLearningButton, startLearningRandom;
    @FXML
    private Button selectAllButton;
    @FXML
    private TableView<VocabSelection> vocabTableView;
    @FXML
    private ComboBox<String> languageFilterComboBox;
    @FXML
    private AnchorPane cont, mainGoals;

    private final ControllerLogin controllerLogin;          //Changed to call startLearning

    public ControllerGoals(ControllerLogin controllerLogin) {        //Changed to call startLearning
        this.controllerLogin = controllerLogin;                              //Changed to call startLearning

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

        setLang();

        AnchorPane.setTopAnchor(vocabTableView, 50.0);
        AnchorPane.setBottomAnchor(startLearningButton, 40.0);
        AnchorPane.setBottomAnchor(startLearningRandom, 40.0);

        TableColumn<VocabSelection, String> answerColumn = new TableColumn<>(LocalizationManager.get("answer"));
        TableColumn<VocabSelection, String> questionColumn = new TableColumn<>(LocalizationManager.get("question"));
        TableColumn<VocabSelection, String> langColumn = new TableColumn<VocabSelection, String>(LocalizationManager.get("language"));
        TableColumn<VocabSelection, Integer> phaseColumn = new TableColumn<>(LocalizationManager.get("phase"));
        TableColumn<VocabSelection, CheckBox> selectCol = new TableColumn<>(LocalizationManager.get("selectToLearn"));

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

        if(selectAllButton.getText().equals("Alle Ausw\u00E4hlen")){
            selectAllButton.setStyle("-fx-pref-width: 140px");
            AnchorPane.setLeftAnchor(selectAllButton, 421.0);
        }
        if(languageFilterComboBox.getPromptText().equals("Nach Sprache Filtern")) {
            languageFilterComboBox.setStyle("-fx-pref-width: 200px");
        }
    }

    /**
     * Starts loads learning scene and starts learning with the selected Vocab in order
     */
    public void startLearningPressed(){
        List<Vocab> vocabToLearn = getSelectedVocab();
        controllerLogin.gotoLearn(vocabToLearn);
    }

    /**
     * Starts loads learning scene and starts learning with the selected Vocab in random order
     */
    public void startLearningRandomPressed(){
        List<Vocab> vocabToLearn = getSelectedVocab();
        Collections.shuffle(vocabToLearn);
        controllerLogin.gotoLearn(vocabToLearn);
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
     * enables the user to deselect all vocabs
     */
    public void deselectAllButtonClicked(){
        for(VocabSelection v : shownVocabList){
            v.getSelectVocab().setSelected(false);
        }
    }

    /**
     * inits labels depending on set language
     */
    private void setLang() {
        ALLLANGLABLE = LocalizationManager.get("allLangLabel");
        startLearningButton.setText(LocalizationManager.get("startLearning"));
        startLearningRandom.setText(LocalizationManager.get("startLearningRand"));
        selectAllButton.setText(LocalizationManager.get("selectAll"));
        languageFilterComboBox.setPromptText(LocalizationManager.get("filterLang"));
    }

    /**
     * @return list of selected Vocab
     */
    private List<Vocab> getSelectedVocab(){
        List<Vocab> list = new ArrayList<Vocab>();
        for(VocabSelection v : shownVocabList){
            if(v.getSelectVocab().isSelected()){
                list.add(v);
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