package application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class VocabList {
    int number;
    String language1;
    String language2;
    int phase;

    public VocabList(){

    }
    public VocabList (int number, String language1, String language2, int phase){
        this.number = number;
        this.language1 = language1;
        this.language2 = language2;
        this.phase = phase;
    }

    //Get data
    public int getNumber() {
        return number;
    }

    public int getPhase() {
        return phase;
    }

    public String getLanguage1() {
        return language1;
    }

    public String getLanguage2() {
        return language2;
    }

    //Set data
    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    public void setLanguage2(String language2) {
        this.language2 = language2;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    // Liste
    public static ArrayList<VocabList> list = new ArrayList<VocabList>();


}
