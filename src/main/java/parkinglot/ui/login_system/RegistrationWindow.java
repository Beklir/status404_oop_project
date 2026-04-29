package parkinglot.ui.login_system;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import parkinglot.managers.AppContext;

import java.io.IOException;
import java.util.Objects;
import parkinglot.users.Customer;
import parkinglot.users.Person;
import parkinglot.models.Location;


public class RegistrationWindow{

    private final Stage stage;
    private final AppContext appContext;

    public RegistrationWindow(AppContext appContext) {
        this.appContext = appContext;
        this.stage = appContext.stage;
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


        appContext.pushView(root, "Registration", 460, 440, false);

//        -----------------Controls----------------


        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirm = confirmPasswordField.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                registerLabel.setText("Fields cannot be empty!");
            } else if (!password.equals(confirm)) {
                registerLabel.setText("Passwords do not match!");
            } else {
                registerButton.setDisable(true);
                registerLabel.setText("Registering...");

                new Thread(() -> {
                    try {
                        Location location = new Location("", "", "", "", "");
                        Person person = new Person(username, location, email, "");
                        Customer customer = new Customer(username, password, person);
                        
                        appContext.apiManager.register(customer);

                        Platform.runLater(() -> {
                            appContext.goBack("Login", 460, 440, false);
                        });
                    } catch (Exception ex) {
                        Platform.runLater(() -> {
                            registerLabel.setText(ex.getMessage());
                            registerButton.setDisable(false);
                        });
                    }
                }).start();
            }
        });


        loginLink.setOnAction(e -> appContext.goBack("Login", 460, 440, false));
    }
}