package application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
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
    private HBox mainLearning, learningButtons, errorCorrectionBox, results, resultButtons, resultButtons2, errorNoVocables, errorButtons;
    @FXML
    private Button solveButton, nextButton, manualCorrectionButton, manualCorrectButton, manualPartlyCorrectButton, manualWrongButton, submitErrorsButton, showAllVocablesButton, hideAllVocablesButton, restartLearningButton;
    @FXML
    private TextField userTranslation, phase, errorCorrection;
    @FXML
    private TableView<VocabList> allVocables;


    private List<Vocab> list;
    private List<VocabList> listTestedVocables = new ArrayList<>();
    private int currentVocIndex = 0;
    private Vocab currentVocable;

    private double score = 0;
    private int completelyCorrect = 0;
    private int partlyCorrect = 0;
    private int completelyWrong = 0;
    private int testedAmount = 0;
    private int scored;
    private int newPhase;
    private int errors;

    private boolean manualCorrectionVisibility = false;


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

    /**
     * adds a few listeners for textfields, inorder to enable some buttons
     * setup for the scenes and learning-scene
     * @param url
     * @param resourceBundle
     */
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
        errorCorrection.textProperty().addListener((observable, oldValue, newValue) -> {
            submitErrorsButton.setDisable(newValue.isEmpty());
        });
        errorCorrection.textProperty().addListener(new ChangeListener<String>() {
                                                       @Override
                                                       public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                                           if (!newValue.matches("\\d*")) {
                                                               errorCorrection.setText(newValue.replaceAll("[^\\d]", ""));
                                                           }
                                                       }
                                                   }
        );
        changeToNextVocable();
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
        TableColumn<VocabList, Integer> numberColumn = new TableColumn("No.");
        TableColumn<VocabList, String> questionColumn  = new TableColumn("1. Language");
        TableColumn<VocabList, String> answerColumn = new TableColumn("2. Language");
        TableColumn<VocabList, String> userTranslationColumn = new TableColumn("Your Answer");
        TableColumn<VocabList, Integer> errorColumn = new TableColumn("Mistakes");
        TableColumn<VocabList, Integer> newPhaseColumn = new TableColumn("new Phase");

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("language1"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("language2"));
        userTranslationColumn.setCellValueFactory(new PropertyValueFactory<>("userTranslation"));
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("errors"));
        newPhaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));

        allVocables.setItems(FXCollections.observableList(listTestedVocables));
        allVocables.getColumns().addAll(numberColumn, questionColumn, answerColumn, userTranslationColumn, errorColumn, newPhaseColumn);
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
        saveCurrentResults();

        currentVocIndex++;
        if (currentVocIndex == list.size()) {
            openResults();
            return;
        }
        changeToNextVocable();
    }

    /**
     * changes the scene to displays the next vocable
     */
    public void changeToNextVocable(){
        nextButton.setVisible(false);
        manualCorrectionButton.setVisible(false);
        userTranslation.setDisable(false);

        currentVocable = list.get(currentVocIndex);
        selectedVocable.setText(currentVocable.question);
        userTranslation.setText("");
        userErrors.setText("");
        userScored.setText("");
        phase.setText("");
        selectedVocableTranslation.setText("");

        manualCorrectionButton.setVisible(false);
        manualCorrectButton.setVisible(false);
        manualPartlyCorrectButton.setVisible(false);
        manualWrongButton.setVisible(false);
        errorCorrectionBox.setVisible(false);
    }

    /**
     * shows the answer and result for the current vocable
     */
    public void solveVocable(){
        nextButton.setVisible(true);
        manualCorrectionButton.setVisible(true);
        //solveButton.setDisable(true);
        userTranslation.setDisable(true);

        selectedVocableTranslation.setText((currentVocable.answer));
        compareAnswer();
    }

    /**
     * compares the translation from the user with the answer of the asked vocable
     *  changes the Phase
     */
    public void compareAnswer(){
        /*
         * compares the translation from the user with the answer of the asked vocable
         * uses the Levenshtein algorithm
         */
        errors = calculate(userTranslation.getText(),currentVocable.answer);

        /*
         *  checks if the Vocable is either correct, partly correct or wrong
         */
        if(errors == 0) {
            correctAnswer();
        }
        else {
            if (errors == 1) {
                partlyCorrectAnswer();
            }
            else {
                wrongAnswerGiven();
            }
        }
    }

    public void wrongAnswerGiven(){
        if(currentVocable.phase != 0) {
            newPhase = currentVocable.phase - 1;
        }
        else{
            newPhase = 0;
        }
        scored = 0;
        showCurrentResults();
    }

    /**
     *sets the results for a completely correct answer
     */
    public void correctAnswer(){
        manualCorrectionVisibility = false;
        showManualCorrectionButtons();

        if(currentVocable.phase != 4) {
            newPhase = currentVocable.phase + 1;
        }
        scored = 3;
        errors = 0;
        showCurrentResults();
    }

    /**
     *sets the results for a partly correct answer
     */
    public void partlyCorrectAnswer(){
        manualCorrectionVisibility = false;
        showManualCorrectionButtons();

        newPhase = currentVocable.phase;
        scored = 1;
        errors = 1;
        showCurrentResults();
    }

    /**
     * sets the results for a wrong answer
     */
    public void wrongAnswer(){
        if(manualCorrectionVisibility) {
            errorCorrectionBox.setVisible(true);
            errorCorrection.setText("");
        }
    }

    public void submitErrors(){
        manualCorrectionVisibility = false;
        showManualCorrectionButtons();

        errors = Integer.parseInt(errorCorrection.getText());
        wrongAnswerGiven();
    }

    public void manualCorrection(){
        manualCorrectionVisibility ^= true;
        showManualCorrectionButtons();
    }

    public void showManualCorrectionButtons(){
        manualCorrectButton.setVisible(manualCorrectionVisibility);
        manualPartlyCorrectButton.setVisible(manualCorrectionVisibility);
        manualWrongButton.setVisible(manualCorrectionVisibility);
        manualCorrectButton.setDisable(false);
        manualPartlyCorrectButton.setDisable(false);
        manualWrongButton.setDisable(false);
        errorCorrectionBox.setVisible(false);
    }

    /**
     * shows results of the current vocable
     */
    public void showCurrentResults(){
        userErrors.setText(" " + errors);
        userScored.setText(" " + scored);
        userScore.setText(" " + (score + scored) );
        phase.setText(" " + (newPhase + 1) );
    }

    /**
     * saves results of the current vocable
     * increases counters for the result screen
     */
    public void saveCurrentResults(){
        if(errors == 0) {
            completelyCorrect++;
        }
        else {
            if (errors == 1) {
                partlyCorrect++;
            }
            else {
                completelyWrong++;
            }
        }
        testedAmount++;

        score +=  scored;

        listTestedVocables.add(new VocabList( testedAmount,  currentVocable.question, currentVocable.answer, userTranslation.getText(), errors, newPhase+1));

        if(newPhase != currentVocable.getPhase()) { // update Vocable only if the phase changes
            APICalls api = new APICalls();
            Vocab phaseOfcurrentVocable = new Vocab(currentVocable.id, currentVocable.answer, currentVocable.question, currentVocable.language, newPhase);
            api.editVoc(phaseOfcurrentVocable);
        }
    }

    /**
     * Algorithm for Levenshtein distance
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
     * Algorithm no.2 for Levenshtein distance
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
     * Algorithm no.3 for Levenshtein distance
     * calculates, how many letters you have to change between two words, inorder for them to match exactly
     * @param numbers ( int Array )
     * @return  the minimum of the listed numbers
     */
     public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }

    public TableView<VocabList> getVocTableList(){
        return allVocables;
    }
}