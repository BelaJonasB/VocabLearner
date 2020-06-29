package application;


public class settingsChanger extends ControllerLogin {
        public settingsChanger() {
            try {
                changeScene("MainScene.fxml", 1080,620, true, false, Variables.getX(), Variables.getY());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
}