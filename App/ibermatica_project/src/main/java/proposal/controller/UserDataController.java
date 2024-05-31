package proposal.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import proposal.App;
import proposal.model.base.DataBase;
import proposal.model.base.User;

public class UserDataController {
    @FXML
    Button btnBack, btnLogOut;

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
    
    @FXML
    private void logOut() throws IOException {
        App.setRoot("fxml/index");
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("fxml/admMenu");;
    }
}
