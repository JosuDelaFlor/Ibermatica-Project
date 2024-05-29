package demo.controller;

import java.io.IOException;

import demo.App;
import demo.lang.Input;
import demo.lang.Lang;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class IndexController {
    @FXML
    Button btnAdd, btnMeeting, btnMaterial, btnPortuguese, btnSpanish;

    @FXML 
    Label lblTitle;
    
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
        Input[] inputList = {new Input(btnAdd, new Label()), new Input(btnMeeting, new Label()), new Input(btnMaterial, new Label()), new Input(new Button(), lblTitle)};
        Input[] changeInputList = Lang.LangChange(langName, inputList, "index");

        for (int i = 0; i < changeInputList.length; i++) {
            inputList[i].getButton().setText(changeInputList[i].getButton().getText());
            inputList[i].getLabel().setText(changeInputList[i].getLabel().getText());
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
