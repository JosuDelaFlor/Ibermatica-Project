module demo {
    requires javafx.controls;
    requires javafx.fxml;

    opens demo to javafx.fxml;
    opens demo.controller to javafx.fxml;

    exports demo;
    exports demo.lang;
    exports demo.controller;
    exports demo.model;
}
