module proposal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens proposal to javafx.fxml;
    opens proposal.controller to javafx.fxml;

    exports proposal;
    exports proposal.controller;
    exports proposal.model;
    exports proposal.tests;
}
