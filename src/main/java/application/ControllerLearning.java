package application;

import javafx.application.Platform;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

/**
 * @author Maximilian Engelmann
 */
public class ControllerLearning extends AnchorPane implements Initializable {

    @FXML
    private Label userScore, selectedVocableTranslation, selectedVocable, userScored,  userErrors, testedVocables, correctVocables, wrongVocables, partCorrectVocables, scoreFinal, averageScored;
    @FXML
    private HBox mainLearning, learningButtons, results, resultButtons, resultButtons2, errorNoVocables, errorButtons;
    @FXML
    private Button solveButton, nextButton, showAllVocablesButton, hideAllVocablesButton, restartLearningButton;
    @FXML
    private TextField userTranslation;
    @FXML
    private TableView<VocabList> allVocables;


    private List<Vocab> list;
    private List<VocabList> listTestedVocables = new ArrayList<>();
    private int currentVocIndex = -1;
    private Vocab currentVocable;

    private double score = 0;
    private int completelyCorrect = 0;
    private int partlyCorrect = 0;
    private int completelyWrong = 0;
    private int testedAmount = 0;


    private final ControllerLogin controllerLogin; //restartLearning

    public ControllerLearning(ControllerLogin controllerLogin)
    {
        this.controllerLogin = controllerLogin;
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
        if(Variables.getSelectedVocab().isEmpty()) {
            changeLearningScene(false, false, false, false, false, false,true,true);
            return;
        } else {
            list = Variables.getSelectedVocab();
        }
        changeLearningScene(true,true,false,false,false, false,false,false);

        userTranslation.textProperty().addListener((observable, oldValue, newValue) -> {
            solveButton.setDisable(newValue.isEmpty());
        });
        nextVocable();
    }

    /**
     * Hides different UI-Elements inorder to change Scenes
     * @param pMainLearning
     * @param pLearningButtons
     * @param pResults
     * @param pResultButtons
     * @param pAllVocables
     */
    public void changeLearningScene(boolean pMainLearning, boolean pLearningButtons, boolean pResults, boolean pResultButtons, boolean pAllVocables, boolean pResultButtons2, boolean pErrorButtons, boolean pErrorNoVocables){
        mainLearning.setVisible(pMainLearning);
        learningButtons.setVisible(pLearningButtons);
        results.setVisible(pResults);
        resultButtons.setVisible(pResultButtons);
        allVocables.setVisible(pAllVocables);
        resultButtons2.setVisible(pResultButtons2);
        errorNoVocables.setVisible(pErrorNoVocables);
        errorButtons.setVisible(pErrorButtons);
    }

