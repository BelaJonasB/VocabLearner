package application;

import animatefx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

/**
 * @author Maximilian Engelmann
 */
public class ControllerLearning extends AnchorPane implements Initializable {

    @FXML
    private Label SelectedVocable, selectedVocable, UserErrors, userErrors, UserTranslation, UserScored, userScored, SelectedVocableTranslation, selectedVocableTranslation, Phase, phase,  UserScore, userScore, TestedVocables, testedVocables, CorrectVocables, correctVocables, AverageScored, averageScored, WrongVocables, wrongVocables, ScoreFinal, scoreFinal, PartCorrectVocables, partCorrectVocables, ErrorNoVocables;
    @FXML
    private HBox learningButtons, errorCorrectionBox, results, resultButtons, resultButtons2, errorNoVocables, errorButtons;
    @FXML
    private VBox mainLearning, normalLoad;
    @FXML
    private Button solveButton, nextButton, manualCorrectionButton, manualCorrectButton, manualPartlyCorrectButton, manualWrongButton, submitErrorsButton, showAllVocablesButton, hideAllVocablesButton, restartLearningButton, changeToVocabulary, changeToGoals;
    @FXML
    private TextField userTranslation, errorCorrection;
    @FXML
    private TableView<VocabList> allVocables;
    @FXML
    private AnchorPane base, inBase;
    @FXML
    private StackPane scoreCircle;
    @FXML
    private BorderPane toLoad;


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


    private final ControllerMainScene controllerMainScene; // for restartLearning and to get the vocables, that are selected for learning

