package ibermatica_project.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

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

public class ReservationAddMenuController {
    @FXML
    MenuButton btnMenu;

    @FXML
    Label lblError, lblReservationTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblStartDate, lblEndDate;

    @FXML
    Button btnUserManagement, btnMachineManagement, btnReserveManagement, btnCreate, btnReset, btnModify, btnVisualize;

    @FXML
    TextField txfStartDate, txfId, txfEndDate, txfSerialNumber;

    static Reservation inputReservation;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    static boolean langChangeBol = AdmMenuController.getLangChangeBol();

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
    }

    @FXML
    private void langChange(String lang) throws IOException {
        if (langChangeBol) {
            Label[] labelList = {lblReservationTitle, lblUserManagement, lblMachineManagement, lblReservesManagement, lblStartDate, lblEndDate};
            Label[] labelChangeList = Lang.langChangeLabel(lang, labelList, "reservationAdd");

            Button[] buttonList = {btnReset, btnModify, btnCreate, btnVisualize};
            Button[] buttonChangeList = Lang.langChangeButton(lang, buttonList, "reservationAdd");

            TextField[] textFieldList = {txfStartDate, txfId, txfEndDate, txfSerialNumber};
            TextField[] textFieldChangeList = Lang.langChangeTextField(lang, textFieldList, "reservationAdd");

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
    public void add() throws IOException {
        boolean valid = testInputs();
        if (valid) {
            if (checkBlockDays()) {
                lblError.setText(null);
                inputReservation = new Reservation(txfId.getText(), txfSerialNumber.getText(), LocalDate.parse(txfStartDate.getText()),
                        LocalDate.parse(txfEndDate.getText()), 0);
                boolean result = db.addReservation(inputReservation);
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
        boolean valid = false, validUserID = false, validSerialNumber = false, validStartDate = false, validEndDate = false;

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

        if (validUserID == true && validSerialNumber == true && validStartDate == true && validEndDate == true) {
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
    private void restartInputs() throws IOException {
        txfStartDate.setText(null);
        txfId.setText(null);
        txfEndDate.setText(null);
        txfSerialNumber.setText(null);
        lblError.setText(null);
    }
    
    @FXML
    private void loadReservationModifyMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationModifyMenu");
    }

    @FXML
    private void loadReservationMangementMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationManagementMenu");
    }

    @FXML
    private void loadUserManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/reservationManagementMenu");
    }

    @FXML
    private void loadMachineManagementMenuScene() throws IOException {
        SceneController.setRoot("fxml/machineManagementMenu");
    }
}
