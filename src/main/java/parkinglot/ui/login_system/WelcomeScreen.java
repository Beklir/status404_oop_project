package parkinglot.ui.login_system;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import parkinglot.ui.ParkingLotDashboard;

import java.util.Objects;

public class WelcomeScreen {

    private final Stage stage;
    private final String username;

    public WelcomeScreen(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
    }

    public void show() {
        stage.setTitle("Parking Lot System");

        Label welcomeLabel = new Label("Welcome!");
        welcomeLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 60));

        Label usernameRow = new Label("Logged in as: " + username);
        usernameRow.setFont(Font.font("Times New Roman", 40));

        Button openDashboardButton = new Button("Open Parking Dashboard");
        Button logOutButton = new Button("Log Out");

        VBox infoCard = new VBox(20, welcomeLabel, usernameRow, openDashboardButton, logOutButton);
        infoCard.setAlignment(Pos.CENTER);
        infoCard.setPadding(new Insets(16, 20, 16, 20));

        StackPane root = new StackPane(infoCard);
        root.setPadding(new Insets(16));

        Scene scene = new Scene(root,460,440);

        Image icon = new  Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/parking_icon.png")));

        stage.getIcons().setAll(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

//        --------------Controls------------

        openDashboardButton.setOnAction(e -> new ParkingLotDashboard(stage, username).show());

        logOutButton.setOnAction(e -> {new LoginWindow(stage).show();});
    }
}
