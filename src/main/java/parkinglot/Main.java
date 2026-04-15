package parkinglot;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Text name = new Text("Hello World!");

        VBox vBox = new VBox( name);
        vBox.setAlignment(Pos.CENTER);

        stage.setTitle("Hello");
        stage.setScene(new Scene(vBox, 300, 150));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
