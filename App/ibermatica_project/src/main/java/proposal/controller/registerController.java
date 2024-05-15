package proposal.controller;

import java.io.IOException;
import java.time.LocalDate;
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

public class RegisterController {
    @FXML
    Button btnBack, btnLogOut, btnRegister, btnGenerateEmail, btnGenerateUser, btnReset;

    @FXML
    TextField txfId, txfName, txfSurname, txfEmail, txfTlfNumber, txfUser; 

    @FXML
    PasswordField psfPass1, psfPass2;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboType;

    @FXML
    Label lblError, lblSuccess;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    @SuppressWarnings("unchecked")
    @FXML
    protected void initialize() { // Load ComboBox data
        List<String> roleNameList = new ArrayList<String>();
        roleNameList.addAll(Arrays.asList("Administrador", "Empleado"));

        for (int i = 0; i < roleNameList.size(); i++) {
            comboType.getItems().add(roleNameList.get(i));
        }

        comboType.setValue(roleNameList.get(1));
    }

    @SuppressWarnings("exports")
    @FXML
    public User register() throws Exception {
        boolean valid = testValidations();
        if (valid) {
            lblError.setText(null);
            int type = 1;
            if ((String) comboType.getValue() == "Administrador") {
                type = 0;
            }
            byte[] passBytes = StaticMethods.encriptPass(psfPass1.getText());
            String pass = StaticMethods.byteToString(passBytes);

            User user = new User(txfId.getText(), txfName.getText(), txfSurname.getText(), txfEmail.getText(),
                    Integer.parseInt(txfTlfNumber.getText()), txfUser.getText(), pass, LocalDate.now(), type);
            int result = db.addNewUser(user);
            if (result == 1) {
                lblSuccess.setText("El usuario se a agregado correctamente");
            } else {
                lblError.setText("Hemos tenido problemas al agregar el usuario");
            }
        }
        return null;
    }

    /**
     * Checks if the data inserted by the user contains the required characteristics
     * @return
     * @throws IOException
     */

    @FXML
    private boolean testValidations() throws IOException {
        boolean valid = false;

        Validation userIdValidation = StaticMethods.userIdValidation(txfId.getText());
        Validation nameSurnameUsernameValidation = StaticMethods.nameSurnameUsernameValidation(txfName.getText(), txfSurname.getText(), txfUser.getText());
        Validation tlfNumberValidation = StaticMethods.tlfNumberValidation(txfTlfNumber.getText());
        Validation emailValidation = StaticMethods.emailValidation(txfEmail.getText());
        Validation passValidation = StaticMethods.passValidation(psfPass1.getText(), psfPass2.getText());
        
        boolean userIdV = true, nameSurnameUsernameV = true, tlfNumberV = true, emailV = true, passV = true;
        
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
        if (!passValidation.isValid()) {
            lblError.setText(passValidation.getErrorMsg());
            passV = false;
        }

        if (userIdV == false || nameSurnameUsernameV == false || tlfNumberV == false || emailV == false || passV == false) {
            valid = false;
        } else {
            valid = checkDbInfo();
        }
    
        return valid;
    }

    private boolean checkDbInfo() {
        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();

        boolean checkId = true, checkEmail = true, checkTlfNumber = true, checkUsername = true;

        for (User user : userList) {
            if (user.getUserId().equals(txfId.getText().replaceAll("\\s", ""))) {
                lblError.setText("El DNI insertado ya existe en la Base de Datos");
                checkId = false;
            } 
            if (user.getEmail().equals(txfEmail.getText().replaceAll("\\s", ""))) {
                lblError.setText("El correo insertado ya existe en la Base de Datos");
                checkEmail = false;
            }
            if (Integer.toString(user.getTlfNum()).equals(txfTlfNumber.getText().replaceAll("\\s", ""))) {
                lblError.setText("El numero de telefono insertado ya existe en la Base de Datos");
                checkTlfNumber = false;
            }
            if (user.getUsername().equals(txfUser.getText().replaceAll("\\s", ""))) {
                lblError.setText("El nombre de usuario insertato ya existe en la Base de Datos");
                checkUsername = false;
            }
        }

        if (checkId == false || checkEmail == false || checkTlfNumber == false || checkUsername == false) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void generateEmail() throws IOException {
        txfEmail.setText(setDefaultText()+"@ayesa.com");
    }

    @FXML
    private void generateUsername() throws IOException {
        txfUser.setText(setDefaultText());
    }

    private String setDefaultText() {
        String name = txfName.getText(), surname = txfSurname.getText();
        if (txfName.getText().equals("")) {
            name = "nombre";
        } 
        if (txfSurname.getText().equals("")) {
            surname = "apellido";
        } else {
            name = txfName.getText().replaceAll("\\s", "").toLowerCase();
            surname = txfSurname.getText().replaceAll("\\s", "").toLowerCase();
        }

        return surname+"."+name;
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void restartInputs() throws IOException {
        txfId.setText(null);
        txfName.setText(null);
        txfSurname.setText(null);
        txfEmail.setText(null);
        txfTlfNumber.setText(null);
        txfUser.setText(null);
        psfPass1.setText(null);
        psfPass2.setText(null);
        comboType.setValue("Empleado");
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
        App.setRoot("fxml/admMenu");;
    }
}
