package proposal.controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import proposal.App;
import proposal.model.DataBase;
import proposal.model.User;

public class IndexController {
    @FXML
    Button btnLogin, btnReset, btnExit;

    @FXML
    TextField lblUser;

    @FXML
    PasswordField lblPass;

    @FXML
    Label lblError;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    @FXML
    private void checkUser() throws IOException {
        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();
        String username = lblUser.getText().replaceAll("\\s", ""), password = lblPass.getText().replaceAll("\\s", "");

        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                if (user.getType() == 0) {
                    App.setRoot("fxml/admMenu");
                } else {
                    App.setRoot("fxml/employeeMenu");
                }
            } else if (username.equals("") || password.equals("")) {
                lblError.setText("Both fields must be completed");
            } else {
                lblError.setText("The username or password is incorrect");
            }
        }
    }

    @FXML
    private void restartLabels() {
        lblUser.setText(null);
        lblPass.setText(null);
        lblError.setText(null);
    }

    @FXML
    private void exit() {
        Platform.exit();
    }
}
