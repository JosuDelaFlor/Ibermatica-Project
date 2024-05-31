package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
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

public class MachineModifyMenuController {
    @FXML
    MenuButton btnMenu;

    @FXML
    TextField txfSerialNumber, txfName, txfAdquisitionDate;

    @FXML
    Button btnSearch, btnModify, btnCreate, btnVisualize, btnUserManagement, btnMachineManagement, btnReserveManagement;

    @FXML
    Label lblSerialNumber, lblMachineTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblAdquisitionDate, lblMachineType, lblStatus, lblName;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboType, comboStatus;

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

        comboStatus.getItems().addAll("Operativa", "No operativa");
    }

    @FXML
    private void langChange(String lang) throws IOException {
        if (langChangeBol) {
            Label[] labelList = {lblSerialNumber, lblName, lblMachineTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblAdquisitionDate, lblMachineType, lblStatus};
            Label[] labelChangeList = Lang.langChangeLabel(lang, labelList, "machineModify");

            Button[] buttonList = {btnSearch, btnModify, btnCreate, btnVisualize};
            Button[] buttonChangeList = Lang.langChangeButton(lang, buttonList, "machineModify");

            TextField[] textFieldList = {txfSerialNumber, txfName, txfAdquisitionDate};
            TextField[] textFieldChangeList = Lang.langChangeTextField(lang, textFieldList, "machineModify");

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

    public void update() throws IOException {
        if (checkDbSerialNumber() && testInputs()) {
            Machine machine = new Machine(null, null, null, null, null);
            machine.setName(txfName.getText().replaceAll("\\s", ""));
            machine.setSerialNumber(txfSerialNumber.getText().replaceAll("\\s", ""));
            if (comboStatus.getValue() == "Operativa") {
                machine.setStatus("operational");
            } else {
                
                machine.setStatus("not_operational");
            }
            machine.setAcquisitionDate(LocalDate.parse(txfAdquisitionDate.getText().replaceAll("\\s", "")));
            machine.setType((String)comboType.getValue());
            boolean result = db.updateMachine(machine);
            if (result == true) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Operación completada");
                alert.setHeaderText("Los datos de la maquina se han actualizado exitosamente");
                alert.show();
            } else {
                generateAlert("Hemos tenido problemas al actualizar los datos de la maquina, por favor contacte con un Administrador");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void search() throws IOException {
        if (!txfSerialNumber.getText().replaceAll("\\s", "").equals("")) {            
            if (checkDbSerialNumber()) {
                Machine machine = db.searchSpecificMachine(txfSerialNumber.getText().replaceAll("\\s", ""));
                txfName.setText(machine.getName());
                txfAdquisitionDate.setText(machine.getAcquisitionDate().toString());
                if (machine.getStatus().equals("operational")) {
                    comboStatus.setValue("Operativa");
                } else {
                    comboStatus.setValue("No operativa");
                }

                if (machine.getType().equals("tornos")) {
                    comboType.setValue("tornos");
                } else if (machine.getType() == "embalaje") {
                    comboType.setValue("embalaje");
                } else {
                    comboType.setValue("procesados");
                }
                comboType.getItems().addAll("tornos", "embalaje", "procesados");
            }
        } else {
            generateAlert("El número de serie no puede estar vacio");
        }
    }

    private boolean checkDbSerialNumber() throws IOException {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();
        boolean valid = false;

        for (Machine machine : machineList) {
            if (machine.getSerialNumber().equals(txfSerialNumber.getText().replaceAll("\\s", ""))) {
                valid = true;
            }
        }
        if (valid) {
            return valid;
        } else {
            generateAlert("El número de serie insertado no existe en la base de datos");
            return false;
        }
    }

    private boolean testInputs() throws IOException {
        if (txfName.getText().equals("") || txfAdquisitionDate.getText().equals("")) {
            generateAlert("Todos los campos tienen que estar completados");
            return false;
        } else {
            return true;
        }
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
    private void loadMachineAddMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineAddMenu");
    }
}
