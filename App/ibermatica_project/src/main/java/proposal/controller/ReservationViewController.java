package proposal.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import proposal.App;
import proposal.model.Reservation;
import proposal.model.base.DataBase;

public class ReservationViewController {
    @FXML
    Button btnback, btnLogOut, btnModify, btnSearch;

    @SuppressWarnings("rawtypes")
    @FXML
    TableView tbtReservation;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboSearchType;

    @FXML
    TextField txfSearch;

    @FXML
    Label lblError;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    @SuppressWarnings("unchecked")
    @FXML
    protected void initialize() {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        TableColumn<Reservation, String> column1 = new TableColumn<>("DNI");
        column1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<Reservation, String> column2 = new TableColumn<>("Numero de serie");
        column2.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        TableColumn<Reservation, String> column3 = new TableColumn<>("Fecha inicial");
        column3.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Reservation, String> column4 = new TableColumn<>("Fecha final");
        column4.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Reservation, String> column5 = new TableColumn<>("Id de reservacion");
        column5.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        
        tbtReservation.getColumns().add(column1);
        tbtReservation.getColumns().add(column2);
        tbtReservation.getColumns().add(column3);
        tbtReservation.getColumns().add(column4);
        tbtReservation.getColumns().add(column5);

        for (int i = 0; i < reservationList.size(); i++) {
            tbtReservation.getItems().add(new Reservation(reservationList.get(i).getUserId(), reservationList.get(i).getSerialNumber(), reservationList.get(i).getStartDate(),
                    reservationList.get(i).getEndDate(), reservationList.get(i).getReservationId()));
        }

        column1.setPrefWidth(120.5);
        column2.setPrefWidth(120.5);
        column3.setPrefWidth(120);
        column4.setPrefWidth(120);

        List<String> searchTypeList = new ArrayList<String>();
        searchTypeList.addAll(Arrays.asList("DNI", "Numero de serie", "Fecha inicial", "Fecha final", "Id de reservacion"));

        for (int i = 0; i < searchTypeList.size(); i++) {
            comboSearchType.getItems().add(searchTypeList.get(i));
        }
        comboSearchType.setValue("DNI");
    }

    @FXML
    private void search() throws IOException {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        Reservation reservation;

        if ((String) comboSearchType.getValue() == "DNI") {
            if (checkInputs(0)) {
                reservationList = db.searchSpecificReservationWithUserId(txfSearch.getText());
                createSpecificTableView(reservationList);
            }
        } else if ((String) comboSearchType.getValue() == "Numero de serie") {
            if (checkInputs(1)) {
                reservationList = db.searchSpecificReservationWithSerialNumer(txfSearch.getText());
                createSpecificTableView(reservationList);
            }
        } else if ((String) comboSearchType.getValue() == "Fecha inicial") {
            if (checkInputs(2)) {
                reservationList = db.searchSpecificReservationWithStartDate(txfSearch.getText());
                createSpecificTableView(reservationList);
            }
        } else if ((String) comboSearchType.getValue() == "Fecha final") {
            if (checkInputs(3)) {
                reservationList = db.searchSpecificReservationWithEndDate(txfSearch.getText());
                createSpecificTableView(reservationList);
            }
        } else {
            if (checkInputs(4)) {
                reservation = db.searchSpecificReservationWithId(Integer.parseInt(txfSearch.getText()));
                createReservationTableView(reservation);
            }
        }
    }

