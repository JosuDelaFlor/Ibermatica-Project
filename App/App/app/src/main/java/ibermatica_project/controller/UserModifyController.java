package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserModifyController {
    @FXML
    MenuButton btnMenu;

    @FXML
    Button btnUserManagement, btnMachineManagement, btnReserveManagement, btnCreate, btnVisualice, btnModify, btnDelete, btnSearch, btnPasswordRestart;

    @FXML
    TextField txfId, txfName, txfSurname, txfUser, txfEmail, txfTlfNumber;

    @FXML
    Label lblUserTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblRegisterDate, lblRegisterday, lblId, lblName, lblSurname, lblEmail, lblPhoneNumber, lblUserName, lblUserType;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboType;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    static User modifyUser;

    static Alert alert = new Alert(Alert.AlertType.WARNING);

    static boolean langChangeBol = AdmMenuController.getLangChangeBol();

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
    }

    @FXML
    private void langChange(String lang) throws IOException{
        if (langChangeBol) {
            Label[] labelList = {lblUserTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblId, lblName, lblSurname, lblEmail, lblPhoneNumber, lblUserName, lblUserType, lblRegisterDate};
            Label[] labelChangeList = Lang.langChangeLabel(lang, labelList, "userModify");

            Button[] buttonList = {btnPasswordRestart, btnVisualice, btnModify, btnCreate, btnSearch};
            Button[] buttonChangeList = Lang.langChangeButton(lang, buttonList, "userModify");

            TextField[] textFieldList = {txfId, txfName, txfSurname, txfEmail, txfTlfNumber, txfUser};
            TextField[] textFieldChangeList = Lang.langChangeTextField(lang, textFieldList, "userModify");

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

    public void modify() throws IOException {
        boolean valid = testValidations(), validId = checkDbId(txfId.getText());
        if (validId == false) {
            generateAlert("El DNI insertado no existe");
        } else if (valid) {
            int type = 1;
            if ((String) comboType.getValue() == "Administrador") {
                type = 0;
            }

            User user = new User(txfId.getText(), txfName.getText(), txfSurname.getText(), txfEmail.getText(),
                        Integer.parseInt(txfTlfNumber.getText()), txfUser.getText(), null, null, type);
            int result = db.updateUser(user);
            if (result == 1) {
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Actialización completada");
                alert.setHeaderText("El usuario ha sido actualizado correctamente");
                alert.show();
            } else {
                generateAlert("Hemos tenido problemas al actializar el usuario");
            }
        }   
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void search() throws IOException {
        boolean validId = checkDbId(txfId.getText());
        if (validId) {
            modifyUser = db.searchSpecificUser(txfId.getText());
            txfName.setText(modifyUser.getName());
            txfSurname.setText(modifyUser.getSurname());
            txfEmail.setText(modifyUser.getEmail());
            txfUser.setText(modifyUser.getUsername());
            txfTlfNumber.setText(Integer.toString(modifyUser.getTlfNum()));
            lblRegisterday.setText(modifyUser.getRegisterdate().toString());
            
            if (modifyUser.getType() == 0) {
                comboType.setValue("Administrador");
                comboType.getItems().add("Empleado");
            } else {
                comboType.setValue("Empleado");
                comboType.getItems().add("Admnistrador");
            }    
        } else {
            generateAlert("El DNI insertado no existe");
        }
    }

    @FXML
    private boolean testValidations() throws IOException {
        boolean valid = false;
        
        Validation userIdValidation = Validations.userIdValidation(txfId.getText());
        Validation nameSurnameUsernameValidation = Validations.nameSurnameUsernameValidation(txfName.getText(), txfSurname.getText(), txfUser.getText());
        Validation tlfNumberValidation = Validations.tlfNumberValidation(txfTlfNumber.getText());
        Validation emailValidation = Validations.emailValidation(txfEmail.getText());

        boolean userIdV = true, nameSurnameUsernameV = true, tlfNumberV = true, emailV = true;

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

    @SuppressWarnings("exports")
    public static User getModifyUser() {
        return modifyUser;
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
    private void loadRegisterMenuScene() throws IOException {
        SceneController.setRoot("fxml/registerMenu");
    }

    @FXML
    private void loadRestartUserPasswordScene() throws IOException {
        if (checkDbId(txfId.getText())) {
            modifyUser = new User(txfId.getText(), txfName.getText(), txfSurname.getText(), txfEmail.getText(),
            1, txfUser.getText(), null, null, 1);
            
            SceneController.loadRestartUserPasswordScene();
        } else {
            generateAlert("El DNI insertado no existe");
        }
    }

    @FXML
    private void loadDeleteUserMenuScene() throws IOException {
        SceneController.setRoot("fxml/deleteUserMenu");
    }

    @FXML
    private void loadReservationManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationManagementMenu");
    }

    @FXML
    private void loadMachineManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineManagementMenu");
    }
}
