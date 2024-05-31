package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import ibermatica_project.lang.Lang;
import ibermatica_project.model.Machine;
import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
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

public class MachineAddMenuController {
    @FXML
    MenuButton btnMenu;

    @FXML
    TextField txfSerialNumber, txfName;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboType, comoStatus;

    @FXML
    Label lblMachineTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblSerialNumber, lblName, lblMachineType, lblStatus;

    @FXML
    Button btnReset, btnAdd, btnVisualice, btnModify, btnUserManagement, btnMachineManagement, btnReserveManagement;

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

        comboType.getItems().addAll("Tornos", "Embalaje", "Procesados");
        comboType.setValue("Tornos");

        comoStatus.getItems().addAll("Operativa", "No operativa");
        comoStatus.setValue("Operativa");
    }

    @FXML
    private void langChange(String lang) throws IOException {
        if (langChangeBol) {
            Label[] labelList = {lblMachineTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblSerialNumber, lblName, lblMachineType, lblStatus};
            Label[] labelChangeList = Lang.langChangeLabel(lang, labelList, "machineAdd");

            Button[] buttonList = {btnAdd, btnReset, btnVisualice, btnModify};
            Button[] buttonChangeList = Lang.langChangeButton(lang, buttonList, "machineAdd");

            TextField[] textFieldList = {txfSerialNumber, txfName};
            TextField[] textFieldChangeList = Lang.langChangeTextField(lang, textFieldList, "machineAdd");

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

    public void add() throws IOException {
        boolean valid = checkDbInfo();
        if (valid) {
            String status;
            if ((String) comoStatus.getValue() == "Operativa") {
                status = "operational";
            } else {
                status = "not_operational";
            }

            Machine machine = new Machine(txfSerialNumber.getText(), txfName.getText(), null,
                (String) comboType.getValue(), status);

            boolean result = db.addMachine(machine);
            if (result == true) {
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Maquina agregada");
                alert.setHeaderText("La maquina se ha agregado correctamente");
                alert.show();
            } else {
                generateAlert("Hemos tenido problemas al agregar la maquina, por favor contacte con un Administrador");
            }
        }
    }

    private boolean checkDbInfo() throws IOException {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();

        boolean checkSerialNumer = true, checkName = true;

        for (Machine machine : machineList) {
            if (machine.getSerialNumber().equals(txfSerialNumber.getText().replaceAll("\\s", ""))) {
                generateAlert("El número de serie insertado ya existe en la Base de Datos");
                checkSerialNumer = false;
            } 
            if (machine.getName().equals(txfName.getText().replaceAll("\\s", ""))) {
                generateAlert("El nombre insertado ya existe en la Base de Datos");
                checkName = false;
            }
        }

        if (checkSerialNumer == false || checkName == false) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void restartInputs() throws IOException {
        txfName.setText(null);
        txfSerialNumber.setText(null);
        comboType.setValue("Tornos");
        comoStatus.setValue("Operativa");
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
    private void loadReservationManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationManagementMenu");
    }

    @FXML
    private void loadMachineManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineManagementMenu");
    }

    @FXML
    private void loadMachineModifyMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineModifyMenu");
    }
}
