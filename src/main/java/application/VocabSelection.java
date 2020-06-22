package application;

import javafx.scene.control.CheckBox;

/**
 * Class to represent the Vocab entries in a list with an additional checkbox.
 * NOTE: not compatible with the json parsed from the API.
 * @author Sebastian Rassmann
 */
public class VocabSelection extends Vocab {

    private CheckBox selectVocab;

    public VocabSelection(){
    }

    public VocabSelection(Vocab v, boolean selectVocab){
        super.answer = v.answer;
        super.id = v.id;
        super.language = v.language;
        super.phase = v.phase;
        super.question = v.question;
    }

    public VocabSelection(int id, String answer, String question, String language, int phase){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
        this.selectVocab = new CheckBox();
    }

    public VocabSelection(int id, String answer, String question, String language, int phase, boolean checkedState){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
        this.selectVocab = new CheckBox();
        this.getSelectVocab().setSelected(checkedState);
    }

    public CheckBox getSelectVocab(){
        return selectVocab;
    }

    public void setSelectVocab(CheckBox selectVocab) {
        this.selectVocab = selectVocab;
    }
}
