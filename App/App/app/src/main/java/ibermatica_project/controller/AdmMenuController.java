package ibermatica_project.controller;

import java.io.IOException;

import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdmMenuController {
    @FXML
    Label lblName, lblSurname, lblUsername, lblEmail, lblTlfNumber, lblType;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    @FXML
    protected void initialize() throws IOException {
        User loggedUser = IndexController.getLoggedUser();

        lblName.setText(loggedUser.getName());
        lblSurname.setText(loggedUser.getSurname());
        lblUsername.setText(loggedUser.getUsername());
        lblEmail.setText(loggedUser.getEmail());
        lblTlfNumber.setText(Integer.toString(loggedUser.getTlfNum()));

        if (loggedUser.getType() == 0) {
            lblType.setText("Administrador");
        } else {
            lblType.setText("Empleado");
        }
    }
}