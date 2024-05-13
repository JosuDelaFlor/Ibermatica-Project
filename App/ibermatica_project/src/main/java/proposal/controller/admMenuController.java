package proposal.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class admMenuController {
    @FXML
    Button btnExit;

    
    @FXML
    private void exit() {
        Platform.exit();
    }
}
