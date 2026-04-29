package parkinglot.ui.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import parkinglot.constants.ParkingSpotType;
import parkinglot.managers.AppContext;
import parkinglot.models.spots.ParkingSpot;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import parkinglot.models.ParkingFloor;
import parkinglot.models.ParkingLot;

import java.util.List;

public class FloorManagerTab {
    private final AppContext appContext;

    public FloorManagerTab(AppContext appContext) {
        this.appContext = appContext;
    }

    public SplitPane getContent() {
        SplitPane splitPane = new SplitPane();

        // --- Left: Floor Management ---
        VBox floorListContainer = new VBox(10);
        floorListContainer.setPadding(new Insets(15));
        floorListContainer.setMinWidth(250);
        floorListContainer.setStyle("-fx-background-color: #fcfcfc;");

        Label floorListTitle = new Label("Parking Floors");
        floorListTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #333;");

        ListView<String> floorListView = new ListView<>();
        VBox.setVgrow(floorListView, Priority.ALWAYS);

        TextField newFloorField = new TextField();
        newFloorField.setPromptText("Floor Name (e.g. Ground Floor)");
        
        Button addFloorBtn = new Button("Add New Floor");
        addFloorBtn.setMaxWidth(Double.MAX_VALUE);
        addFloorBtn.setPrefHeight(30);
        
        Button deleteFloorBtn = new Button("Delete Selected Floor");
        deleteFloorBtn.setMaxWidth(Double.MAX_VALUE);
        deleteFloorBtn.setStyle("-fx-text-fill: #d9534f;");

        floorListContainer.getChildren().addAll(floorListTitle, floorListView, new Separator(), newFloorField, addFloorBtn, deleteFloorBtn);

        // --- Right: Spot Management ---
        VBox spotContainer = new VBox(15);
        spotContainer.setPadding(new Insets(15));
        spotContainer.setStyle("-fx-background-color: white;");

        Label spotTitle = new Label("Parking Spots Detail View");
        spotTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #333;");

        TableView<ParkingSpot> spotTable = new TableView<>();
        
        TableColumn<ParkingSpot, String> numCol = new TableColumn<>("Spot #");
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        
        TableColumn<ParkingSpot, String> typeCol = new TableColumn<>("Spot Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<ParkingSpot, Boolean> statusCol = new TableColumn<>("Is Free");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("free"));

        spotTable.getColumns().addAll(numCol, typeCol, statusCol);
        spotTable.setPlaceholder(new Label("Select a floor to view spots"));
        spotTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Spot creation controls
        HBox spotControls = new HBox(12);
        spotControls.setAlignment(Pos.CENTER_LEFT);
        spotControls.setPadding(new Insets(10));
        spotControls.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #eee; -fx-border-radius: 5;");

        TextField spotNumField = new TextField();
        spotNumField.setPromptText("ID");
        spotNumField.setPrefWidth(70);

        ComboBox<ParkingSpotType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(ParkingSpotType.values());
        typeCombo.getSelectionModel().select(ParkingSpotType.COMPACT);

        Button addSpotBtn = new Button("Add Spot");
        addSpotBtn.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");
        
        Button deleteSpotBtn = new Button("Delete Spot");
        deleteSpotBtn.setStyle("-fx-background-color: #f0ad4e; -fx-text-fill: white;");

        spotControls.getChildren().addAll(new Label("Quick Add:"), spotNumField, typeCombo, addSpotBtn, new Separator(), deleteSpotBtn);

        spotContainer.getChildren().addAll(spotTitle, spotTable, spotControls);
        VBox.setVgrow(spotTable, Priority.ALWAYS);

        splitPane.getItems().addAll(floorListContainer, spotContainer);
        splitPane.setDividerPositions(0.3);

        // --- Logic: Data Observation & Selection ---
        appContext.parkingLotProperty().addListener((obs, oldLot, newLot) -> {
            if (newLot != null) {
                Platform.runLater(() -> updateFloorList(floorListView, newLot));
            }
        });

        // Initialize if data already exists
        if (appContext.getParkingLot() != null) {
            updateFloorList(floorListView, appContext.getParkingLot());
        }

        floorListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && appContext.getParkingLot() != null) {
                appContext.getParkingLot().getFloors().stream()
                        .filter(f -> f.getName().equals(newVal))
                        .findFirst()
                        .ifPresent(floor -> spotTable.setItems(FXCollections.observableArrayList(floor.getSpots())));
            }
        });

        return splitPane;
    }

    private void updateFloorList(ListView<String> floorListView, ParkingLot lot) {
        List<String> names = lot.getFloors().stream().map(ParkingFloor::getName).toList();
        floorListView.setItems(FXCollections.observableArrayList(names));
        if (floorListView.getSelectionModel().getSelectedIndex() < 0 && !names.isEmpty()) {
            floorListView.getSelectionModel().select(0);
        }
    }
}
