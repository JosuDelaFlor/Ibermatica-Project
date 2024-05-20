module design {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens design to javafx.fxml;
    opens design.controller to javafx.fxml;

    exports design.controller;
    exports design;
}
