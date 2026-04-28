package parkinglot;

import javafx.application.Application;
import javafx.stage.Stage;
import parkinglot.ui.login_system.LoginWindow;

// Start the Application from the Launcher class
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new LoginWindow(primaryStage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


