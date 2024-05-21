package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ibermatica_project.model.SimpleUser;
import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserManagementMenuController {
    @FXML
    MenuButton btnMenu;

    @SuppressWarnings("rawtypes")
    @FXML
    TableView tblViewUser;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboSearchType;

    @FXML
    TextField txfSearch;

    @FXML
    Button btnSearch, btnModify, btnCreate, btnDelete, btnUserManagement, btnMachineManagement, btnReserveManagement;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    @SuppressWarnings("unchecked")
    @FXML
    protected void initialize() throws IOException {
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

        ArrayList<SimpleUser> simpleUsersList = new ArrayList<SimpleUser>();
        simpleUsersList = db.searchAllSimpleUsers();

        TableColumn<SimpleUser, String> column1 = new TableColumn<>("DNI");
        column1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<SimpleUser, String> column2 = new TableColumn<>("Nombre");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SimpleUser, String> column3 = new TableColumn<>("Apellido");
        column3.setCellValueFactory(new PropertyValueFactory<>("surname"));
        TableColumn<SimpleUser, String> column4 = new TableColumn<>("Correo");
        column4.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<SimpleUser, String> column5 = new TableColumn<>("Usuario");
        column5.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<SimpleUser, String> column6 = new TableColumn<>("Tipo");
        column6.setCellValueFactory(new PropertyValueFactory<>("type"));

        tblViewUser.getColumns().add(column1);
        tblViewUser.getColumns().add(column2);
        tblViewUser.getColumns().add(column3);
        tblViewUser.getColumns().add(column4);
        tblViewUser.getColumns().add(column5);
        tblViewUser.getColumns().add(column6);

        for (int i = 0; i < simpleUsersList.size(); i++) {
            tblViewUser.getItems().add(new SimpleUser(simpleUsersList.get(i).getUserId(), simpleUsersList.get(i).getName(), simpleUsersList.get(i).getSurname(),
                simpleUsersList.get(i).getEmail(), simpleUsersList.get(i).getTlfNum(), simpleUsersList.get(i).getUsername(), simpleUsersList.get(i).getType()));
        }

        column4.setPrefWidth(170);

        List<String> searchTypeList = new ArrayList<String>();
        searchTypeList.addAll(Arrays.asList("DNI", "Nombre", "Apellido", "Numero de telefono"));
    
        for (int i = 0; i < searchTypeList.size(); i++) {
            comboSearchType.getItems().add(searchTypeList.get(i));
        }
        comboSearchType.setValue(searchTypeList.get(0));
    }

    @FXML
    private void search() throws IOException {
        ArrayList<SimpleUser> simpleUsersList = new ArrayList<SimpleUser>();

        if ((String) comboSearchType.getValue() == "DNI") {
            if (checkInputs(0)) {
                simpleUsersList = db.searchSimpleUserWithId(txfSearch.getText());
                generateSpecificTable(simpleUsersList);
            }
        } else if ((String) comboSearchType.getValue() == "Nombre") {
            if (checkInputs(1)) {
                simpleUsersList = db.searchSimpleUserWithName(txfSearch.getText());
                generateSpecificTable(simpleUsersList);
            }
        } else if ((String) comboSearchType.getValue() == "Apellido") {
            if (checkInputs(2)) {
                simpleUsersList = db.searchSimpleUserWithSurname(txfSearch.getText());
                generateSpecificTable(simpleUsersList);
            }
        } else {
            if (checkInputs(3)) {
                simpleUsersList = db.searchSimpleUserWithTlfNumber(Integer.parseInt(txfSearch.getText()));
                generateSpecificTable(simpleUsersList);
            }
        }
    }

    @FXML
    private boolean checkInputs(int searchType) throws IOException {
        boolean valid = true;

        if (txfSearch.getText().equals("")) {
            generateAlert(4);
            return false;
        } else {
            if (searchType == 0) { // searchType 0 = user_id
                if (!checkDbUserId(txfSearch.getText().replaceAll("\\s", ""))) {
                    generateAlert(0);
                    valid = false;
                }
            } else if (searchType == 1) { // searchType 1 = name
                if (!checkDbName(txfSearch.getText().replaceAll("\\s", ""))) {
                    generateAlert(1);
                    valid = false;
                }
            } else if (searchType == 2) { // searchType 2 = surname
                if (!checkDbSurname(txfSearch.getText().replaceAll("\\s", ""))) {
                    generateAlert(2);
                    valid = false;
                }
            } else if (searchType == 3) { // seachType 3 = tlfNumber
                try {
                    if (!checkDbTlfNumer(Integer.parseInt(txfSearch.getText()))) {
                        generateAlert(3);
                        valid = false;
                    }
                } catch (Exception e) {
                    generateAlert(5);
                    valid = false;
                }
            } 
        } 
        return valid;
    }

    @FXML
    private boolean checkDbUserId(String userId) throws IOException {
        boolean valid = false;
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        simpleUserList = db.searchAllSimpleUsers();

        for (SimpleUser simpleUser : simpleUserList) {
            if (simpleUser.getUserId().equals(userId)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbName(String name) throws IOException {
        boolean valid = false;
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        simpleUserList = db.searchAllSimpleUsers();

        for (SimpleUser simpleUser : simpleUserList) {
            if (simpleUser.getName().equals(name)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbSurname(String surname) throws IOException {
        boolean valid = false;
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        simpleUserList = db.searchAllSimpleUsers();

        for (SimpleUser simpleUser : simpleUserList) {
            if (simpleUser.getSurname().equals(surname)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbTlfNumer(int tlfNumber) throws IOException {
        boolean valid = false;
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        simpleUserList = db.searchAllSimpleUsers();

        for (SimpleUser simpleUser : simpleUserList) {
            if (simpleUser.getTlfNum() == tlfNumber) {
                valid = true;
            }
        }
        return valid;
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void generateSpecificTable(ArrayList<SimpleUser> simpleUsersList) {
        tblViewUser.getItems().clear();
        TableColumn<SimpleUser, String> column1 = new TableColumn<>("DNI");
        column1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<SimpleUser, String> column2 = new TableColumn<>("Nombre");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SimpleUser, String> column3 = new TableColumn<>("Apellido");
        column3.setCellValueFactory(new PropertyValueFactory<>("surname"));
        TableColumn<SimpleUser, String> column4 = new TableColumn<>("Correo");
        column4.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<SimpleUser, String> column5 = new TableColumn<>("Usuario");
        column5.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<SimpleUser, String> column6 = new TableColumn<>("Tipo");
        column6.setCellValueFactory(new PropertyValueFactory<>("type"));

        tblViewUser.getColumns().add(column1);
        tblViewUser.getColumns().add(column2);
        tblViewUser.getColumns().add(column3);
        tblViewUser.getColumns().add(column4);
        tblViewUser.getColumns().add(column5);
        tblViewUser.getColumns().add(column6);

        for (int i = 0; i < simpleUsersList.size(); i++) {
            tblViewUser.getItems().add(new SimpleUser(simpleUsersList.get(i).getUserId(), simpleUsersList.get(i).getName(), simpleUsersList.get(i).getSurname(),
                simpleUsersList.get(i).getEmail(), simpleUsersList.get(i).getTlfNum(), simpleUsersList.get(i).getUsername(), simpleUsersList.get(i).getType()));
        }

        column4.setPrefWidth(170);
    }

    @FXML
    private void generateAlert(int error) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cuidado!");

        if (error == 0) {
            alert.setHeaderText("El DNI insertado no existe en la base de datos");
        } else if (error == 1) {
            alert.setHeaderText("El Nombre insertado no existe en la base de datos");
        } else if (error == 2) {
            alert.setHeaderText("El Apellido insertado no existe en la base de datos");
        } else if (error == 3) {
            alert.setHeaderText("El Numero de telefono insertado no existe en la base de datos");
        } else if (error == 4) {
            alert.setHeaderText("El campo de filtrado no puede estar vacio");
        } else {
            alert.setHeaderText("El Numero de telefono solo puede estar compuesto por numeros");
        }
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
}
