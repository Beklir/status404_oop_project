module parkinglot {
    requires javafx.controls;
    requires javafx.graphics;

    // Export the package to allow other modules to use it
    exports parkinglot;
    // IMPORTANT: Open the package to javafx.graphics
    // This allows JavaFX to launch the Application class
    opens parkinglot;
}