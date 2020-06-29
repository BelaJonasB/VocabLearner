package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Maximilian Engelmann
 */
public class ControllerLearning extends AnchorPane implements Initializable {

    @FXML
    private HBox mainLearning, learningButtons, results, resultButtons;
    @FXML
    private Button solveButton, nextButton, returnButton, showAllVocablesButton;
    @FXML
    private TextField selectedVocable, selectedVocableTranslation, userTranslation, userScore, userErrors, userScored, testedVocables, correctVocables, wrongVocables, partCorrectVocables, scoreFinal;

    private final Vocab one = new Vocab(1,"Reason","Grund","english",1);//for testing
    private final Vocab two = new Vocab(2,"LikyLiky","MögiMögi","english",1);//for testing
    private final Vocab three = new Vocab(3,"Learning","Lernen","english",1);//for testing

    private List<Vocab> list;
    private int currentVocIndex = -1;
    private Vocab currentVocable;

    private int score = 0;
    private int completelyCorrect = 0;
    private int partlyCorrect = 0;
    private int completelyWrong = 0;
    private int testedAmount = 0;

    public ControllerLearning()
    {
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
            list = List.of(one,two,three);
        } else {
            list = Variables.getSelectedVocab();
        }
        results.setVisible(false);
        resultButtons.setVisible(false);
        userTranslation.textProperty().addListener((observable, oldValue, newValue) -> {
            solveButton.setDisable(newValue.isEmpty());
        });
        nextVocable();
    }

    public void solveVocable(){
        nextButton.setVisible(true);
        solveButton.setDisable(true);
        userTranslation.setDisable(true);
        selectedVocableTranslation.setText((currentVocable.answer));
        compareAnswer();
    }

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

    public void returnToXYZ(){

    }

    public void showAllVocables(){

    }

    public void compareAnswer(){
        String translation = userTranslation.getText();
        int scored = 0;

        int errors = calculate(translation,currentVocable.answer);


        if(errors == 0) {
            scored = 3;
            completelyCorrect++;
        }
        if(errors == 1) {
            scored = 1;
            partlyCorrect++;
        }
        else{
            completelyWrong++;
        }
        testedAmount++;

        score +=  scored;

        userErrors.setText(" " + errors);
        userScored.setText(" " + scored);
        userScore.setText(" " + score);
    }


    /**
     * Algorithmus for Levenshtein distance
     * @param x
     * @param y
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

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

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

    public void openResults(){
        mainLearning.setVisible(false);
        learningButtons.setVisible(false);
        results.setVisible(true);
        resultButtons.setVisible(true);

        testedVocables.setText("" + testedAmount);
        correctVocables.setText("" + completelyCorrect);
        partCorrectVocables.setText("" + partlyCorrect);
        wrongVocables.setText("" + completelyWrong);
        scoreFinal.setText("" + score);
    }
}
