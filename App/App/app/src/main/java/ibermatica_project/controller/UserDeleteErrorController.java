package ibermatica_project.controller;

import java.io.IOException;
import java.util.ArrayList;

import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import ibermatica_project.model.base.Validations;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UserDeleteErrorController {
    @FXML
    Label lblInfo;

    @FXML
    Button btnDelete, btnAssign, btnBack;

    @FXML
    TextField txfUserId;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    private static User deleteUser = UserManagementMenuController.getDeleteUser();

    @FXML
    private void initialize() throws IOException {
        lblInfo.setText("El usuario "+deleteUser.getName()+" no se puede eliminar por que tiene reservas a su cargo\ndesea eliminar "+
            "todas sus reservas o asignarlas o otro usuario?");
    }

    @FXML
    private void assign() throws IOException {
        if (checkDbUserId()) {
            boolean result = db.updateReservationUserId(txfUserId.getText().replaceAll("\\s", ""), deleteUser.getUserId());
            if (result) {
                Validations.generateSuccessAltert("La asignacion se a hecho correctamente");
                loadUserManagementMenuScene();
            } else {
                Validations.generateErrorAltert("Hemos tenido problemas al hacer la asignacion del usuario, por favor contacte con un Administrador");
            }
        }
    }

    private boolean checkDbUserId() {
        boolean valid = false;
        if (txfUserId.getText().isEmpty()) {
            Validations.generateErrorAltert("El campo del DNI no esta completado");
            return false;
        }

        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();

        for (User user : userList) {
            if (user.getUserId().equals(txfUserId.getText().replaceAll("\\s", ""))) {
                valid = true;
            }

        }
        if (!valid) {
            Validations.generateErrorAltert("El DNI insertado no existe");
        }
        return valid;
    }

    @FXML
    private void deleteReserver() throws IOException {
        boolean result = db.deleteAllReservations(deleteUser.getUserId());
        if (!result) {
            Validations.generateSuccessAltert("Las reservas del usuario se han eliminado correctamente");
            loadUserManagementMenuScene();
        } else {
            Validations.generateErrorAltert("Hemos tenido problemas al eliminar las reservas del usuario, por favor contacte con un Administrador");
        }
    }

    @FXML
    private void loadUserManagementMenuScene() throws IOException {
        SceneController.loadUserManagementMenuScene();
    }
}
