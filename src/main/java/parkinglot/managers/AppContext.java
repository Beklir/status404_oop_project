package parkinglot.managers;

import javafx.stage.Stage;

public class AppContext {
    public final Stage stage;
    public final APIManager apiManager = new APIManager();

    public AppContext(Stage stage) {
        this.stage = stage;
    }
}
