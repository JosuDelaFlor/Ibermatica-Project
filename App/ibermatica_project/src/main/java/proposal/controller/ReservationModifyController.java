package proposal.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        boolean validId = checkDbId(Integer.parseInt(txfSearchInput.getText()));
        if (validId) {
            lblError.setText(null);
            inputReservation = db.searchSpecificReservation(Integer.parseInt(txfSearchInput.getText()));
            txfId.setText(inputReservation.getUserId());
            txfSerialNumber.setText(inputReservation.getSerialNumber());
            txfStartDate.setText(inputReservation.getStartDate().toString());
            txfEndDate.setText(inputReservation.getEndDate().toString());
        } else {
            lblError.setText("El numero de reserva insertado no existe");
        }
    }

    @FXML
    public void modify() throws IOException {
        if (txfSearchInput.getText().equals("") || txfId.getText().equals("") || txfSerialNumber.getText().equals("") ||
                txfStartDate.getText().equals("") || txfEndDate.getText().equals("")) {
            lblError.setText("Todos los campos tienen que estar rellenados");
            
        } else {
            LocalDate startDate = LocalDate.parse(txfStartDate.getText()), endDate = LocalDate.parse(txfEndDate.getText());
            Reservation modifyReservation = new Reservation(txfId.getText(), txfSerialNumber.getText(), 
                startDate, endDate, Integer.parseInt(txfSearchInput.getText()));

            boolean valid = checkDbData(modifyReservation);
            if (valid) {
                lblError.setText(null);
                int result = db.updateReservation(modifyReservation);
                if (result == 1) {
                    lblSuccess.setText("La reserva se ha actualizado correctamente");
                } else {
                    lblError.setText("Hemos tenido problemas al intentar actualizar la reserva");
                }
            }
        }
    }

    private boolean checkDbData(Reservation checkReservation) {
        boolean valid = true;
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        LocalDate checkStartDate = checkReservation.getStartDate(), checkEndDate = checkReservation.getEndDate();
        if (checkStartDate.isAfter(checkEndDate)) {
            valid = false;
            lblError.setText("La fecha de inicio tiene que ser que la fecha final");
            lblSuccess.setText(null);
            return valid;
        }
        long dayDiff = ChronoUnit.DAYS.between(checkStartDate, checkEndDate);

        if (dayDiff == 1) {
            dayDiff++;
        }

        ArrayList<LocalDate> blockedDates = new ArrayList<LocalDate>();
        for (int i = 0; i < dayDiff; i++) {
            LocalDate blockedDay = checkStartDate.plusDays(i);
            blockedDates.add(blockedDay);
        }

        for (int i = 0; i < reservationList.size(); i++) {
            if (txfSerialNumber.getText().equals(reservationList.get(i).getSerialNumber())) {
                if (blockedDates.get(i).equals(reservationList.get(i).getStartDate()) || blockedDates.get(i).equals(reservationList.get(i).getStartDate())) {
                    valid = false;
                    lblError.setText("La maquina ya tiene una reserva para esos dias");
                    lblSuccess.setText(null);
                }
            }
        }

        ArrayList<User> userList = new ArrayList<User>();
        userList = db.searchAllUsers();

        for (User user : userList) {
            if (!user.getUserId().equals(checkReservation.getUserId())) {
                valid = false;
                lblError.setText("El DNI insertado no existe");
            } else {
                valid = true;
                lblError.setText(null);
            }
        }

        ArrayList<Machine> machineList = new ArrayList<Machine>();
        machineList = db.searchAllMachines();

        for (Machine machine : machineList) {
            if (!machine.getSerialNumber().equals(checkReservation.getSerialNumber())) {
                valid = false;
                lblError.setText("El numero de serie insertado no existe");
            }
        }

        return valid;
    }

    @FXML
    private boolean checkDbId(int id) throws IOException {
        boolean valid = false;
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();
        
        for (Reservation reservation : reservationList) {
            if (reservation.getReservationId() == id) {
                valid = true;
            }
        }

        return valid;
    }

    // private boolean checkValidDate(String date, String dateFormat) {
    //     DateFormat df = new SimpleDateFormat(dateFormat);
    //     df.setLenient(false);
    //     try {
    //         df.parse(date);
    //     } catch (ParseException e) {
    //         return false;
    //     }
    //     return true;
    // }

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
