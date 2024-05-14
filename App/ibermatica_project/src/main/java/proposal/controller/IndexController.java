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
import proposal.model.base.DataBase;
import proposal.model.base.User;

public class IndexController {
    @FXML
    Button btnLogin, btnReset, btnExit;

    @FXML
    TextField txfUser;

    @FXML
    PasswordField psfPass;

    @FXML
    Label lblError;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    @FXML
    private void checkUser() throws IOException {
        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();
        String username = txfUser.getText().replaceAll("\\s", ""), password = psfPass.getText().replaceAll("\\s", "");

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
        txfUser.setText(null);
        psfPass.setText(null);
        lblError.setText(null);
    }

    @FXML
    private void exit() {
        Platform.exit();
    }
}
