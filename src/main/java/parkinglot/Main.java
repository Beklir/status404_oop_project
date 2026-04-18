package parkinglot;

import javafx.application.Application;
import javafx.stage.Stage;
import parkinglot.ui.login_system.LoginWindow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new LoginWindow(primaryStage).show();
    }

}