    @FXML
    private boolean checkInputs(int searchType) throws IOException {
        boolean valid = true;

        if (txfSearch.getText().equals("")) {
            lblError.setText("Tiene que completar el filtro");
            return false;
        } else {
            if (searchType == 0) { // searchType 0 = user_id
                if (!checkDbUserId(txfSearch.getText().replaceAll("\\s", ""))) {
                    lblError.setText("No existe ninguna reserva con el DNI insertado");
                    valid = false;
                }
            } else if (searchType == 1) { // searchType 1 = serial_num
                if (!checkDbSerialNumber(txfSearch.getText().replaceAll("\\s", ""))) {
                    lblError.setText("No existe ninguna reserva con el Numero de serie insertado");
                    valid = false;
                }
            } else if (searchType == 2) { // searchType 2 = start_date
                LocalDate startDate = LocalDate.parse(txfSearch.getText());
                if (!checkValidDate(txfSearch.getText().replaceAll("\\s", ""), "yyyy-mm-dd")) {
                    lblError.setText("El formato de la fecha final no es correcto 'yyyy-mm-dd'");
                } else if (!checkDbStartDate(startDate)) {
                    lblError.setText("No exixte ninguna reserva con la Fecha de inicio insertada");
                    valid = false;
                }
            } else if (searchType == 3) { // seachType 3 == end_date
                LocalDate endDate = LocalDate.parse(txfSearch.getText());
                if (!checkValidDate(txfSearch.getText().replaceAll("\\s", ""), "yyyy-mm-dd")) {
                    lblError.setText("El formato de la fecha final no es correcto 'yyyy-mm-dd'");
                } else if (!checkDbEndDate(endDate)) {
                    lblError.setText("No exixte ninguna reserva con la Fecha final insertada");
                    valid = false;
                }
            } else { // search type +3 == reservation_num
                if (!checkDbReservationId(Integer.parseInt(txfSearch.getText()))) {
                    lblError.setText("No exixte ninguna reserva con el Numero de reserva insertado");
                    valid = false;
                }
            }
            return valid;
        }
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

    @SuppressWarnings("unchecked")
    private void createSpecificTableView(ArrayList<Reservation> reservationList) {
        tbtReservation.getItems().clear();
        TableColumn<Reservation, String> column1 = new TableColumn<>("DNI");
        column1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<Reservation, String> column2 = new TableColumn<>("Numero de serie");
        column2.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        TableColumn<Reservation, String> column3 = new TableColumn<>("Fecha inicial");
        column3.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Reservation, String> column4 = new TableColumn<>("Fecha final");
        column4.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Reservation, String> column5 = new TableColumn<>("Id de reservacion");
        column5.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        
        tbtReservation.getColumns().add(column1);
        tbtReservation.getColumns().add(column2);
        tbtReservation.getColumns().add(column3);
        tbtReservation.getColumns().add(column4);
        tbtReservation.getColumns().add(column5);

        for (int i = 0; i < reservationList.size(); i++) {
            tbtReservation.getItems().add(new Reservation(reservationList.get(i).getUserId(), reservationList.get(i).getSerialNumber(), reservationList.get(i).getStartDate(),
                    reservationList.get(i).getEndDate(), reservationList.get(i).getReservationId()));
        }

        column1.setPrefWidth(120.5);
        column2.setPrefWidth(120.5);
        column3.setPrefWidth(120);
        column4.setPrefWidth(120);
    }

    @SuppressWarnings("unchecked")
    private void createReservationTableView(Reservation reservation) {
        TableColumn<Reservation, String> column1 = new TableColumn<>("DNI");
        column1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<Reservation, String> column2 = new TableColumn<>("Numero de serie");
        column2.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        TableColumn<Reservation, String> column3 = new TableColumn<>("Fecha inicial");
        column3.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Reservation, String> column4 = new TableColumn<>("Fecha final");
        column4.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Reservation, String> column5 = new TableColumn<>("Id de reservacion");
        column5.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        
        tbtReservation.getColumns().add(column1);
        tbtReservation.getColumns().add(column2);
        tbtReservation.getColumns().add(column3);
        tbtReservation.getColumns().add(column4);
        tbtReservation.getColumns().add(column5);
        
        tbtReservation.getItems().add(reservation);

        column1.setPrefWidth(120.5);
        column2.setPrefWidth(120.5);
        column3.setPrefWidth(120);
        column4.setPrefWidth(120);
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
        App.setRoot("fxml/admMenu");
    }

    @FXML
    private void loadModifyReservationScene() throws IOException {
        App.setRoot("fxml/modifyReservation");
    }
}
