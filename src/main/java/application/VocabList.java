package application;

import java.util.ArrayList;

/**
 * gets the Vocabulary from the server and creates VocabList objects
 * @author Lukas Radermacher
 */
public class VocabList {
    int id;
    String question;
    String answer;
    String language;
    int phase;
    private boolean select;

    /**
     * Creates a new VocabList object
     *
     * @param id Vocabulary id
     * @param answer
     * @param question
     * @param language
     * @param phase phase of the vocabulary
     * @param select state of checkbox
     */
    public VocabList() {
    }

    public VocabList(int id, String answer, String question, String language, int phase, boolean select){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
        this.select = select;
    }

    public VocabList(int id, String answer, String question, String language, int phase){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
    }
    public VocabList(String answer, String question, String language, int phase){
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
    }
    //Get data

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getLanguage() {
        return language;
    }

    public int getPhase() {
        return phase;
    }

    public boolean isSelect() {
        return select;
    }

    //Set data


    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

}
