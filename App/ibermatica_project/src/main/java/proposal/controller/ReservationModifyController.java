package proposal.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import proposal.App;
import proposal.model.base.DataBase;

public class ReservationModifyController {
    @FXML
    Button btnback, btnRegister, btnResign, btnReservation, btnLogOut, btnVisualize;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

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
