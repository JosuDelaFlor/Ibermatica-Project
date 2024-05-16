package proposal.controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import proposal.App;

public class AdmMenuController {
    @FXML
    Button btnExit, btnRegister, btnResign, btnReservation, btnLogOut;

    
    @FXML
    private void logOut() throws IOException {
        App.setRoot("fxml/index");
    }

    @FXML
    private void loadRegisterScene() throws IOException {
        App.setRoot("fxml/registerMenu");
    }

    @FXML
    private void loadResignScene() throws IOException {
        App.setRoot("fxml/resignMenu");
    }

    @FXML
    private void loadReservationScene() throws IOException {
        App.setRoot("fxml/reservationMenu");
    }

    @FXML
    private void loadUserDataScene() throws IOException {
        App.setRoot("fxml/userData");
    }

    @FXML
    private void loadUserModifyScene() throws IOException {
        App.setRoot("fxml/userModify");
    }

    @FXML
    private void exit() {
        Platform.exit();
    }
}
