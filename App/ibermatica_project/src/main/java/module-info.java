module proposal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens proposal to javafx.fxml;
    exports proposal;
    exports proposal.controller;
    exports proposal.model;
}
