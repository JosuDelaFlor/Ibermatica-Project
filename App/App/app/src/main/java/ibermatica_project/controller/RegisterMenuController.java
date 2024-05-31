package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ibermatica_project.lang.Lang;
import ibermatica_project.model.Validation;
import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import ibermatica_project.model.base.Validations;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RegisterMenuController {
    @FXML
    MenuButton btnMenu;

    @FXML
    Button btnUserManagement, btnMachineManagement, btnReserveManagement, btnVisualice, btnModify, btnDelete, btnRegister, btnGenerateEmail, btnGenerateUser, btnReset;

    @FXML
    Label lblUserTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblId, lblName, lblSurname, lblEmail, lblPhoneNumber, lblUserName, lblPassword1, lblPassword2, lblUserType; 

    @FXML
    TextField txfId, txfName, txfSurname, txfEmail, txfTlfNumber, txfUser;

    @FXML
    PasswordField psfPass1, psfPass2;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboType;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    static Alert alert = new Alert(Alert.AlertType.WARNING);

    static boolean langChangeBol = AdmMenuController.getLangChangeBol();
    
    @SuppressWarnings("unchecked")
    @FXML
    protected void initialize() throws IOException {
        langChangeBol = AdmMenuController.getLangChangeBol();
        if (langChangeBol) {
            langChange("English");
        } else {
            langChange("Spanish");
        }

        User loggedUser = IndexController.getLoggedUser();

        MenuItem menuItem1 = new MenuItem("Cerrar sesión"), menuItem2 = new MenuItem("Inicio");
        btnMenu.setText(loggedUser.getName());
        btnMenu.getItems().addAll(menuItem1, menuItem2);

        FileInputStream input1 = new FileInputStream("src\\main\\resources\\ibermatica_project\\img\\menu.png");
        Image image1 = new Image(input1);
        ImageView imageView1 = new ImageView(image1);
        btnMenu.setGraphic(imageView1);
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);

        FileInputStream input2 = new FileInputStream("src\\main\\resources\\ibermatica_project\\img\\logout.png");
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        menuItem1.setGraphic(imageView2);
        imageView2.setFitWidth(20);
        imageView2.setFitHeight(20); 

        FileInputStream input3 = new FileInputStream("src\\main\\resources\\ibermatica_project\\img\\home.png");
        Image image3 = new Image(input3);
        ImageView imageView3 = new ImageView(image3);
        menuItem2.setGraphic(imageView3);
        imageView3.setFitWidth(20);
        imageView3.setFitHeight(20);

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    SceneController.loadLoginScene();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        menuItem1.setOnAction(event1);

        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    SceneController.setRoot("fxml/admMenu");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        menuItem2.setOnAction(event2);

        List<String> roleNameList = new ArrayList<String>();
        roleNameList.addAll(Arrays.asList("Administrador", "Empleado"));

        for (int i = 0; i < roleNameList.size(); i++) {
            comboType.getItems().add(roleNameList.get(i));
        }
        comboType.setValue(roleNameList.get(1));
    }

    @FXML
    private void langChange(String lang) throws IOException {
        if (langChangeBol) {
            Label[] labelList = {lblUserTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblId, lblName, lblSurname, lblEmail, lblPhoneNumber, lblUserName, lblPassword1, lblPassword2, lblUserType};
            Label[] labelChangeList = Lang.langChangeLabel(lang, labelList, "register");

            Button[] buttonList = {btnRegister, btnGenerateEmail, btnGenerateUser, btnReset, btnVisualice, btnModify};
            Button[] buttonChangeList = Lang.langChangeButton(lang, buttonList, "register");

            TextField[] textFieldList = {txfId, txfName, txfSurname, txfEmail, txfTlfNumber, txfUser};
            TextField[] textFieldChangeList = Lang.langChangeTextField(lang, textFieldList, "register");

            for (int i = 0; i < labelChangeList.length; i++) {
                labelList[i].setText(labelChangeList[i].getText());
            }

            for (int i = 0; i < buttonChangeList.length; i++) {
                buttonList[i].setText(buttonChangeList[i].getText());
            }

            for (int i = 0; i < textFieldChangeList.length; i++) {
                textFieldList[i].setPromptText(textFieldChangeList[i].getPromptText());
            }
        }
    }

    public void register() throws IOException {
        boolean valid = testValidations();
        if (valid) {
            int type = 1;
            if ((String) comboType.getValue() == "Administrador") {
                type = 0;
            }
            User user = new User(txfId.getText(), txfName.getText(), txfSurname.getText(), txfEmail.getText(),
                    Integer.parseInt(txfTlfNumber.getText()), txfUser.getText(), psfPass1.getText(), LocalDate.now(), type);
            int result = db.addNewUser(user);
            if (result == 1) {
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Registro completado");
                alert.setHeaderText("El usuario se ha dado de alta correctamente");
                alert.show();
            } else {
                generateAlert("Hemos tenido problemas al dar de alta al usuario, por favor contacte con un Administrador");
            }
        }
    }

    /**
     * Checks if the data inserted by the user contains the required characteristics
     * @return
     * @throws IOException
     */

    @FXML
    private boolean testValidations() throws IOException {
        boolean valid = false;

        Validation userIdValidation = Validations.userIdValidation(txfId.getText());
        Validation nameSurnameUsernameValidation = Validations.nameSurnameUsernameValidation(txfName.getText(), txfSurname.getText(), txfUser.getText());
        Validation tlfNumberValidation = Validations.tlfNumberValidation(txfTlfNumber.getText());
        Validation emailValidation = Validations.emailValidation(txfEmail.getText());
        Validation passValidation = Validations.passValidation(psfPass1.getText(), psfPass2.getText());
        
        boolean userIdV = true, nameSurnameUsernameV = true, tlfNumberV = true, emailV = true, passV = true;
        
        if (!userIdValidation.isValid()) {
            generateAlert(userIdValidation.getErrorMsg());
            userIdV = false;
        }
        if (!nameSurnameUsernameValidation.isValid()) {
            generateAlert(nameSurnameUsernameValidation.getErrorMsg());
            nameSurnameUsernameV = false;
        }
        if (!tlfNumberValidation.isValid()) {
            generateAlert(tlfNumberValidation.getErrorMsg());
            tlfNumberV = false;
        }
        if (!emailValidation.isValid()) {
            generateAlert(emailValidation.getErrorMsg());
            emailV = false;
        }
        if (!passValidation.isValid()) {
            generateAlert(passValidation.getErrorMsg());
            passV = false;
        }

        if (userIdV == false || nameSurnameUsernameV == false || tlfNumberV == false || emailV == false || passV == false) {
            valid = false;
        } else {
            valid = checkDbInfo();
        }
        return valid;
    }

    private boolean checkDbInfo() throws IOException {
        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();

        boolean checkId = true, checkEmail = true, checkTlfNumber = true, checkUsername = true;

        for (User user : userList) {
            if (user.getUserId().equals(txfId.getText().replaceAll("\\s", ""))) {
                generateAlert("El DNI insertado ya existe en la Base de Datos");
                checkId = false;
            } 
            if (user.getEmail().equals(txfEmail.getText().replaceAll("\\s", ""))) {
                generateAlert("El correo insertado ya existe en la Base de Datos");
                checkEmail = false;
            }
            if (Integer.toString(user.getTlfNum()).equals(txfTlfNumber.getText().replaceAll("\\s", ""))) {
                generateAlert("El numero de telefono insertado ya existe en la Base de Datos");
                checkTlfNumber = false;
            }
            if (user.getUsername().equals(txfUser.getText().replaceAll("\\s", ""))) {
                generateAlert("El nombre de usuario insertato ya existe en la Base de Datos");
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
    private void generateAlert(String error) throws IOException {
        alert.setTitle("Cuidado!");
        alert.setHeaderText(error);
        alert.show();
    }

    @FXML
    private void loadUserManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/userManagementMenu");
    }

    @FXML
    private void loadUserModifyMenuScene() throws IOException {
        SceneController.setRoot("fxml/userModifyMenu");
    }

    @FXML
    private void loadDeleteUserMenuScene() throws IOException {
        SceneController.setRoot("fxml/deleteUserMenu");
    }

    @FXML
    private void loadMachineManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineManagementMenu");
    }

    @FXML
    private void loadReservationManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationManagementMenu");
    }
}
