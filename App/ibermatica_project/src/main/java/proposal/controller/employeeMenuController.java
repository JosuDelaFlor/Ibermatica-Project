package proposal.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import proposal.App;

public class EmployeeMenuController {
    @FXML
    Button btnExit;

    @FXML
    private void exit() throws IOException {
        App.setRoot("fxml/index");
    }
}
