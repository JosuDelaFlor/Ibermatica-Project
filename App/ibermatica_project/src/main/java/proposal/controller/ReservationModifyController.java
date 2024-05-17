package proposal.controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import proposal.App;
import proposal.model.Machine;
import proposal.model.Reservation;
import proposal.model.base.DataBase;
import proposal.model.base.User;

public class ReservationModifyController {
    @FXML
    Button btnback, btnRegister, btnResign, btnReservation, btnLogOut, btnVisualize, btnModify, btnRestart, btnSearch;

    @FXML
    Label lblSuccess, lblError;

    @FXML
    TextField txfId, txfSerialNumber, txfStartDate, txfEndDate, txfSearchInput;

    static Reservation inputReservation;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    @FXML
    private void searchReservation() throws IOException {
        boolean validReservationId = checkDbReservationId(Integer.parseInt(txfSearchInput.getText()));
        if (validReservationId) {
            inputReservation = db.searchSpecificReservation(Integer.parseInt(txfSearchInput.getText()));
            txfId.setText(inputReservation.getUserId());
            txfSerialNumber.setText(inputReservation.getSerialNumber());
            txfStartDate.setText(inputReservation.getStartDate().toString());
            txfEndDate.setText(inputReservation.getEndDate().toString());
            lblError.setText(null);
        } else {
            lblError.setText("El numero de reserva insertado no existe");
        }
    }

    @FXML
    public void modify() throws IOException {
        boolean valid = testInputs();
        if (valid) {
            lblSuccess.setText("Bien");
        }
    }

    @FXML
    private boolean testInputs() throws IOException {
        boolean valid = false;
        if (!txfSearchInput.getText().equals("")) {
            valid = checkDbReservationId(Integer.parseInt(txfSearchInput.getText()));
            if (valid) {
                if (!checkDbUserId(txfId.getText())) {
                    valid = false;
                    lblError.setText("El DNI no existe");
                }
                if (!checkDbSerialNumber(txfSerialNumber.getText())) {
                    valid = false;
                    lblError.setText("El numero de serie no existe");
                }
            }
        } else {
            lblError.setText("El numero de reserva esta vacio");
        }
        return valid;
    }

    @FXML
    private boolean checkDbUserId(String userId) throws IOException {
        boolean valid = false;
        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();

        for (User user : userList) {
            if (user.getUserId().equals(userId)) {
                valid = true;
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

    @FXML
    private void restartData() throws IOException {
        txfId.setText(null);
        txfSearchInput.setText(null);
        txfSerialNumber.setText(null);
        txfStartDate.setText(null);
        txfEndDate.setText(null);
        lblError.setText(null);
        lblSuccess.setText(null);
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
    private void loadResvervationMenuScene() throws IOException {
        App.setRoot("fxml/reservationMenu");
    }
}
