package design.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class IndexController {
    @FXML
    Button btnLogin;

    @FXML
    private void checkUser() throws IOException {
        ScenesController.loadAdmMenuScene();
    }
}
