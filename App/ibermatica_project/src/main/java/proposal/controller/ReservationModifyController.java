package proposal.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import proposal.App;
import proposal.model.BlockDay;
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
        resetLbls();
        if (!txfSearchInput.getText().replaceAll("\\s", "").equals("")) {
            if (checkDbReservationId(Integer.parseInt(txfSearchInput.getText()))) {
                inputReservation = db.searchSpecificReservation(Integer.parseInt(txfSearchInput.getText()));
                txfId.setText(inputReservation.getUserId());
                txfSerialNumber.setText(inputReservation.getSerialNumber());
                txfStartDate.setText(inputReservation.getStartDate().toString());
                txfEndDate.setText(inputReservation.getEndDate().toString());
            } else {
                lblError.setText("El numero de reserva no existe");
            }
        } else {
            lblError.setText("El numero de reserva no puede estar vacio");
        }
    }

    @FXML
    public void modify() throws IOException {
        boolean valid = testInputs();
        if (valid) {
            resetLbls();
            if (checkBlockDays()) {
                inputReservation = new Reservation(txfId.getText(), txfSerialNumber.getText(), LocalDate.parse(txfStartDate.getText()),
                        LocalDate.parse(txfEndDate.getText()), Integer.parseInt(txfSearchInput.getText()));
                int result = db.updateReservation(inputReservation);
                if (result == 1) {
                    lblSuccess.setText("La reserva se ha actualizado exitosamente");
                } else {
                    lblError.setText("Hemos tenido problemas al actualizar la reserva");
                }
            }
        }
    }

    @FXML
    private boolean testInputs() throws IOException {
        boolean valid = false, validReservationId = false, validUserID = false, validSerialNumber = false, validStartDate = false, validEndDate = false;
        resetLbls();
        
        if (txfSearchInput.getText().replaceAll("\\s", "").equals("")) {
            lblError.setText("El campo Numero de reserva tiene que estar completado");
        } else if (!checkDbReservationId(Integer.parseInt(txfSearchInput.getText()))) {
            lblError.setText("El numero de reserva insertado no existe");
        } else {
            validReservationId = true;
        }

        if (txfId.getText().replaceAll("\\s", "").equals("")) {
            lblError.setText("El campo DNI tiene que estar completado");
        } else if (!checkDbUserId(txfId.getText())) {
            lblError.setText("El DNI insertado no existe");
        } else {
            validUserID = true;
        }

        if (txfSerialNumber.getText().replaceAll("\\s", "").equals("")) {
            lblError.setText("El campo Numero de serie tiene que estar completado");
        } else if (!checkDbSerialNumber(txfSerialNumber.getText())) {
            lblError.setText("El Numero de serie insertado no existe");
        } else {
            validSerialNumber = true;
        }

        if (txfStartDate.getText().replaceAll("\\s", "").equals("")) {
            lblError.setText("El campo Fecha de inicio tiene que estar completado");
        } else if (!checkValidDate(txfStartDate.getText(), "yyyy-mm-dd")) {
            lblError.setText("El formato de la fecha de inicio no es correcto 'yyyy-mm-dd'");
        } else {
            validStartDate = true;
        }

        if (txfEndDate.getText().replaceAll("\\s", "").equals("")) {
            lblError.setText("El campo Fecha final tiene que estar completado");
        } else if (!checkValidDate(txfEndDate.getText(), "yyyy-mm-dd")) {
            lblError.setText("El formato de la fecha final no es correcto 'yyyy-mm-dd'");
        } else {
            validEndDate = true;
        }

        if (validStartDate == true && validEndDate == true) {
            LocalDate startDate = LocalDate.parse(txfStartDate.getText()), endDate = LocalDate.parse(txfEndDate.getText());
            if (startDate.isAfter(endDate)) {
                lblError.setText("La fecha de inicio tiene que ser una fecha anterior a la fecha final");
                validStartDate = false;
            }
        }

        if (validReservationId == true && validUserID == true && validSerialNumber == true && validStartDate == true && validEndDate == true) {
            valid = true;
        }
        return valid;
    }

    private boolean checkBlockDays () {
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
    private void resetLbls() throws IOException {
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
