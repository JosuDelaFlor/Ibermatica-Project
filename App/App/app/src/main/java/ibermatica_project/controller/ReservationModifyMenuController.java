package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import ibermatica_project.lang.Lang;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReservationModifyMenuController {
    @FXML
    MenuButton btnMenu;

    @FXML
    Button btnVisualize, btnModify, btnSearch, btnUserManagement, btnMachineManagement, btnReserveManagement, btnCreate;

    @FXML
    TextField txfId, txfSerialNumber, txfStartDate, txfEndDate, txfSearchInput;

    @FXML
    Label lblError, lblReservationTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblId, lblSerialNumber,
        lblReservationId, lblStartDate, lblEndDate;

    static Reservation inputReservation;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    static boolean langChangeBol = AdmMenuController.getLangChangeBol();

    @FXML
    protected void initialize() throws IOException {
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
    }

    @FXML
    private void langChange(String lang) throws IOException {
        if (langChangeBol) {
            Label[] labelList = {lblReservationTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblStartDate, lblEndDate,
                lblSerialNumber, lblId};
            Label[] labelChangeList = Lang.langChangeLabel(lang, labelList, "reservationModify");

            Button[] buttonList = {btnSearch, btnModify, btnVisualize, btnCreate};
            Button[] buttonChangeList = Lang.langChangeButton(lang, buttonList, "reservationModify");

            TextField[] textFieldList = {txfId, txfSerialNumber, txfStartDate, txfEndDate};
            TextField[] textFieldChangeList = Lang.langChangeTextField(lang, textFieldList, "reservationModify");

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

    @FXML
    private void search() throws IOException {
        if (!txfSearchInput.getText().replaceAll("\\s", "").equals("")) {
            if (checkDbReservationId(Integer.parseInt(txfSearchInput.getText()))) {
                inputReservation = db.searchSpecificReservationWithId(Integer.parseInt(txfSearchInput.getText()));
                txfId.setText(inputReservation.getUserId());
                txfSerialNumber.setText(inputReservation.getSerialNumber());
                txfStartDate.setText(inputReservation.getStartDate().toString());
                txfEndDate.setText(inputReservation.getEndDate().toString());
            } else {
                generateAlert("El numero de reserva no existe");
            }
        } else {
            generateAlert("El numero de reserva no puede estar vacio");
        }
    }

    @FXML
    public void modify() throws IOException {
        boolean valid = testInputs();
        if (valid) {
            if (checkBlockDays()) {
                inputReservation = new Reservation(txfId.getText(), txfSerialNumber.getText(), LocalDate.parse(txfStartDate.getText()),
                        LocalDate.parse(txfEndDate.getText()), Integer.parseInt(txfSearchInput.getText()));
                boolean result = db.updateReservation(inputReservation);
                if (result == true) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Operación completada");
                    alert.setHeaderText("La reserva se ha actualizado exitosamente");
                    alert.show();
                } else {
                    generateAlert("Hemos tenido problemas al actualizar la reserva");
                }
            }
        }
    }

    @FXML
    private boolean testInputs() throws IOException {
        boolean valid = false, validReservationId = false, validUserID = false, validSerialNumber = false, validStartDate = false, validEndDate = false;
        
        if (txfSearchInput.getText().replaceAll("\\s", "").equals("")) {
            generateAlert("El campo Numero de reserva tiene que estar completado");
        } else if (!checkDbReservationId(Integer.parseInt(txfSearchInput.getText()))) {
            generateAlert("El numero de reserva insertado no existe");
        } else {
            validReservationId = true;
        }

        if (txfId.getText().replaceAll("\\s", "").equals("")) {
            generateAlert("El campo DNI tiene que estar completado");
        } else if (!checkDbUserId(txfId.getText())) {
            generateAlert("El DNI insertado no existe");
        } else {
            validUserID = true;
        }

        if (txfSerialNumber.getText().replaceAll("\\s", "").equals("")) {
            generateAlert("El campo Numero de serie tiene que estar completado");
        } else if (!checkDbSerialNumber(txfSerialNumber.getText())) {
            generateAlert("El Numero de serie insertado no existe");
        } else if (!checkMachineStatus(txfSerialNumber.getText())) {
            generateAlert("La maquina que quiere reservar no se encuentra operativa");
        } else {
            validSerialNumber = true;
        }

        if (txfStartDate.getText().replaceAll("\\s", "").equals("")) {
            generateAlert("El campo Fecha de inicio tiene que estar completado");
        } else if (!checkValidDate(txfStartDate.getText(), "yyyy-mm-dd")) {
            generateAlert("El formato de la fecha de inicio no es correcto 'yyyy-mm-dd'");
        } else {
            validStartDate = true;
        }

        if (txfEndDate.getText().replaceAll("\\s", "").equals("")) {
            generateAlert("El campo Fecha final tiene que estar completado");
        } else if (!checkValidDate(txfEndDate.getText(), "yyyy-mm-dd")) {
            generateAlert("El formato de la fecha final no es correcto 'yyyy-mm-dd'");
        } else {
            validEndDate = true;
        }

        if (validStartDate == true && validEndDate == true) {
            LocalDate startDate = LocalDate.parse(txfStartDate.getText()), endDate = LocalDate.parse(txfEndDate.getText());
            if (startDate.isAfter(endDate)) {
                generateAlert("La fecha de inicio tiene que ser una fecha anterior a la fecha final");
                validStartDate = false;
            }
        }

        if (validReservationId == true && validUserID == true && validSerialNumber == true && validStartDate == true && validEndDate == true) {
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
            if (LocalDate.parse(txfStartDate.getText()).equals(reservation.getStartDate()) && LocalDate.parse(txfEndDate.getText()).equals(reservation.getEndDate())) {
                lblError.setText(null);
                valid = true;
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

    @FXML
    private boolean checkMachineStatus(String serialNumber) throws IOException {
        boolean valid = false;
        Machine machine = db.searchSpecificMachine(serialNumber);

        if (machine.getStatus().equals("operational")) {
            valid = true;
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
    private void generateAlert(String error) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cuidado!");
        alert.setHeaderText(error);
        alert.show();
    }

    @FXML
    private void loadReservationMangementMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationManagementMenu");
    }

    @FXML
    private void loadUserManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/userManagementMenu");
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
