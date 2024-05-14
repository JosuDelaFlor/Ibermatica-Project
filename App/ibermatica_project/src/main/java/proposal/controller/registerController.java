package proposal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import proposal.App;
import proposal.model.Validation;
import proposal.model.base.DataBase;
import proposal.model.base.User;
import proposal.model.base.StaticMethods;

public class registerController {
    @FXML
    Button btnBack, btnLogOut, btnRegister;

    @FXML
    TextField txfId, txfName, txfSurname, txfEmail, txfTlfNumber, txfUser; 

    @FXML
    PasswordField psfPass1, psfPass2;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboType;

    @FXML
    Label lblError;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    @SuppressWarnings("unchecked")
    @FXML
    protected void initialize() {
        List<String> roleNameList = new ArrayList<String>();
        roleNameList.addAll(Arrays.asList("Administrador", "Empleado"));

        for (int i = 0; i < roleNameList.size(); i++) {
            comboType.getItems().add(roleNameList.get(i));
        }
    }

    @SuppressWarnings("exports")
    @FXML
    public User register() throws IOException {
        boolean valid = testValidations();
        if (valid) {
            lblError.setText(null);
        }
        return null;
    }

    @FXML
    private boolean testValidations() throws IOException {
        Validation userIdValidation = StaticMethods.userIdValidation(txfId.getText());
        Validation nameSurnameUsernameValidation = StaticMethods.nameSurnameUsernameValidation(txfName.getText(), txfSurname.getText(), txfUser.getText());
        Validation tlfNumberValidation = StaticMethods.tlfNumberValidation(txfTlfNumber.getText());
        Validation emailValidation = StaticMethods.emailValidation(txfEmail.getText());
        Validation passValidation = StaticMethods.passValidation(psfPass1.getText(), psfPass2.getText());
        if (!userIdValidation.isValid()) {
            lblError.setText(userIdValidation.getErrorMsg());
        } else if (!nameSurnameUsernameValidation.isValid()) {
            lblError.setText(nameSurnameUsernameValidation.getErrorMsg());
        } else if (!tlfNumberValidation.isValid()) {
            lblError.setText(tlfNumberValidation.getErrorMsg());
        } else if (!emailValidation.isValid()) {
            lblError.setText(emailValidation.getErrorMsg());
        } else if (!passValidation.isValid()) {
            lblError.setText(passValidation.getErrorMsg());
        } else {
            return true;
        }

        return false;
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
