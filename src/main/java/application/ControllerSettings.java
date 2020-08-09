package application;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for the Controller of the Settings Window
 */
public class ControllerSettings extends AnchorPane implements Initializable {
    public ControllerSettings() {
        FXMLLoader goSett = new FXMLLoader(getClass().getResource("/Settings.fxml"));
        goSett.setRoot(this);
        goSett.setController(this);
        try {
            goSett.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    ComboBox<String> langDD;
    @FXML
    Label listSetts1,warn,settsHeader1,settsHeader2,listSetts3,oldPw,newPw,newPwConf;
    @FXML
    Button apply,pw, applyPwChange,cancelPwChange;
    @FXML
    AnchorPane settingsTable1,settingsTable2,settingsTable3;
    @FXML
    TextField email;
    @FXML
    BorderPane pwChangeWind;
    @FXML
    VBox pwMain;
    @FXML
    PasswordField oldPwText,newPwText,newPwConfText;

    String warnEmpty,warnWrongOld,warnWrongNew;

    Crypt c;

    /**
     * Init the default Settings and Tools to edit them
     */
    public void initialize(URL location, ResourceBundle resources) {
        ControllerLoading l = new ControllerLoading();
        l.changeSize(410,700,180.0,250.0);
        pwChangeWind.setCenter(l);

        applyPwChange.setDisable(true);
        warn.setVisible(false);

        new Thread(() -> {
            //Set selected Language to the selected item in dropdown
            try {
                String mac = "DiddiKong"; //Default, if no mac adress
                try {
                    NetworkInterface net = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
                    mac = new String(net.getHardwareAddress());
                } catch (SocketException | UnknownHostException e) {
                    System.out.println("No Mac-Address found");
                }
                c = new Crypt(mac);

                Gson g = new Gson();
                Settings s = g.fromJson(new FileReader("src/main/resources/Settings.json"), Settings.class);
                String lang = s.getLang().toLowerCase();
                char ch = lang.charAt(0);
                ch = Character.toUpperCase(ch);
                char finalC = ch;
                Platform.runLater(() -> {
                    langDD.getSelectionModel().select(lang.replaceFirst(String.valueOf(lang.charAt(0)), String.valueOf(finalC)));
                });
            } catch (Exception e){
                e.printStackTrace();
            }

            String mail = "";
            Gson g = new Gson();
            try {
                User u = g.fromJson(new FileReader("src/main/resources/logInfo.json"), User.class);
                mail = u.getEmail();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String finalMail = mail;
            Platform.runLater(() -> {
                email.setText(finalMail);
                pwChangeWind.setCenter(pwMain);
                pwChangeWind.setVisible(false);
            });
        }).start();


        LocalizationManager.Init();
        setLang();
        langDD.getItems().addAll("English", "German");

        buttonFeedback(apply,"14");
        buttonFeedback(pw,"15");
        buttonFeedback(applyPwChange,"14");
        buttonFeedback(cancelPwChange, "14");

        settingsTable1.prefWidth(500.0);
        settingsTable2.prefWidth(500.0);
        settingsTable3.prefWidth(500.0);

        Pattern p = Pattern.compile("\\s");
        newPwText.textProperty().addListener((observable, oldValue, newValue) -> {
            Matcher m = p.matcher(newValue);
            if(m.matches()||newValue.equals("")) {
                applyPwChange.setDisable(true);
                warn.setText(warnEmpty);
                warn.setVisible(true);
            } else {
                if(newValue.equals(newPwConfText.textProperty().get())) {
                    applyPwChange.setDisable(false);
                    warn.setVisible(false);
                } else {
                    applyPwChange.setDisable(true);
                    warn.setText(warnWrongNew);
                    warn.setVisible(true);
                    newPwConfText.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                        if(newValue.equals(newValue1)) {
                            applyPwChange.setDisable(false);
                            warn.setVisible(false);
                        } else {
                            applyPwChange.setDisable(true);
                            warn.setText(warnWrongNew);
                            warn.setVisible(true);
                        }
                    });
                }
            }
        });
    }

    /**
     * Apply changes and reload Scene
     */
    public void apply() {
        try {
            LocalizationManager.setLanguage(LocalizationManager.SupportedLanguage.valueOf(langDD.getSelectionModel().getSelectedItem().toUpperCase()));

            APICalls a = new APICalls();
            a.changeLog(email.getText(),"0");
            Variables.setMail(email.getText());

            User u = new Gson().fromJson(new FileReader("src/main/resources/logInfo.json"), User.class);

            u = new User(email.getText(),u.getPassword());
            Gson g = new Gson();
            String s = g.toJson(u);
            FileWriter file = new FileWriter("src/main/resources/logInfo.json");
            file.write(s);
            file.close();


            //refresh Main Window
            new SettingsChanger();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void pwChange() {
        pwChangeWind.setVisible(true);
    }
    public void cancel() {
        pwChangeWind.setVisible(false);
        oldPwText.setText("");
        newPwText.setText("");
        newPwConfText.setText("");
        warn.setVisible(false);
    }
    public void confirm() {
        try {
            Gson g = new Gson();
            User u = g.fromJson(new FileReader("src/main/resources/logInfo.json"), User.class);
            String pw = c.decrypt(u.getPassword());
            if(pw.equals(oldPwText.textProperty().get())) {
                APICalls a = new APICalls();
                a.changeLog("0",newPwText.textProperty().get());

                User u2 = new User(Variables.getMail(),c.encrypt(newPwText.textProperty().get()));
                String s = g.toJson(u2);
                FileWriter file = new FileWriter("src/main/resources/logInfo.json");
                file.write(s);
                file.close();

                //refresh Main Window
                new SettingsChanger();
            } else {
                warn.setText(warnWrongOld);
                warn.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the Strings for Language of Program
     */
    public void setLang() {
        listSetts1.setText(LocalizationManager.get("listSetts1"));
        apply.setText(LocalizationManager.get("apply"));
        settsHeader1.setText(LocalizationManager.get("settsHeader1"));
        settsHeader2.setText(LocalizationManager.get("settsHeader2"));
        listSetts3.setText(LocalizationManager.get("listSetts3"));
        oldPw.setText(LocalizationManager.get("oldPw"));
        newPw.setText(LocalizationManager.get("newPw"));
        newPwConf.setText(LocalizationManager.get("newPwConf"));
        cancelPwChange.setText(LocalizationManager.get("cancelPwChange"));
        applyPwChange.setText(LocalizationManager.get("applyPwChange"));
        pw.setText(LocalizationManager.get("pw"));
        warnEmpty=LocalizationManager.get("warnEmpty");
        warnWrongOld=LocalizationManager.get("warnWrongOld");
        warnWrongNew=LocalizationManager.get("warnWrongNew");
    }
    public void buttonFeedback(Button b,String size) {
        b.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                b.setStyle("-fx-font-size: "+size+"; -fx-padding: 4 0 "+ (Integer.parseInt(size) - 10) +" 0");
            } else {
                b.setStyle("");
            }
        });
    }
}