    /**
    * switches to the result screen
     */
    public void openResults(){
        changeLearningScene(false,false,true,true,false, false,false,false);

        /*
          *  creates the Table to list all tested Vocables and their results in detail
         */
        TableColumn<VocabList, Integer> numberColumn = new TableColumn("Nummer");
        TableColumn<VocabList, String> questionColumn  = new TableColumn("Deutsch");
        TableColumn<VocabList, String> userTranslationColumn = new TableColumn("Antwort");
        TableColumn<VocabList, String> answerColumn = new TableColumn("Englisch");
        TableColumn<VocabList, Integer> errorColumn = new TableColumn("Fehler");

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("language1"));
        userTranslationColumn.setCellValueFactory(new PropertyValueFactory<>("language2"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("userTranslation"));
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));

        allVocables.setItems(FXCollections.observableList(listTestedVocables));
        allVocables.getColumns().addAll(numberColumn, questionColumn, userTranslationColumn, answerColumn, errorColumn);
        allVocables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        allVocables.setEditable(false);


        /*
         *  fills the textfields on the overall resultscreen with numbers and values
         */
        testedVocables.setText(String.valueOf(testedAmount));
        correctVocables.setText(String.valueOf(completelyCorrect));
        partCorrectVocables.setText(String.valueOf(partlyCorrect));
        wrongVocables.setText(String.valueOf(completelyWrong));
        scoreFinal.setText(String.valueOf(score));
        averageScored.setText(String.valueOf(score/testedAmount));
    }

    /**
     *  from the Errorscreen switch to Vocabulary
     */
    public void gotoVocabList(){
        controllerLogin.gotoVocList();
    }

    /**
     *  from the Errorscreen switch to Goals
     */
    public void gotoGoals(){
        controllerLogin.gotoGoals();
    }

    /**
    * start learning again, with the same vocables
     */
    public void restartLearning(){
        List<Vocab> vocabToLearn = Variables.getSelectedVocab();
        Collections.shuffle(vocabToLearn);
        controllerLogin.gotoLearn(vocabToLearn);
    }

    /**
     *  switches from the results overall to stats for every single vocable
     */
    public void showAllVocables(){
        changeLearningScene(false,false,false,false,true, true,false,false);
    }

    public void hideAllVocables(){
        changeLearningScene(false,false,true,true,false, false,false,false);
    }

    /**
     * shows the a new vocable that shall be translated
     */
    public void nextVocable(){
        currentVocIndex++;
        if (currentVocIndex == list.size()) {
            openResults();
            return;
        }

        nextButton.setVisible(false);
        userTranslation.setDisable(false);

        currentVocable = list.get(currentVocIndex);
        selectedVocable.setText(currentVocable.question);
        userTranslation.setText("");
        selectedVocableTranslation.setText(" ");
    }

    /**
     * shows the answer and result for the current vocable
     */
    public void solveVocable(){
        nextButton.setVisible(true);
        solveButton.setDisable(true);
        userTranslation.setDisable(true);
        selectedVocableTranslation.setText((currentVocable.answer));
        compareAnswer();
    }

    /**
     * compares the translation from the user with the answer of the asked vocable
     *  increases the counters for the result screen
     *  saves the result for the vocable
     *  changes the Phase
     */
    public void compareAnswer(){
        String translation = userTranslation.getText();
        int scored = 0;
        int newPhase = currentVocable.getPhase();

        System.out.println("Called for word: " + currentVocable);
        System.out.println("newPhase before: " + newPhase);
        /*
         * compares the translation from the user with the answer of the asked vocable
         */
        int errors = calculate(translation,currentVocable.answer);

        /*
         *  increases the counters for the result screen
         */
        if(errors == 0) {
            scored = 3;
            if(currentVocable.getPhase() < 4){
                newPhase++;
            }
            completelyCorrect++;
        }
        else if (errors == 1) {
                scored = 1;
                partlyCorrect++;
        } else {
                if(currentVocable.getPhase() > 0){
                    newPhase--;
                }
                completelyWrong++;

        }
        System.out.println("newPhase after: " + newPhase);
        testedAmount++;
        score +=  scored;

        /*
         *  saves the result for the vocable
         */
        listTestedVocables.add(new VocabList( testedAmount,  currentVocable.question, currentVocable.answer,
                translation, errors));

        userErrors.setText(" " + errors);
        userScored.setText(" " + scored);
        userScore.setText(" " + score);

        System.out.println("Change voc: " + currentVocable + " phase to " + newPhase);
        if(newPhase != currentVocable.getPhase()){ // update Vocab only if the phase changes
            System.out.println("really doing it!");
            APICalls api = new APICalls();
            Vocab phaseOfcurrentVocable = new Vocab(currentVocable.id, currentVocable.answer, currentVocable.question,
                    currentVocable.language, newPhase);
            api.editVoc(phaseOfcurrentVocable);
        }
    }

    /**
     * Algorithmus for Levenshtein distance
     * calculates, how many letters you have to change between two words, inorder for them to match exactly
     * @param x = translation (from User)
     * @param y = answer of the current Vocable
     * @return
     */
    static int calculate(String x, String y) {
        if (x.isEmpty()) {
            return y.length();
        }

        if (y.isEmpty()) {
            return x.length();
        }

        int substitution = calculate(x.substring(1), y.substring(1))
                + costOfSubstitution(x.charAt(0), y.charAt(0));
        int insertion = calculate(x, y.substring(1)) + 1;
        int deletion = calculate(x.substring(1), y) + 1;

        return min(substitution, insertion, deletion);
    }

    /**
     *Algorithmus nr.2 for Levenshtein distance
     * calculates, how many letters you have to change between two words, inorder for them to match exactly
     * @param a = char at "position" of translation (from User)
     * @param b = char at "position" of  answer of the current Vocable
     * @return if chars at "position" are equal or not
     */
    public static int costOfSubstitution(char a, char b)
    {
        return a == b ? 0 : 1;
    }

    /**
     *Algorithmus nr.3 for Levenshtein distance
     * calculates, how many letters you have to change between two words, inorder for them to match exactly
     * @param numbers ( int Array )
     * @return  the minimum of the listed numbers
     */
     public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }


    /**
     * Method to start Learning. Called when "start Learning" in Goal Scene is pressed. Starts Learning Scene
     * @param vocabToLearn list of Vocabs selected in screen in randomized order if wanted.
     */
    public void startLearning(List<Vocab> vocabToLearn) {
        list = vocabToLearn;
    }

    public TableView<VocabList> getVocTableList(){
        return allVocables;
    }
}