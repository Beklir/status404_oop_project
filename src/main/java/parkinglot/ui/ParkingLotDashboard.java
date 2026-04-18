package parkinglot.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import parkinglot.constants.VehicleType;
import parkinglot.hardware.EntrancePanel;
import parkinglot.hardware.ExitPanel;
import parkinglot.models.Location;
import parkinglot.models.ParkingFloor;
import parkinglot.models.ParkingLot;
import parkinglot.models.ParkingTicket;
import parkinglot.models.spots.CompactSpot;
import parkinglot.models.spots.ElectricSpot;
import parkinglot.models.spots.HandicappedSpot;
import parkinglot.models.spots.LargeSpot;
import parkinglot.models.spots.MotorbikeSpot;
import parkinglot.models.vehicles.Car;
import parkinglot.models.vehicles.ElectricVehicle;
import parkinglot.models.vehicles.Motorbike;
import parkinglot.models.vehicles.Truck;
import parkinglot.models.vehicles.Van;
import parkinglot.models.vehicles.Vehicle;
import parkinglot.ui.login_system.WelcomeScreen;

import java.util.HashMap;
import java.util.Map;

public class ParkingLotDashboard {
    private final Stage stage;
    private final String username;

    private ParkingLot parkingLot;
    private EntrancePanel entrancePanel;
    private ExitPanel exitPanel;
    private final Map<String, Vehicle> activeVehiclesByLicense = new HashMap<>();

    private Label statusLabel;
    private Label metricsLabel;
    private TextArea ticketsArea;

    public ParkingLotDashboard(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
    }

    public void show() {
        stage.setTitle("Parking Lot Dashboard");

        Label titleLabel = new Label("Parking Lot Operations");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        metricsLabel = new Label();
        metricsLabel.setStyle("-fx-font-size: 13px;");

        statusLabel = new Label("Ready.");

        VBox entryCard = buildEntryCard();
        VBox paymentCard = buildPaymentCard();
        VBox exitCard = buildExitCard();

        HBox operationsRow = new HBox(12, entryCard, paymentCard, exitCard);
        HBox.setHgrow(entryCard, Priority.ALWAYS);
        HBox.setHgrow(paymentCard, Priority.ALWAYS);
        HBox.setHgrow(exitCard, Priority.ALWAYS);

        ticketsArea = new TextArea();
        ticketsArea.setEditable(false);
        ticketsArea.setPrefRowCount(10);
        ticketsArea.setPromptText("Active tickets will appear here...");

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new WelcomeScreen(stage, username).show());

        VBox root = new VBox(
                12,
                titleLabel,
                new Label("Signed in as: " + username),
                metricsLabel,
                operationsRow,
                new Label("Active Tickets"),
                ticketsArea,
                statusLabel,
                backButton
        );
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(16));


        Scene scene = new Scene(root, 960, 640);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private VBox buildEntryCard() {
        Label sectionTitle = new Label("Vehicle Entry");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField licenseField = new TextField();
        licenseField.setPromptText("License number");

        ComboBox<VehicleType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(VehicleType.values());
        typeBox.getSelectionModel().select(VehicleType.CAR);

        Button enterButton = new Button("Issue Ticket");
        enterButton.setMaxWidth(Double.MAX_VALUE);

        GridPane form = new GridPane();
        form.setVgap(8);
        form.setHgap(8);
        form.add(new Label("License"), 0, 0);
        form.add(licenseField, 1, 0);
        form.add(new Label("Type"), 0, 1);
        form.add(typeBox, 1, 1);

        VBox card = new VBox(10, sectionTitle, form, enterButton);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-border-color: #d0d0d0; -fx-border-radius: 8; -fx-background-radius: 8;");
        return card;
    }

    private VBox buildPaymentCard() {
        Label sectionTitle = new Label("Ticket Payment");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField ticketField = new TextField();
        ticketField.setPromptText("Ticket number");
        TextField cashField = new TextField();
        cashField.setPromptText("Cash amount");

        Button payButton = new Button("Pay by Cash");
        payButton.setMaxWidth(Double.MAX_VALUE);

        GridPane form = new GridPane();
        form.setVgap(8);
        form.setHgap(8);
        form.add(new Label("Ticket #"), 0, 0);
        form.add(ticketField, 1, 0);
        form.add(new Label("Cash"), 0, 1);
        form.add(cashField, 1, 1);

        VBox card = new VBox(10, sectionTitle, form, payButton);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-border-color: #d0d0d0; -fx-border-radius: 8; -fx-background-radius: 8;");
        return card;
    }

    private VBox buildExitCard() {
        Label sectionTitle = new Label("Vehicle Exit");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField licenseField = new TextField();
        licenseField.setPromptText("License number");

        Button exitButton = new Button("Exit Vehicle");
        exitButton.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(10, sectionTitle, new Label("License"), licenseField, exitButton);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-border-color: #d0d0d0; -fx-border-radius: 8; -fx-background-radius: 8;");
        return card;
    }

    private Vehicle createVehicle(VehicleType type, String license) {
        switch (type) {
            case TRUCK:
                return new Truck(license);
            case VAN:
                return new Van(license);
            case MOTORBIKE:
                return new Motorbike(license);
            case ELECTRIC:
                return new ElectricVehicle(license);
            case CAR:
            default:
                return new Car(license);
        }
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }
}
