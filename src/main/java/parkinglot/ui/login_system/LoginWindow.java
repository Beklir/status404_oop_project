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
import parkinglot.ui.admin.AdminWindow;
import parkinglot.users.Account;
import parkinglot.users.Admin;

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

        Node topRight = getTopRightPane();
        StackPane root = new StackPane();
        root.setPadding(new Insets(20, 40, 40, 40));

        if (appContext.apiManager.isLoggedIn()) {
            showAutoLoginState(root, topRight);
        } else {
            showStandardLoginState(root, topRight);
        }

        appContext.resetToView(root, "Login", 460, 440, false);

        // Add icon (only once or every time resetToView is called?)
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/login_icon.png")));
        stage.getIcons().setAll(icon);
    }

    private void showAutoLoginState(StackPane root, Node topRight) {
        Label statusLabel = new Label("Restoring session...");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ProgressIndicator progress = new ProgressIndicator();

        VBox loadingBox = new VBox(20, statusLabel, progress);
        loadingBox.setAlignment(Pos.CENTER);

        root.getChildren().setAll(loadingBox, topRight);
        StackPane.setAlignment(topRight, Pos.TOP_RIGHT);

        autoLogin(root, statusLabel, loadingBox, topRight);
    }

    private void autoLogin(StackPane root, Label statusLabel, VBox loadingBox, Node topRight) {
        new Thread(() -> {
            try {
                Account account = appContext.apiManager.getCurrentAccount();
                if (account != null) {
                    appContext.setAccount(account);
                    Platform.runLater(() -> {
                        if (account instanceof Admin) {
                            new AdminWindow(appContext).show();
                        } else {
                            new WelcomeScreen(appContext).show();
                        }
                    });
                } else {
                    throw new Exception("Session expired or invalid.");
                }
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    statusLabel.setText("Failed to restore session.");
                    statusLabel.setTextFill(Color.RED);
                    loadingBox.getChildren().remove(1); // Remove progress indicator

                    Button retryBtn = new Button("Try Again");
                    retryBtn.setOnAction(e -> showAutoLoginState(root, topRight));

                    Button logoutBtn = new Button("Log Out");
                    logoutBtn.setOnAction(e -> {
                        appContext.logOut();
                        showStandardLoginState(root, topRight);
                    });

                    HBox actions = new HBox(10, retryBtn, logoutBtn);
                    actions.setAlignment(Pos.CENTER);
                    loadingBox.getChildren().add(actions);
                });
            }
        }).start();
    }

    private void showStandardLoginState(StackPane root, Node topRight) {
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

        CheckBox rememberMeCheckBox = new CheckBox("Remember me");
        rememberMeCheckBox.setSelected(true);

        VBox formBox = new VBox(6,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                rememberMeCheckBox
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

        root.getChildren().setAll(card, topRight);
        StackPane.setAlignment(topRight, Pos.TOP_RIGHT);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                loginLabel.setText("Please enter username and password.");
                loginLabel.setTextFill(Color.RED);
                return;
            }
            loginButton.setDisable(true);
            loginLabel.setText("Logging in...");
            loginLabel.setTextFill(Color.BLACK);
            boolean rememberMe = rememberMeCheckBox.isSelected();
            new Thread(() -> {
                boolean authenticated = false;
                try {
                    Account account = appContext.apiManager.login(username, password, rememberMe);
                    if (account == null) {
                        Platform.runLater(() -> {
                            loginLabel.setText("Incorrect username or password.");
                            loginLabel.setTextFill(Color.RED);
                            loginButton.setDisable(false);
                        });
                        return;
                    }
                    authenticated = true;
                    appContext.setAccount(account);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        loginLabel.setText("Unable to connect to the server.");
                        loginLabel.setTextFill(Color.RED);
                        loginButton.setDisable(false);
                    });
                    return;
                }

                if (authenticated) {
                    Platform.runLater(() -> {
                        if (appContext.account instanceof Admin) {
                            new AdminWindow(appContext).show();
                        } else {
                            new WelcomeScreen(appContext).show();
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        loginLabel.setText("Incorrect username or password.");
                        loginLabel.setTextFill(Color.RED);
                        loginButton.setDisable(false);
                    });
                }

            }).start();
        });

        registerLink.setOnAction(e -> new RegistrationWindow(appContext).show());
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