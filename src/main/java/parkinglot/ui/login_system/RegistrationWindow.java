package parkinglot.ui.login_system;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.IOException;


public class RegistrationWindow{

    private final Stage stage;

    public RegistrationWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Registration");

        Label registerLabel = new Label("Create an Account");
        registerLabel.setAlignment(Pos.CENTER);
        registerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        Label usernameLabel = new Label("Username");
        TextField usernameField = new TextField();

        Label emailLabel = new Label("Email");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();

        Label confirmPasswordLabel = new Label("Confirm Password");
        PasswordField confirmPasswordField = new PasswordField();

        Button registerButton = new Button("Register");
        registerButton.setPrefSize(100, 30);

        Hyperlink loginLink = new Hyperlink("← Back to Login");

        VBox formBox = new VBox(6,
                usernameLabel, usernameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                confirmPasswordLabel, confirmPasswordField
        );
        formBox.setPadding(new Insets(0, 0, 10, 0));

        VBox card = new VBox(16,
                registerLabel,
                formBox,
                registerButton,
                loginLink
        );
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(36, 40, 36, 40));

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(40));


        Scene scene = new Scene(root, 460, 440);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

//        -----------------Controls----------------


        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirm = confirmPasswordField.getText();

            boolean userExists = false;

            for (User user: Database.loadUsers()){
                if (user.username().equals(username)) {
                    userExists = true;
                    break;
                }
            }


            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                registerLabel.setText("Fields cannot be empty!");
            }else if (!password.equals(confirm)) {
                registerLabel.setText("Passwords do not match!");
            }else if (userExists){
                registerLabel.setText("Account already exists!");
            }else {
                try {
                    Database.saveUser(new User(username, password, email));
                    new LoginWindow(stage).show();
                } catch (IOException ex) {
                    registerLabel.setText("Error saving user.");
                }
            }
        });


        loginLink.setOnAction(e -> new LoginWindow(stage).show());
    }
}