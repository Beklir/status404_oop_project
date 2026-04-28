package parkinglot.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import parkinglot.managers.AppContext;
import parkinglot.ui.login_system.LoginWindow;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;

public class ServerUI {

    private ConfigurableApplicationContext springContext;
    private TextArea logArea;
    private final AppContext appContext;
    private final Stage stage;

    public ServerUI(AppContext appContext){
        this.appContext = appContext;
        this.stage = appContext.stage;
        new Thread(() -> {
            ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class);

            Platform.runLater(() -> {
                this.springContext = context;
                onSpringStarted();
            });
        }).start();
    }

    private void onSpringStarted() {
        // 1. Ensure the context is a WebServerApplicationContext
        if (springContext instanceof WebServerApplicationContext) {
            WebServer webServer = ((WebServerApplicationContext) springContext).getWebServer();

            try {
                assert webServer != null;
                int port = webServer.getPort();
                // Get the local machine's IP address
                String ip = InetAddress.getLocalHost().getHostAddress();

                System.out.println("\nServer started at:");
                System.out.println("IP: " + ip);
                System.out.println("Port: " + port);
                System.out.println("URL: http://" + ip + ":" + port);

            } catch (Exception e) {
                System.out.println("Could not resolve local IP.");
            }
        } else {
            System.out.println("Context is not a Web Server context.");
        }
    }

    public void show() {
        logArea = new TextArea();
        logArea.setEditable(false);

        // Redirect System.out to the TextArea
        PrintStream ps = new PrintStream(new ConsoleStream(logArea));
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        System.setOut(ps);
        System.setErr(ps);

        Button stopButton = new Button("Stop Server");
        stopButton.setOnAction(e -> {
            if(springContext==null){
                System.out.println("Please wait, server hasn't started yet.");
                return;
            }
            springContext.close();
            System.setOut(originalOut);
            System.setErr(originalErr);
            new LoginWindow(appContext).show();
        });

        HBox actionPane = new HBox(10, stopButton);

        BorderPane root = new BorderPane(logArea);
        root.setBottom(actionPane);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Parking Lot Server Control");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setOnCloseRequest(e -> {
            if (springContext != null) {
                springContext.close();
            }
            Platform.exit();
        });
        stage.show();

        System.out.println("Starting Server...");
    }

    // Helper to pipe console to UI
    private static class ConsoleStream extends OutputStream {
        private TextArea output;
        public ConsoleStream(TextArea ta) { this.output = ta; }
        @Override
        public void write(int i) {
            Platform.runLater(() -> output.appendText(String.valueOf((char) i)));
        }
    }
}