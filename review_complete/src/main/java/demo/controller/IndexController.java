package demo.controller;

import java.io.IOException;

import demo.App;
import demo.lang.Lang;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class IndexController {
    @FXML
    Button btnAdd, btnMeeting, btnMaterial, btnPortuguese, btnSpanish;
    
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

    private void langChange(String langName) throws IOException {
        Button[] buttonList = {btnAdd, btnMeeting, btnMaterial};
        Button[] buttonChangeList = Lang.buttonLangChange(langName, buttonList, "index");

        for (int i = 0; i < buttonChangeList.length; i++) {
            buttonList[i].setText(buttonChangeList[i].getText());
        }
    }

    @FXML
    private void btnPortuguesePerformed() throws IOException {
        langChange("Portuguese");
        btnSpanish.setVisible(true);
        btnSpanish.setDisable(false);
        btnSpanish.setLayoutX(14);
        btnPortuguese.setVisible(false);
        btnPortuguese.setDisable(true);
        btnPortuguese.setLayoutX(139);
    }

    @FXML
    private void btnSpanishPerformed() throws IOException {
        langChange("Spanish");
        btnPortuguese.setVisible(true);
        btnPortuguese.setDisable(false);
        btnPortuguese.setLayoutX(14);
        btnSpanish.setVisible(false);
        btnSpanish.setDisable(true);
        btnSpanish.setLayoutX(139);
    }
}
