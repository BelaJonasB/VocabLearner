package application;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerVocabAdd extends AnchorPane implements Initializable {
    @FXML
    private Label TopLabel, Lower1Label, Lower2Label, Lower3Label;
    @FXML
    private TextField AnswerTextField, QuestionTextField, LanguageTextField;
    @FXML
    private Button ConfirmButton, CancelButton;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //execute language selection
        LocalizationManager.Init();
        setLang();


    }

    /**
     * Returns the new generated vocabulary and add's it to the VocList
     */
    public void parseNewVoc (){
        if (AnswerTextField.getText() != null && QuestionTextField.getText() != null && LanguageTextField.getText() != null){
            int phase = 0;
            Vocab newVoc = new Vocab(AnswerTextField.getText(),QuestionTextField.getText(),LanguageTextField.getText(),phase);
            //APICalls.postToVoc(newVoc);
            System.out.println(newVoc);

        }
        AnswerTextField.clear();
        QuestionTextField.clear();
        LanguageTextField.clear();
    }
    /**
     * Closes the AddVoc stage -> returns to Vocab5 without changing
     */
    public void closeAddStage (){
        Stage Stage = (Stage) CancelButton.getScene().getWindow();
        Stage.close();
    }

    /**
     * Method for setting the language
     */
    public void setLang(){
        AnswerTextField.setPromptText(LocalizationManager.get("answerAdd"));
        QuestionTextField.setPromptText(LocalizationManager.get("questionAdd"));
        LanguageTextField.setPromptText(LocalizationManager.get("languageAdd"));
        ConfirmButton.setText(LocalizationManager.get("confirm"));
        CancelButton.setText(LocalizationManager.get("cancel"));

    }

}

