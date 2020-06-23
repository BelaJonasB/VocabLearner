package application;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a JSON vocabulary entry from the server
 * @author Sebastian Rassmann
 */
public class Vocab {

    int id;
    String answer;
    String question;
    String language;
    int phase;

    /**
     * Creates new Vocab entry
     */
    public Vocab (){

    }

    /**
     * Creates new Vocab entry
     *
     * @param id Vocab id in list
     * @param answer
     * @param question
     * @param language
     * @param phase
     */
    public Vocab(int id, String answer, String question, String language, int phase){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    /**
     * inits Vocab entry from Gson
     * @param gson Gson as String
     * @return
     */
    public Vocab fromGson(String gson){
        // TODO implement
        return new Vocab();
    }

    /**
     * pares entire Vocab List from Gson. Can be used the retract the date from the API.
     * @return
     */
    public static List<Vocab> parseVocabListFromGson(String gson){
        return new ArrayList<Vocab>();
    }

    public String toString(){
        return ("ID# " + this.id + ": " + this.question + " : " + this.answer + " in " + this.language +
                ", phase " + this.phase);
    }
}
