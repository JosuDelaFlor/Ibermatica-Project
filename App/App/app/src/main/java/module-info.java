module ibermatica_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;

    opens ibermatica_project to javafx.fxml;
    opens ibermatica_project.controller to javafx.fxml;

    exports ibermatica_project;
    exports ibermatica_project.lang;
    exports ibermatica_project.controller;
    exports ibermatica_project.model;
    exports ibermatica_project.tests;
}
