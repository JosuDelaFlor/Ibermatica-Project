package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class MachineManagementMenuController {
    @FXML
    MenuButton btnMenu;

    @SuppressWarnings("rawtypes")
    @FXML
    TableView tblViewMachine;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboSearchType, comboSearchInput;

    @FXML
    TextField txfSearch;

    @FXML
    Label lblMachineTitle, lblUserManagement, lblMachineManagement, lblReservesManagement;

    @FXML
    Button btnSearch, btnModify, btnCreate, btnDelete, btnUserManagement, btnMachineManagement, btnReserveManagement;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    static ArrayList<Machine> machineList = new ArrayList<Machine>();

    static Machine deletMachine;

    static boolean langChangeBol = AdmMenuController.getLangChangeBol();
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @FXML
    protected void initialize() throws IOException {
        if (langChangeBol) {
            Label[] labelList = {lblMachineTitle, lblUserManagement, lblMachineManagement, lblReservesManagement};
            Label[] labelChangeList = Lang.langChangeLabel("English", labelList, "machineManagement");

            Button[] buttonList = {btnSearch, btnModify, btnCreate};
            Button[] buttonChangeList = Lang.langChangeButton("English", buttonList, "machineManagement");

            TextField[] textFieldList = {txfSearch};
            TextField[] textFieldChangeList = Lang.langChangeTextField("English", textFieldList, "machineManagement");

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

        comboSearchInput.setEditable(false);

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

        List<String> searchTypeList = new ArrayList<String>();
        searchTypeList.addAll(Arrays.asList("Número de serie", "Nombre", "Fecha de adquisición", "Tipo", "Estado"));
    
        for (int i = 0; i < searchTypeList.size(); i++) {
            comboSearchType.getItems().add(searchTypeList.get(i));
        }
        comboSearchType.setValue(searchTypeList.get(0));

        TableColumn<Machine, String> column1 = new TableColumn<>("Número de serie");
        column1.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        TableColumn<Machine, String> column2 = new TableColumn<>("Nombre");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Machine, String> column3 = new TableColumn<>("Fecha de adquisición");
        column3.setCellValueFactory(new PropertyValueFactory<>("adquisitionDate"));
        TableColumn<Machine, String> column4 = new TableColumn<>("Tipo");
        column4.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Machine, String> column5 = new TableColumn<>("Estado");
        column5.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn actionCol = new TableColumn("Eliminar");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionDelete"));

        tblViewMachine.getColumns().add(column1);
        tblViewMachine.getColumns().add(column2);
        // tblViewMachine.getColumns().add(column3);
        tblViewMachine.getColumns().add(column4);
        tblViewMachine.getColumns().add(column5);
        tblViewMachine.getColumns().add(actionCol);

        machineList = db.searchAllMachines();

        generateTableView(machineList, actionCol);
    }

    @SuppressWarnings("rawtypes")
    @FXML
    private void delete(String serialNumber) throws IOException {
        boolean result = db.deleteMachine(serialNumber);
        if (result == false) {
            tblViewMachine.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Operación completada");
            alert.setHeaderText("El usuario se ha aliminado por completo");
            machineList = db.searchAllMachines();
            TableColumn actionCol = new TableColumn("Action");
            generateTableView(machineList, actionCol);
        } else {
            SceneController.loadMachineDeleteErrorScene();
        }
    }

    @SuppressWarnings("rawtypes")
    @FXML
    private void search() throws IOException {
        TableColumn actionCol = new TableColumn("Action");

        if ((String) comboSearchType.getValue() == "Número de serie") {
            if (checkInputs(0)) {
                machineList = db.searchMachineWithSerialNumber(txfSearch.getText());
                generateTableView(machineList, actionCol);
            }
        } else if ((String) comboSearchType.getValue() == "Nombre") {
            if (checkInputs(1)) {
                machineList = db.searchMachineWithName(txfSearch.getText());
                generateTableView(machineList, actionCol);
            }
        } else if ((String) comboSearchType.getValue() == "Fecha de adquisición") {
            if (checkInputs(2)) {
                machineList = db.searchMachineWithAdquisitonDate(LocalDate.parse(txfSearch.getText()));
                generateTableView(machineList, actionCol);
            }
        } else if ((String) comboSearchType.getValue() == "Tipo") {
            if ((String) comboSearchInput.getValue() == "Tornos") {
                machineList = db.searchMachineWithType("tornos");
            } else if ((String) comboSearchInput.getValue() == "Embalaje") {
                machineList = db.searchMachineWithType("embalaje");
            } else {
                machineList = db.searchMachineWithType("procesados");
            }
            generateTableView(machineList, actionCol);
        } else if ((String) comboSearchType.getValue() == "Estado") {
            if ((String) comboSearchInput.getValue() == "No operativa") {
                machineList = db.searchMachineWithStatus("not_operational");
            } else {
                machineList = db.searchMachineWithStatus("operational");
            }
            generateTableView(machineList, actionCol);
        } 
    }

    @FXML
    private boolean checkInputs(int searchType) throws IOException {
        boolean valid = true;

        if (searchType == 0) { // searchType 0 = SerialNumber
            if (!checkDbSerialNumber(txfSearch.getText().replaceAll("\\s", ""))) {
                generateAlert(0);
                valid = false;
            }
        } else if (searchType == 1) { // searchType 1 = name
            if (!checkDbName(txfSearch.getText().replaceAll("\\s", ""))) {
                generateAlert(1);
                valid = false;
            }
        } else if (searchType == 2) { // searchType 2 = adquisitionDate
            LocalDate adquisitionDate = LocalDate.parse(txfSearch.getText());
            if (!checkValidDate(txfSearch.getText().replaceAll("\\s", ""), "yyyy-mm-dd")) {
                generateAlert(2);
            } else if (!checkDbAdquisitionDate(adquisitionDate)) {
                generateAlert(3);
                valid = false;
            }
        } 
        return valid;
    }

    @FXML
    private boolean checkDbSerialNumber(String serialNumber) throws IOException {
        boolean valid = false;
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();

        for (Machine machine : machineList) {
            if (machine.getSerialNumber().equals(serialNumber)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbName(String name) throws IOException {
        boolean valid = false;
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();

        for (Machine machine : machineList) {
            if (machine.getName().equals(name)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbAdquisitionDate(LocalDate adquisitionDate) throws IOException {
        boolean valid = false;
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();

        for (Machine machine : machineList) {
            if (machine.getAcquisitionDate().equals(adquisitionDate)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbType(String type) throws IOException {
        boolean valid = false;
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();

        for (Machine machine : machineList) {
            if (machine.getType().equals(type)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbStatus(String status) throws IOException {
        boolean valid = false;
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();

        for (Machine machine : machineList) {
            if (machine.getStatus().equals(status)) {
                valid = true;
            }
        }
        return valid;
    }

    private boolean checkValidDate(String date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        df.setLenient(false);
        try {
            df.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @FXML
    private void generateTableView(ArrayList<Machine> machineList, TableColumn actionCol) throws IOException {
        tblViewMachine.getItems().clear();

        for (int i = 0; i < machineList.size(); i++) {
            tblViewMachine.getItems().add(new Machine(machineList.get(i).getSerialNumber(), machineList.get(i).getName(),
                machineList.get(i).getAcquisitionDate(), machineList.get(i).getType(), machineList.get(i).getStatus()));
        }

        Callback<TableColumn<Machine, String>, TableCell<Machine, String>> cellFactory
                = //
                new Callback<TableColumn<Machine, String>, TableCell<Machine, String>>() {
            @Override
            public TableCell call(final TableColumn<Machine, String> param) {
                final TableCell<Machine, String> cell = new TableCell<Machine, String>() {

                    final Button btn = new Button();
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        FileInputStream input2;
                        try {
                            input2 = new FileInputStream("src\\main\\resources\\ibermatica_project\\img\\x.png");
                            Image image2 = new Image(input2);
                            ImageView imageView2 = new ImageView(image2);
                            btn.setGraphic(imageView2);
                            imageView2.setFitWidth(20);
                            imageView2.setFitHeight(20); 
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Machine machine = getTableView().getItems().get(getIndex());
                                try {
                                    deletMachine = db.searchSpecificMachine(machine.getSerialNumber());
                                    delete(machine.getSerialNumber());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        actionCol.setCellFactory(cellFactory);
    }

    public static Machine getDeleteMachine() {
        return deletMachine;
    }

    @SuppressWarnings({ "unchecked" })
    @FXML
    private void comoboBoxSelection() throws IOException {
        boolean changeComboBox = false;
        comboSearchInput.getItems().clear();
        if ((String) comboSearchType.getValue() == "Tipo") {
            List<String> machineTypeList = new ArrayList<String>();
            machineTypeList.addAll(Arrays.asList("Tornos", "Embalaje", "Procesados"));
            for (int i = 0; i < machineTypeList.size(); i++) {
                comboSearchInput.getItems().add(machineTypeList.get(i));
            }
            comboSearchInput.setValue(machineTypeList.get(0));
            changeComboBox = true;
        } else if ((String) comboSearchType.getValue() == "Estado") {
            comboSearchInput.getItems().add("Operativa");
            comboSearchInput.getItems().add("No operativa");
            comboSearchInput.setValue("Operativa");
            changeComboBox = true;
        }

        if (changeComboBox) {
            txfSearch.setVisible(false);
            txfSearch.setEditable(false);
            comboSearchInput.setLayoutX(203);
            comboSearchInput.setLayoutY(88);
            comboSearchInput.setVisible(true);
            txfSearch.setLayoutX(448);
            txfSearch.setLayoutY(88);
        } else {
            txfSearch.setVisible(true);
            txfSearch.setEditable(true);
            comboSearchInput.setLayoutX(448);
            comboSearchInput.setLayoutY(88);
            comboSearchInput.setEditable(false);
            comboSearchInput.setVisible(false);
            txfSearch.setLayoutX(203);
            txfSearch.setLayoutY(88);
        }
    }

    @FXML
    private void generateAlert(int error) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cuidado!");

        if (error == 0) {
            alert.setHeaderText("El Número de serie insertado no existe en la base de datos");
        } else if (error == 1) {
            alert.setHeaderText("El Nombre insertado no existe en la base de datos");
        } else if (error == 2) {
            alert.setHeaderText("No existe ninguna maquina con la fecha de adquisición insertada");
        } else if (error == 3) {
            alert.setHeaderText("El formato de la fecha de adquisición es incorrecto (yyyy-mm-dd)");
        } 
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

    @FXML
    private void loadMachineModifyMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineModifyMenu");
    }
}
