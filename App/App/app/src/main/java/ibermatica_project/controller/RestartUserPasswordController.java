package ibermatica_project.controller;

import java.io.IOException;

import ibermatica_project.model.Validation;
import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.Validations;
import ibermatica_project.model.base.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RestartUserPasswordController {
    @FXML
    TextField txfUserId;

    @FXML
    PasswordField psfPassword1, psfPassword2;

    @FXML
    Button btnUpdate, btnBack;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    static Alert alert = new Alert(Alert.AlertType.WARNING);

    @FXML
    private void update() throws IOException {
        if (checkPassword()) {
            int result = db.updatePassword(psfPassword1.getText().replaceAll("\\s", ""), txfUserId.getText().replaceAll("\\s", ""));
            if (result >= 1) {
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Contraseña actualizada");
                alert.setHeaderText("La actualización de la contraseña se completo con éxito");
                alert.show();
                SceneController.loadUserModifyMenuScene();
            } else {
                generateAlert("Hemos tenido algún fallo al actualizar la contraseña, por favor contacta con un Administrador");
            } 
        }
    }

    private boolean checkPassword() throws IOException {
        boolean valid = false, passV = false, userIdV = false;

        Validation validation = Validations.passValidation(psfPassword1.getText().replaceAll("\\s", ""), psfPassword2.getText().replaceAll("\\s", ""));
        if (!validation.isValid()) {
            generateAlert(validation.getErrorMsg());
        } else {
            passV = true;
        }
        
        User user = UserModifyController.getModifyUser();
        if (!user.getUserId().equals(txfUserId.getText().replaceAll("\\s", ""))) {
            generateAlert("El DNI insertado no coincide con su usuario");
        } else {
            userIdV = true;
        }

        if (passV == true && userIdV == true) {
            valid = true;
        }
        return valid;
    }

    @FXML
    private void generateAlert(String error) throws IOException {
        alert.setTitle("Cuidado!");
        alert.setHeaderText(error);
        alert.show();
    }

    @FXML
    private void loadUserModifyScene() throws IOException {
        SceneController.loadUserModifyMenuScene();
    }
}
