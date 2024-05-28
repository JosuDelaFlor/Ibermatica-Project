package ibermatica_project.controller;

import java.io.IOException;

import ibermatica_project.lang.Lang;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Prueba {
    static AdmMenuController admMenuController = new AdmMenuController();

    @FXML
    public void prueba() {
        admMenuController.lblEmail.setText("kk");
    }

    public void langChange(String langName) throws IOException{
        Lang lang = new Lang(langName);
        
        admMenuController.lblTitleAdm.setText(lang.getProperty("lblTitle"));
        admMenuController.lblNameAdm.setText(lang.getProperty("lblName"));
        admMenuController.lblSurnameAdm.setText(lang.getProperty("lblSurname"));
        admMenuController.lblUserNameAdm.setText(lang.getProperty("lblUserName"));
        admMenuController.lblEmailAdm.setText(lang.getProperty("lblEmail"));
        admMenuController.lblPhoneNumberAdm.setText(lang.getProperty("lblPhoneNumber"));
        admMenuController.lblUserTypeAdm.setText(lang.getProperty("lblUserType"));
        
    }
}
