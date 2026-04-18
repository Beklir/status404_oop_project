package parkinglot.ui.login_system;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;


public class LoginWindow {

    private final Stage stage;

    public LoginWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Login");

        Label loginLabel = new Label("Enter your Username and Password");
        loginLabel.setAlignment(Pos.CENTER);
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        Label usernameLabel = new Label("Username");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Log In");
        loginButton.setPrefSize(100, 30);

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register here");

        VBox formBox = new VBox(6,
                usernameLabel, usernameField,
                passwordLabel, passwordField
        );
        formBox.setPadding(new Insets(0, 0, 10, 0));

        VBox card = new VBox(16,
                loginLabel,
                formBox,
                loginButton,
                registerLink
        );
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(36, 40, 36, 40));

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(40));


        Scene scene = new Scene(root, 460, 440);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

//        ------------------Controls---------------------

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            boolean authenticated = false;

            for (User user: Database.loadUsers()){
                if (user.username().equals(username) && user.password().equals(password)) {
                    authenticated = true;
                    break;
                }
            }

            if (!username.isEmpty() && !password.isEmpty()) {
                if (authenticated) {
                    new WelcomeScreen(stage, username).show();
                } else {
                    loginLabel.setText("Incorrect username or password.");
                    loginLabel.setTextFill(Color.RED);
                }
            }
        });


        registerLink.setOnAction(e -> new RegistrationWindow(stage).show()); //
    }
}