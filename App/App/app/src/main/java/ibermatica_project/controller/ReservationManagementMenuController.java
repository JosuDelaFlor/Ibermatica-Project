package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import ibermatica_project.lang.Lang;
import ibermatica_project.model.Reservation;
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

public class ReservationManagementMenuController {
    @SuppressWarnings("rawtypes")
    @FXML
    TableView tblViewReservation;

    @FXML
    MenuButton btnMenu;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboSearchType;

    @FXML
    Label lblReservationTitle, lblUserManagement, lblMachineManagement, lblReservesManagement;

    @FXML
    TextField txfSearch;

    @FXML
    Button btnSearch, btnModify, btnCreate, btnDelete, btnUserManagement, btnMachineManagement, btnReserveManagement;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    static ArrayList<Reservation> reservationList = new ArrayList<Reservation>();

    static boolean langChangeBol = AdmMenuController.getLangChangeBol();
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @FXML
    protected void initialize() throws IOException {
        if (langChangeBol) {
            Label[] labelList = {lblReservationTitle, lblUserManagement, lblMachineManagement, lblReservesManagement};
            Label[] labelChangeList = Lang.langChangeLabel("English", labelList, "reservationManagement");

            Button[] buttonList = {btnSearch, btnModify, btnCreate};
            Button[] buttonChangeList = Lang.langChangeButton("English", buttonList, "reservationManagement");

            TextField[] textFieldList = {txfSearch};
            TextField[] textFieldChangeList = Lang.langChangeTextField("English", textFieldList, "reservationManagement");

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
        searchTypeList.addAll(Arrays.asList("DNI", "Número de serie", "Fecha inicial", "Fecha final", "Id de reservación"));
    
        for (int i = 0; i < searchTypeList.size(); i++) {
            comboSearchType.getItems().add(searchTypeList.get(i));
        }
        comboSearchType.setValue(searchTypeList.get(0));

        TableColumn<Reservation, String> column1 = new TableColumn<>("DNI");
        column1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<Reservation, String> column2 = new TableColumn<>("Numero de serie");
        column2.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        TableColumn<Reservation, String> column3 = new TableColumn<>("Fecha inicial");
        column3.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Reservation, String> column4 = new TableColumn<>("Fecha final");
        column4.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Reservation, String> column5 = new TableColumn<>("Id");
        column5.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        TableColumn actionCol = new TableColumn("Eliminar");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionDelete"));


        tblViewReservation.getColumns().add(column1);
        tblViewReservation.getColumns().add(column2);
        tblViewReservation.getColumns().add(column3);
        tblViewReservation.getColumns().add(column4);
        tblViewReservation.getColumns().add(column5);
        tblViewReservation.getColumns().add(actionCol);

        column1.setPrefWidth(120.5);
        column2.setPrefWidth(120.5);
        column3.setPrefWidth(120);
        column4.setPrefWidth(120);

        reservationList = db.searchAllReservations();

        generateTableView(reservationList, actionCol);
    }

    @SuppressWarnings("rawtypes")
    @FXML
    private void delete(int reservationId) throws IOException {
        boolean result = db.deleteReservation(reservationId);
        if (result == false) {
            tblViewReservation.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Operación completada");
            alert.setHeaderText("La reserva se ha eliminado por completo");
            reservationList = db.searchAllReservations();
            TableColumn actionCol = new TableColumn("Action");
            generateTableView(reservationList, actionCol);
        } else {
            generateAlert(6);
        }
    }

    @SuppressWarnings("rawtypes")
    @FXML
    private void search() throws IOException {
        TableColumn actionCol = new TableColumn("Action");

        if ((String) comboSearchType.getValue() == "DNI") {
            if (checkInputs(0)) {
                reservationList = db.searchSpecificReservationWithUserId(txfSearch.getText());
                generateTableView(reservationList, actionCol);
            }
        } else if ((String) comboSearchType.getValue() == "Numero de serie") {
            if (checkInputs(1)) {
                reservationList = db.searchSpecificReservationWithSerialNumer(txfSearch.getText());
                generateTableView(reservationList, actionCol);
            }
        } else if ((String) comboSearchType.getValue() == "Fecha inicial") {
            if (checkInputs(2)) {
                reservationList = db.searchSpecificReservationWithStartDate(txfSearch.getText());
                generateTableView(reservationList, actionCol);
            }
        } else if ((String) comboSearchType.getValue() == "Fecha final") {
            if (checkInputs(3)) {
                reservationList = db.searchSpecificReservationWithEndDate(txfSearch.getText());
                generateTableView(reservationList, actionCol);
            }
        } else {
            if (checkInputs(4)) {
                reservationList = db.searchAllReservationWithId(Integer.parseInt(txfSearch.getText()));
                generateTableView(reservationList, actionCol);
            }
        }
    }

    @FXML
    private boolean checkInputs(int searchType) throws IOException {
        boolean valid = true;

        if (txfSearch.getText().equals("")) {
            generateAlert(7);
            return false;
        } else {
            if (searchType == 0) { // searchType 0 = user_id
                if (!checkDbUserId(txfSearch.getText().replaceAll("\\s", ""))) {
                    generateAlert(0);
                    valid = false;
                }
            } else if (searchType == 1) { // searchType 1 = name
                if (!checkDbSerialNumber(txfSearch.getText().replaceAll("\\s", ""))) {
                    generateAlert(1);
                    valid = false;
                }
            } else if (searchType == 2) { // searchType 2 = start_date
                LocalDate startDate = LocalDate.parse(txfSearch.getText());
                if (!checkValidDate(txfSearch.getText().replaceAll("\\s", ""), "yyyy-mm-dd")) {
                    generateAlert(2);
                } else if (!checkDbStartDate(startDate)) {
                    generateAlert(3);
                    valid = false;
                }
            } else if (searchType == 3) { // seachType 3 == end_date
                LocalDate endDate = LocalDate.parse(txfSearch.getText());
                if (!checkValidDate(txfSearch.getText().replaceAll("\\s", ""), "yyyy-mm-dd")) {
                    generateAlert(2);
                } else if (!checkDbEndDate(endDate)) {
                    generateAlert(4);
                    valid = false;
                } else { // search type +3 == reservation_num
                    if (!checkDbReservationId(Integer.parseInt(txfSearch.getText()))) {
                        generateAlert(5);
                        valid = false;
                    }
                }
            } 
        } 
        return valid;
    }

    @FXML
    private boolean checkDbUserId(String userId) throws IOException {
        boolean valid = false;
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        for (Reservation reservation : reservationList) {
            if (reservation.getUserId().equals(userId)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbSerialNumber(String serialNumer) throws IOException {
        boolean valid = false;
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        for (Reservation reservation : reservationList) {
            if (reservation.getSerialNumber().equals(serialNumer)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbStartDate(LocalDate startDate) throws IOException {
        boolean valid = false;
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        for (Reservation reservation : reservationList) {
            if (reservation.getStartDate().equals(startDate)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbEndDate(LocalDate endDate) throws IOException {
        boolean valid = false;
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        for (Reservation reservation : reservationList) {
            if (reservation.getEndDate().equals(endDate)) {
                valid = true;
            }
        }
        return valid;
    }

    @FXML
    private boolean checkDbReservationId(int reservationId) throws IOException {
        boolean valid = false;
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        for (Reservation reservation : reservationList) {
            if (reservation.getReservationId() == reservationId) {
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
    private void generateTableView(ArrayList<Reservation> reservationList, TableColumn actionCol) throws IOException {
        tblViewReservation.getItems().clear();

        for (int i = 0; i < reservationList.size(); i++) {
            tblViewReservation.getItems().add(new Reservation(reservationList.get(i).getUserId(), reservationList.get(i).getSerialNumber(), reservationList.get(i).getStartDate(),
                    reservationList.get(i).getEndDate(), reservationList.get(i).getReservationId()));
        }

        Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>> cellFactory
                = //
                new Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>>() {
            @Override
            public TableCell call(final TableColumn<Reservation, String> param) {
                final TableCell<Reservation, String> cell = new TableCell<Reservation, String>() {

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
                                Reservation reservation = getTableView().getItems().get(getIndex());
                                try {
                                    delete(reservation.getReservationId());
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

    @FXML
    private void generateAlert(int error) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cuidado!");

        if (error == 0) {
            alert.setHeaderText("El DNI insertado no existe en la base de datos");
        } else if (error == 1) {
            alert.setHeaderText("El número de serie insertado no existe en la base de datos");
        } else if (error == 2) {
            alert.setHeaderText("El formato de la fecha no es el correcto (yyyy-mm-dd)");
        } else if (error == 3) {
            alert.setHeaderText("No existe ninguna reserva con la fecha de inicio insertada");
        } else if (error == 4) {
            alert.setHeaderText("No existe ninguna reserva con la fecha final insertada");
        } else if (error == 5) {
            alert.setHeaderText("No existe ninguna reserva con el número de reserva insertado");
        } else if (error == 6) {
            alert.setHeaderText("Hemos tenido problemas al eliminar al usuario, por favor contacte con un Administrador");
        } else if (error == 7) {
            alert.setHeaderText("El campo de filtrado no puede estar vacio");
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
    private void loadReservationModifyMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationModifyMenu");
    }

    @FXML
    private void loadReservationAddMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationAddMenu");
    }

    @FXML
    private void loadMachineManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineManagementMenu");
    }
}
