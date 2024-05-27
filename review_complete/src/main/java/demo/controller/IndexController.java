package demo.controller;

import java.io.IOException;

import demo.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class IndexController {
    @FXML
    Button btnAdd, btnMeeting, btnMaterial;
    
    @FXML
    private void loadStudentScene() throws IOException {
        App.setRoot("fxml/student");
    }

    @FXML
    private void loadMeetingScene() throws IOException {
        App.setRoot("fxml/meeting");
    }

    @FXML
    private void loadMaterialScene() throws IOException {
        App.setRoot("fxml/material");
    }
}
