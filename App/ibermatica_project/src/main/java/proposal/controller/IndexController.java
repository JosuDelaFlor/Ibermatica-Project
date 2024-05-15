package proposal.controller;

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
import proposal.model.base.StaticMethods;

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

    static User loggedUser;

    /**
     * Collects all the users in the database and compares the inserted data and if they match 
     * those in the database the user will access, depending on the type of user, they will access 
     * one scene or another
     * @throws Exception 
     */

    @FXML
    private void checkUser() throws Exception {
        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();
        String username = txfUser.getText().replaceAll("\\s", ""), password = psfPass.getText().replaceAll("\\s", "");

        for (User user : userList) {
            byte[] passBytes = StaticMethods.stringToByte(user.getPassword());
            String pass = StaticMethods.decipher(passBytes);

            if (user.getUsername().equals(username) && pass.equals(password)) {
                loggedUser = new User(user.getUserId(), user.getName(), user.getSurname(), user.getEmail(), user.getTlfNum(), 
                        user.getUsername(), user.getPassword(), user.getRegisterdate(), user.getType());
                if (user.getType() == 0) {
                    App.setRoot("fxml/admMenu");
                } else {
                    App.setRoot("fxml/employeeMenu");
                }
            } else if (username.equals("") || password.equals("")) {
                lblError.setText("Los dos campos tienes que estar completados");
            } else {
                lblError.setText("El nombre de usuario o la contraseña son incorrectos");
            }
        }
    }

    @SuppressWarnings("exports")
    public static User getLoggedUser() {
        return loggedUser;
    }

    @FXML
    private void restartLabels() { // Reset all labels
        txfUser.setText(null);
        psfPass.setText(null);
        lblError.setText(null);
    }

    @FXML
    private void exit() {
        Platform.exit();
    }
}
