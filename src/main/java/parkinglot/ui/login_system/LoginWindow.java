package parkinglot.ui.login_system;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import parkinglot.managers.AppContext;
import parkinglot.server.ServerUI;
import parkinglot.users.Account;

import java.util.Objects;


public class LoginWindow {

    private final AppContext appContext;
    private final Stage stage;
    private final TextField ipField = new TextField();
    private final TextField portField = new TextField();

    public LoginWindow(AppContext appContext) {
        this.appContext = appContext;
        this.stage = appContext.stage;
        ipField.setText(appContext.apiManager.getServerAddress().ip);
        portField.setText(String.valueOf(appContext.apiManager.getServerAddress().port));
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
        card.setPadding(new Insets(56, 40, 36, 40));

        Node topRight = getTopRightPane();

        StackPane root = new StackPane(card, topRight);
        StackPane.setAlignment(topRight, Pos.TOP_RIGHT);
        root.setPadding(new Insets(20, 40,40,40));


        Scene scene = new Scene(root, 460, 440);

        Image icon = new  Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/login_icon.png")));

        stage.getIcons().setAll(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

//        ------------------Controls---------------------

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                loginLabel.setText("Please enter username and password.");
                loginLabel.setTextFill(Color.RED);
                return;
            }

            new Thread(() -> {
                Platform.runLater(() -> {
                    loginLabel.setText("Logging in...");
                    loginLabel.setTextFill(Color.BLACK);
                });

                boolean authenticated = false;
                try {
                    Account account = appContext.apiManager.login(username, password);
                    authenticated = account != null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        loginLabel.setText("Unable to connect to the server.");
                        loginLabel.setTextFill(Color.RED);
                    });
                    return;
                }


                if (authenticated) {
                    Platform.runLater(() -> new WelcomeScreen(appContext, username).show());
                } else {
                    Platform.runLater(() -> {
                        loginLabel.setText("Incorrect username or password.");
                        loginLabel.setTextFill(Color.RED);
                    });
                }

            }).start();
        });

        registerLink.setOnAction(e -> new RegistrationWindow(appContext).show()); //
    }

    private Pane getTopRightPane() {
        ipField.setPromptText("Server IP Addr.");
        ipField.setPrefWidth(120);
        ipField.textProperty().addListener((_, _, newValue) -> {appContext.apiManager.setServerIp(newValue);});

        portField.setPromptText("Port");
        portField.setPrefWidth(50);
        portField.textProperty().addListener((_, _, newValue) -> {
            try{
                appContext.apiManager.setServerPort(Integer.parseInt(newValue));
            } catch (NumberFormatException _) {}
        });

        Hyperlink button = new Hyperlink("Host Server");
        button.setOnMouseClicked(_->{
            new ServerUI(appContext).show();
        });

        HBox hBox = new HBox(10, ipField, portField);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(5, hBox, button);
        content.setAlignment(Pos.TOP_RIGHT);
        content.setVisible(false);
        content.setManaged(false);

        Hyperlink toggleBtn = new Hyperlink("Connect...");
        toggleBtn.setOnAction(e -> {
            boolean isVisible = content.isVisible();
            content.setVisible(!isVisible);
            content.setManaged(!isVisible);
            toggleBtn.setText(isVisible ? "Connect..." : "Hide");
        });

        VBox container = new VBox(5, toggleBtn, content);
        container.setAlignment(Pos.TOP_RIGHT);
        container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        return container;
    }
}