package application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocalizationManager extends ControllerLogin{
    private static SupportedLanguage currentLanguage = SupportedLanguage.ENGLISH;//defaults to english
    private static final Map<SupportedLanguage, Map<String, String>> translations = new HashMap<>();
    private static Settings u;

    public static void Init() {
        //Initialize the strings.

        Map<String, String> english = new HashMap<>();
        Map<String, String> german = new HashMap<>();

        english.put("vocab", "Vocabulary");
        english.put("learn", "Learn");
        english.put("goals", "Goals");
        english.put("reg", "Register");
        english.put("login", "Login");
        english.put("wrongLogText", "Wrong eMail and/or Password");
        english.put("serverText", "Server error");
        english.put("validMail", "Not a valid eMail");
        english.put("wrongRegText", "EMail already registered");
        english.put("pwLabel", "Password:");
        english.put("remember", "Remember me");
        english.put("listSetts1", "Language:");
        english.put("apply", "Apply");
        english.put("setting", "Settings");
        //English text for VocList
        english.put("searchField","Enter search");
        english.put("search","Search");
        english.put("add","Add");
        english.put("delete","Delete");
        english.put("switch","Edit");

        german.put("vocab", "Vokabeln");
        german.put("learn", "Lernen");
        german.put("goals", "Ziele");
        german.put("reg", "Registrieren");
        german.put("login", "Login");
        german.put("wrongLogText", "Falsche Email und/oder Passwort");
        german.put("serverText", "Server Error");
        german.put("validMail", "Keine g\u00FCltige eMail");
        german.put("wrongRegText", "EMail bereits registriert");
        german.put("pwLabel", "Passwort:");
        german.put("remember", "Anmeldung merken");
        german.put("listSetts1", "Sprache:");
        german.put("apply", "Anwenden");
        german.put("setting", "Einstellungen");
        //German text for VocList
        german.put("searchField","Suche hier ein eingeben");
        german.put("search","Suchen");
        german.put("add","Hinzuf\u00FCgen");
        german.put("delete","L\u00F6schen");
        german.put("switch","Bearbeiten");



        translations.put(SupportedLanguage.ENGLISH, english);
        translations.put(SupportedLanguage.GERMAN, german);

        Gson g = new Gson();
        try {
            u = g.fromJson(new FileReader("src/main/resources/Settings.json"), Settings.class);
            currentLanguage = SupportedLanguage.valueOf(u.getLang());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String get(String key) {
        return translations.get(currentLanguage).get(key);
    }

    public static void setLanguage(SupportedLanguage language) throws IOException {
        currentLanguage = language;
        Gson g = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        u.setLang(language.toString());
        String s = g.toJson(u);
        FileWriter file = new FileWriter("src/main/resources/Settings.json");
        file.write(s);
        file.close();
    }

    public enum SupportedLanguage {
        ENGLISH, GERMAN
    }
}
