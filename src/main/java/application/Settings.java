package application;

/**
 * Class to construct Settings Json
 */
public class Settings {
    private String lang;
    private int rem;

    public Settings(String lang,int rem) {
        this.lang = lang;
        this.rem = rem;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getRem() {
        return rem;
    }

    public void setRem(int rem) {
        this.rem = rem;
    }
}
