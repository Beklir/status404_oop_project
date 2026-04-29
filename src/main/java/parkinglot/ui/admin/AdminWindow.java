package parkinglot.ui.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import parkinglot.managers.AppContext;
import parkinglot.ui.login_system.LoginWindow;
import parkinglot.ui.login_system.ProfileWindow;

public class AdminWindow {
    private final AppContext appContext;
    private final Stage stage;

    public AdminWindow(AppContext appContext) {
        this.appContext = appContext;
        this.stage = appContext.stage;
    }

    public void show() {
        stage.setTitle("Admin Panel - Parking Lot System");

        TabPane tabPane = new TabPane();
        
        // Real tabs
        Tab dashboardTab = new Tab("Dashboard", createPlaceholder("Dashboard Content Overview"));
        Tab floorTab = new Tab("Floors", new FloorManagerTab(appContext).getContent());
        Tab attendantTab = new Tab("Attendants", createPlaceholder("Parking Attendant Management"));
        Tab rateTab = new Tab("Rates", createPlaceholder("Parking Rate Configuration"));

        tabPane.getTabs().addAll(dashboardTab, floorTab, attendantTab, rateTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Reusable TopBar
        parkinglot.ui.components.TopBar topBar = new parkinglot.ui.components.TopBar(appContext, "Admin Management Portal");

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        root.setTop(topBar);
        root.setPadding(new Insets(0, 0, 10, 0));

        appContext.resetToView(root, "Admin Panel - Parking Lot System", 1000, 700, true);
        
        // Initial data fetch
        refreshData();
    }

    private void refreshData() {
        new Thread(() -> {
            try {
                parkinglot.models.ParkingLot lot = appContext.apiManager.getStatus();
                javafx.application.Platform.runLater(() -> appContext.setParkingLot(lot));
            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Synchronization Error");
                    alert.setHeaderText("Failed to fetch parking lot status");
                    alert.setContentText(e.getMessage());
                    alert.show();
                });
            }
        }).start();
    }

    private Node createPlaceholder(String text) {
        StackPane pane = new StackPane(new Label(text));
        pane.setAlignment(Pos.CENTER);
        return pane;
    }
}
