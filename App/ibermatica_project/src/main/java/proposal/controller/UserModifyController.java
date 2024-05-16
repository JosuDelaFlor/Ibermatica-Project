package proposal.controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import proposal.App;
import proposal.model.Validation;
import proposal.model.base.DataBase;
import proposal.model.base.StaticMethods;
import proposal.model.base.User;

public class UserModifyController {
    @FXML
    Button btnLogOut, btnBack, passRestart, btnSearchUser, btnModify;

    @FXML
    TextField txfId, txfName, txfSurname, txfUser, txfEmail, txfTlfNumber, txfSearch;

    @FXML
    Label lblRegisterday, lblError, lblSuccess;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboType;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    @SuppressWarnings("unchecked")
    @FXML
    private void searchUser() throws IOException {
        User user = db.searchSpecificUser(txfId.getText());
        txfName.setText(user.getName());
        txfSurname.setText(user.getSurname());
        txfEmail.setText(user.getEmail());
        txfUser.setText(user.getUsername());
        txfTlfNumber.setText(Integer.toString(user.getTlfNum()));
        lblRegisterday.setText(user.getRegisterdate().toString());
        
        if (user.getType() == 0) {
            comboType.setValue("Administrador");
        } else {
            comboType.setValue("Empleado");
        }
    }

    public void modify() throws IOException {
        boolean valid = testValidations(), validId = checkDbId(txfId.getText());
        if (validId == false) {
            lblError.setText("El DNI insertado no existe");
        } else if (valid) {
            lblError.setText(null);
            int type = 1;
            if ((String) comboType.getValue() == "Administrador") {
                type = 0;
            }

            User user = new User(txfId.getText(), txfName.getText(), txfSurname.getText(), txfEmail.getText(),
                        Integer.parseInt(txfTlfNumber.getText()), txfUser.getText(), null, null, type);
            int result = db.updateUser(user);
            if (result == 1) {
                lblSuccess.setText("El usuario ha sido actualizado");
            } else {
                lblError.setText("Hemos tenido problemas al actializar el usuario");
            }
        }   
    }

    @FXML
    private boolean testValidations() throws IOException {
        boolean valid = false;
        
        Validation userIdValidation = StaticMethods.userIdValidation(txfId.getText());
        Validation nameSurnameUsernameValidation = StaticMethods.nameSurnameUsernameValidation(txfName.getText(), txfSurname.getText(), txfUser.getText());
        Validation tlfNumberValidation = StaticMethods.tlfNumberValidation(txfTlfNumber.getText());
        Validation emailValidation = StaticMethods.emailValidation(txfEmail.getText());

        boolean userIdV = true, nameSurnameUsernameV = true, tlfNumberV = true, emailV = true;

        if (!userIdValidation.isValid()) {
            lblError.setText(userIdValidation.getErrorMsg());
            userIdV = false;
        }
        if (!nameSurnameUsernameValidation.isValid()) {
            lblError.setText(nameSurnameUsernameValidation.getErrorMsg());
            nameSurnameUsernameV = false;
        }
        if (!tlfNumberValidation.isValid()) {
            lblError.setText(tlfNumberValidation.getErrorMsg());
            tlfNumberV = false;
        }
        if (!emailValidation.isValid()) {
            lblError.setText(emailValidation.getErrorMsg());
            emailV = false;
        }

        if (userIdV == false || nameSurnameUsernameV == false || tlfNumberV == false || emailV == false) {
            valid = false;
        } else {
            valid = true;
        }
        
        return valid;
    }

    private boolean checkDbId(String id) {
        boolean validId = false;
        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();

        for (User user : userList) {
            if (user.getUserId().equals(id)) {
                validId = true;
            }
        }
        return validId;
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("fxml/index");
    }

    @FXML
    private void loadUserDataScene() throws IOException {
        App.setRoot("fxml/userData");
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("fxml/admMenu");
    }
}
