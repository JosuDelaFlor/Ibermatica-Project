package proposal.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import proposal.App;

public class registerController {
    @FXML
    Button btnBack, btnLogOut;

    @FXML
    private void logOut() throws IOException {
        App.setRoot("fxml/index");
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("fxml/admMenu");;
    }
}
