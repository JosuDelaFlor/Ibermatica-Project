package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import ibermatica_project.model.BlockDay;
import ibermatica_project.model.Machine;
import ibermatica_project.model.Reservation;
import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class EmployeeMenuController {
    @FXML
    Label lblName, lblSurname, lblEmail, lblTlfNumber, lblUsername, lblTitle, lblError;

    @FXML
    TextField txfStartDate, txfEndDate, txfSerialNumber;

    @FXML
    MenuButton btnMenu;

    @SuppressWarnings("rawtypes")
    @FXML
    TableView tblUserReservation;

    @FXML
    Button btnAddReservation;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
    User loggedUser = IndexController.getLoggedUser();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @FXML
    protected void initialize() throws IOException {
        lblTitle.setText("Bienvenido " + loggedUser.getName());
        lblName.setText(loggedUser.getName());
        lblSurname.setText(loggedUser.getSurname());
        lblUsername.setText(loggedUser.getUsername());
        lblEmail.setText(loggedUser.getEmail());
        lblTlfNumber.setText(Integer.toString(loggedUser.getTlfNum()));

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

        TableColumn<Reservation, String> column1 = new TableColumn<>("Numero de serie");
        column1.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        TableColumn<Reservation, String> column2 = new TableColumn<>("Fecha inicial");
        column2.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Reservation, String> column3 = new TableColumn<>("Fecha final");
        column3.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn actionCol = new TableColumn("Eliminar");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionDelete"));

        tblUserReservation.getColumns().add(column1);
        tblUserReservation.getColumns().add(column2);
        tblUserReservation.getColumns().add(column3);
        tblUserReservation.getColumns().add(actionCol);

        reservationList = db.searchReservationWithUserId(loggedUser.getUserId());

        generateTableView(reservationList, actionCol);
    }

    @SuppressWarnings("rawtypes")
    @FXML
    public void add() throws IOException {
        boolean valid = testInputs();
        if (valid) {
            if (checkBlockDays()) {
                Reservation reservation = new Reservation(loggedUser.getUserId(), txfSerialNumber.getText().replaceAll("\\s", ""),
                    LocalDate.parse(txfStartDate.getText().replaceAll("\\s", "")),
                    LocalDate.parse(txfEndDate.getText().replaceAll("\\s", "")), 0);
                    restartInputs();
                boolean result = db.addReservation(reservation);
                if (result == true) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Operación completada");
                    alert.setHeaderText("La reserva se ha actualizado exitosamente");
                    alert.show();
                    TableColumn actionCol = new TableColumn("Action");
                    tblUserReservation.getItems().clear();
                    generateTableView(reservationList, actionCol);
                } else {
                    generateAlert(6);
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @FXML
    private void delete(int reservationId) throws IOException {
        boolean result = db.deleteReservation(reservationId);
        if (result == false) {
            tblUserReservation.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Operación completada");
            alert.setHeaderText("La reserva se ha eliminado por completo");
            reservationList = db.searchReservationWithUserId(loggedUser.getUserId());
            TableColumn actionCol = new TableColumn("Action");
            generateTableView(reservationList, actionCol);
        } else {
            generateAlert(0);
        }
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @FXML
    private void generateTableView(ArrayList<Reservation> reservationList, TableColumn actionCol) throws IOException {
        tblUserReservation.getItems().clear();

        for (int i = 0; i < reservationList.size(); i++) {
            tblUserReservation.getItems().add(new Reservation(reservationList.get(i).getUserId(), reservationList.get(i).getSerialNumber(), reservationList.get(i).getStartDate(),
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

    @SuppressWarnings("unused")
    @FXML
    private boolean testInputs() throws IOException {
        boolean valid = false, validSerialNumber = false, validDates = false;
        
        if (txfStartDate.getText().replaceAll("\\s", "").equals("")) {
            generateAlert(1);
        } else if (txfEndDate.getText().replaceAll("\\s", "").equals("")) {
            generateAlert(2);
        } else if (txfSerialNumber.getText().replaceAll("\\s", "").equals("")) {
            generateAlert(4);
        } else {
            validSerialNumber = true;
        }

        try {
            LocalDate startDate = LocalDate.parse(txfStartDate.getText());
            LocalDate endDate = LocalDate.parse(txfEndDate.getText());
            validDates = true;
        } catch (Exception e) {
            generateAlert(5);
        }

        if (validSerialNumber && validDates) {
            valid = true;
        }
        return valid;
    }

    private boolean checkBlockDays () throws IOException {
        ArrayList<Reservation> allReservations = new ArrayList<Reservation>();
        allReservations = db.searchAllReservations();

        ArrayList<BlockDay> blockDays = new ArrayList<BlockDay>();

        for (Reservation reservation : allReservations) {
            if (reservation.getSerialNumber().equals(txfSerialNumber.getText())) {
                BlockDay blockDay = new BlockDay(reservation.getStartDate(), reservation.getEndDate(), 
                        BlockDay.calculateDayDiff(LocalDate.parse(txfStartDate.getText()), LocalDate.parse(txfEndDate.getText())));
                blockDays.add(blockDay);
            }
        }

        ArrayList<LocalDate> reserverDates = new ArrayList<LocalDate>();

        for (int i = 0; i < blockDays.size(); i++) {
            LocalDate newDate = blockDays.get(i).getStartDay();
            for (int j = 0; j < blockDays.get(i).getDayDiff(); j++) {
                newDate.plusDays(j);
                reserverDates.add(newDate);
            }
        }

        boolean valid = true;

        for (LocalDate localDate : reserverDates) {
            if (LocalDate.parse(txfStartDate.getText()).equals(localDate) || LocalDate.parse(txfEndDate.getText()).equals(localDate)) {
                lblError.setText("La maquina ya esta reservada para las fechas seleccionadas");
                valid = false;
            }
        }

        for (Reservation reservation : allReservations) {
            if (LocalDate.parse(txfStartDate.getText()).equals(reservation.getStartDate()) || LocalDate.parse(txfEndDate.getText()).equals(reservation.getEndDate())) {
                lblError.setText("La maquina ya esta reservada para las fechas seleccionadas");
                valid = false;
            }
        }

        return valid;
    }

    @FXML
    private void generateAlert(int error) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cuidado!");

        if (error == 0) {
            alert.setHeaderText("Hemos tenido problemas al eliminar la reserva, por favor contacte con un Administrador");
        } else if (error == 1) {
            alert.setHeaderText("El campo de la fecha de inicio tiene que estar completado");
        } else if (error == 2) {
            alert.setHeaderText("El campo de la fecha final tiene que estar completado");
        } else if (error == 3) {
            alert.setHeaderText("El número de serie inertado no existe");
        } else if (error == 4) {
            alert.setHeaderText("El campo del número de serie tiene que estar completado");
        } else if (error == 5) {
            alert.setHeaderText("El formato de la fecha tiene que ser yyyy-mm-dd");
        } else {
            alert.setHeaderText("Hemos tenido problemas al actualizar la reserva, por favor contacte con un Administrador");
        }
        alert.show();
    }

    @FXML
    private void loadRestartPasswordScene() throws IOException {
        SceneController.loadRestartPasswordScene();
    }

    @FXML
    private void restartInputs() throws IOException {
        txfStartDate.setText("");
        txfEndDate.setText("");
        txfSerialNumber.setText("");
        lblError.setText(null);
    }
}
