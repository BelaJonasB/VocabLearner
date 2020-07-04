package application;

/**
 * Class to make "Apply" in settings work
 */
public class settingsChanger extends ControllerLogin {
        public settingsChanger() {
            try {
                changeScene("MainScene.fxml", 1080,720, true, false, Variables.getX(), Variables.getY());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
}