    /**
     * constructor
     * @param controllerMainScene  mainController, where all other controllers and the vocables, that are selected for learning are referenced
     */
    public ControllerLearning(ControllerMainScene controllerMainScene)
    {
        this.controllerMainScene = controllerMainScene;
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
        setLang();
        if(Variables.getSelectedVocab().isEmpty()) {
            changeLearningScene(false, false, false, false, false, false,true,true);
            return;
        } else {
            list = Variables.getSelectedVocab();
        }
        changeLearningScene(true,true,false,false,false, false,false,false);


        base.prefHeightProperty().bind(Main.primarStage.heightProperty().subtract(255));
        inBase.prefHeightProperty().bind(base.prefHeightProperty());

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
     * sets the texts of the selected language for all UI elements
     */
    public void setLang(){
        SelectedVocable.setText(LocalizationManager.get("SelectedVocable"));
        UserErrors.setText(LocalizationManager.get("UserErrors"));
        UserTranslation.setText(LocalizationManager.get("UserTranslation"));
        UserScored.setText(LocalizationManager.get("UserScored"));
        SelectedVocableTranslation.setText(LocalizationManager.get("SelectedVocableTranslation"));
        Phase.setText(LocalizationManager.get("Phase"));
        UserScore.setText(LocalizationManager.get("UserScore"));
        manualCorrectionButton.setText(LocalizationManager.get("manualCorrectionButton"));
        manualCorrectButton.setText(LocalizationManager.get("manualCorrectButton"));
        manualPartlyCorrectButton.setText(LocalizationManager.get("manualPartlyCorrectButton"));
        manualWrongButton.setText(LocalizationManager.get("manualWrongButton"));
        errorCorrection.setPromptText(LocalizationManager.get("ErrorCorrection"));
        submitErrorsButton.setText(LocalizationManager.get("submitErrorsButton"));
        solveButton.setText(LocalizationManager.get("solveButton"));
        nextButton.setText(LocalizationManager.get("nextButton"));
        TestedVocables.setText(LocalizationManager.get("TestedVocables"));
        CorrectVocables.setText(LocalizationManager.get("CorrectVocables"));
        AverageScored.setText(LocalizationManager.get("AverageScored"));
        WrongVocables.setText(LocalizationManager.get("WrongVocables"));
        ScoreFinal.setText(LocalizationManager.get("ScoreFinal"));
        PartCorrectVocables.setText(LocalizationManager.get("PartCorrectVocables"));
        showAllVocablesButton.setText(LocalizationManager.get("showAllVocablesButton"));
        restartLearningButton.setText(LocalizationManager.get("restartLearningButton"));
        hideAllVocablesButton.setText(LocalizationManager.get("hideAllVocablesButton"));
        ErrorNoVocables.setText(LocalizationManager.get("ErrorNoVocables"));
        changeToVocabulary.setText(LocalizationManager.get("changeToVocabulary"));
        changeToGoals.setText(LocalizationManager.get("changeToGoals"));
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
        TableColumn<VocabList, Integer> numberColumn = new TableColumn<>(LocalizationManager.get("numberColumn"));
        TableColumn<VocabList, String> questionColumn  = new TableColumn<>(LocalizationManager.get("questionColumn"));
        TableColumn<VocabList, String> answerColumn = new TableColumn<>(LocalizationManager.get("answerColumn"));
        TableColumn<VocabList, String> userTranslationColumn = new TableColumn<>(LocalizationManager.get("userTranslationColumn"));
        TableColumn<VocabList, Integer> errorColumn = new TableColumn<>(LocalizationManager.get("errorColumn"));
        TableColumn<VocabList, Integer> newPhaseColumn = new TableColumn<>(LocalizationManager.get("newPhaseColumn"));

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("language1"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("language2"));
        userTranslationColumn.setCellValueFactory(new PropertyValueFactory<>("userTranslation"));
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("errors"));
        newPhaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));

        questionColumn.setStyle("-fx-alignment: center");
        answerColumn.setStyle("-fx-alignment: center");
        userTranslationColumn.setStyle("-fx-alignment: center");
        errorColumn.setStyle("-fx-alignment: center");
        newPhaseColumn.setStyle("-fx-alignment: center");

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
        controllerMainScene.gotoVocList();
    }

    /**
     *  from the Errorscreen switch to Goals
     */
    public void gotoGoals(){
        controllerMainScene.gotoGoals();
    }

    /**
    * start learning again, with the same vocables
     */
    public void restartLearning(){
        List<Vocab> vocabToLearn = Variables.getSelectedVocab();
        Collections.shuffle(vocabToLearn);
        controllerMainScene.gotoLearn(vocabToLearn);
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
        UserErrors.setVisible(false);
        UserScored.setVisible(false);
        SelectedVocableTranslation.setVisible(false);
        Phase.setVisible(false);
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
        UserErrors.setVisible(true);
        UserScored.setVisible(true);
        SelectedVocableTranslation.setVisible(true);
        Phase.setVisible(true);

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

    /**
    *  sets the results for a wrong answer
     */
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
     * sets the results for a completely correct answer
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
     * sets the results for a wrong answer manually
     */
    public void wrongAnswer(){
        if(manualCorrectionVisibility) {
            errorCorrectionBox.setVisible(true);
            errorCorrection.setText("");
        }
    }

    /**
     * sets the ammount of for a errors in an answer manually
     */
    public void submitErrors(){
        manualCorrectionVisibility = false;
        showManualCorrectionButtons();

        errors = Integer.parseInt(errorCorrection.getText());
        wrongAnswerGiven();
    }

    /**
     * toggles the buttons for the manual correction
     */
    public void manualCorrection(){
        manualCorrectionVisibility ^= true;
        showManualCorrectionButtons();
    }

    /**
     * sets the visibility of the buttons for the manual correction
     */
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
        if(scored>0) {
            new Pulse(scoreCircle).setSpeed(2.0).setCycleCount(2).play();
        } else {
            new Shake(scoreCircle).setCycleCount(1).play();
        }
        phase.setText(" " + (newPhase + 1) );
    }

    /**
     * saves results of the current vocable
     * increases counters for the result screen
     */
    public void saveCurrentResults(){
        ControllerLoading l = new ControllerLoading();
        l.changeSize(0,300, 0.0,100.0);
        toLoad.setCenter(l);

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
            new Thread(() -> {
                APICalls api = new APICalls();
                Vocab phaseOfcurrentVocable = new Vocab(currentVocable.id, currentVocable.answer, currentVocable.question, currentVocable.language, newPhase);
                api.editVoc(phaseOfcurrentVocable);
                Platform.runLater(() -> toLoad.setCenter(normalLoad));
            }).start();
        } else {
            toLoad.setCenter(normalLoad);
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
}