package ibermatica_project.controller;

import java.io.IOException;
import java.util.ArrayList;

import java.sql.Connection;

import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class IndexController {
    @FXML
    Button btnLogin;

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
    private void checkUser() throws IOException {
        Connection connection = db.connect();
        if (connection == null) {
            showAlert(1);
        } else {
            ArrayList<User> userList = new ArrayList<User>();
            userList = db.searchAllUsers();
            String username = txfUser.getText().replaceAll("\\s", ""), password = psfPass.getText().replaceAll("\\s", "");

            for (User user : userList) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    loggedUser = new User(user.getUserId(), user.getName(), user.getSurname(), user.getEmail(), user.getTlfNum(), 
                            user.getUsername(), user.getPassword(), user.getRegisterdate(), user.getType());
                    if (user.getType() == 0) {
                        SceneController.loadAdmMenuScene();
                        break;
                    } else {
                        // App.setRoot("fxml/employeeMenu");
                    }
                } else if (username.equals("") || password.equals("")) {
                    showAlert(0);
                } else {
                    lblError.setText("Usuario o contraseña incorrectos");
                }
            }
        }
    }

    @SuppressWarnings("exports")
    public static User getLoggedUser() {
        return loggedUser;
    }

    private static void showAlert(int error) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cuidado!");
        if (error == 0) {
            alert.setHeaderText("Error al iniciar sesión");
            alert.setContentText("El nombre de usuario y la contraseña tienen que estar completados");
        } else {
            alert.setHeaderText("Error de servidor");
            alert.setContentText("No hemos conseguido conectarnos al servidor, pregunte al Administrador para intentar resolver el problema");
        }
        alert.show();
    }
}