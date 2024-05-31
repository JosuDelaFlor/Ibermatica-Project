package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;

import ibermatica_project.lang.Lang;
import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdmMenuController {

    @FXML
    Label lblName, lblSurname, lblUsername, lblEmail, lblTlfNumber, lblType, lblTitleAdm,
        lblNameAdm, lblSurnameAdm, lblUserNameAdm, lblEmailAdm, lblPhoneNumberAdm, lblUserTypeAdm, lblUserManagement,
        lblMachineManagement, lblReservesManagement;

    @FXML
    MenuButton btnMenu;

    @FXML
    Button btnUserManagement, btnMachineManagement, btnReserveManagement, btnSpanish, btnEnglish, btnRestartPassword;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null); 

    static boolean langChangeBol;
    
    @FXML
    public void initialize() throws IOException {        
        User loggedUser = IndexController.getLoggedUser();

        lblTitleAdm.setText("Bienvenido " + loggedUser.getName());
        lblName.setText(loggedUser.getName());
        lblSurname.setText(loggedUser.getSurname());
        lblUsername.setText(loggedUser.getUsername());
        lblEmail.setText(loggedUser.getEmail());
        lblTlfNumber.setText(Integer.toString(loggedUser.getTlfNum()));

        if (loggedUser.getType() == 0) {
            lblType.setText("Administrador");
        } else {
            lblType.setText("Empleado");
        }

        MenuItem menuItem1 = new MenuItem("Cerrar sesión");
        btnMenu.setText(loggedUser.getName());
        btnMenu.getItems().add(menuItem1);

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

        if (langChangeBol) {
            langChange("English");
            btnEnglish.setVisible(false);
            btnEnglish.setDisable(true);
            btnEnglish.setLayoutX(14);
            btnSpanish.setLayoutX(94);
            btnSpanish.setVisible(true);
            btnSpanish.setDisable(false);
            langChangeBol = true;
        } else {
            langChange("Spanish");
            langChangeBol = false;
            btnSpanish.setVisible(false);
            btnSpanish.setDisable(true);
            btnSpanish.setLayoutX(14);
            btnEnglish.setLayoutX(94);
            btnEnglish.setVisible(true);
            btnEnglish.setDisable(false);
        }
    }

    private void langChange(String langName) throws IOException {
        Label[] labelList = {lblTitleAdm, lblNameAdm, lblSurnameAdm, lblUserNameAdm, lblEmailAdm, lblPhoneNumberAdm, lblUserTypeAdm, lblUserManagement,
            lblMachineManagement, lblReservesManagement};
        Label[] labelChangeList = Lang.langChangeLabel(langName, labelList, "admMenu");

        Button[] buttonList = {btnRestartPassword};
        Button[] buttonChangeList = Lang.langChangeButton(langName, buttonList, "admMenu");

        for (int i = 0; i < labelChangeList.length; i++) {
            labelList[i].setText(labelChangeList[i].getText());
        }

        for (int i = 0; i < buttonChangeList.length; i++) {
            buttonList[i].setText(buttonChangeList[i].getText());
        }
    }

    @FXML
    private void btnSpanishActionPerformed() throws IOException {
        langChangeBol = false;
        langChange("Spanish");
        btnSpanish.setVisible(false);
        btnSpanish.setDisable(true);
        btnSpanish.setLayoutX(14);
        btnEnglish.setLayoutX(94);
        btnEnglish.setVisible(true);
        btnEnglish.setDisable(false);
    }

    @FXML
    private void btnEnglishActionPerformed() throws IOException {
        langChangeBol = true;
        langChange("English");
        btnEnglish.setVisible(false);
        btnEnglish.setDisable(true);
        btnEnglish.setLayoutX(14);
        btnSpanish.setLayoutX(94);
        btnSpanish.setVisible(true);
        btnSpanish.setDisable(false);
    }

    public static boolean getLangChangeBol() {
        return langChangeBol;
    }

    @FXML
    private void loadUserManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/userManagementMenu");
    }

    @FXML
    private void loadRestartPasswordScene() throws IOException {
        SceneController.loadRestartPasswordScene();
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