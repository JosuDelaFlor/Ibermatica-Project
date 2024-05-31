package ibermatica_project.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/*
 * The Lang class is responsible for managing all the translation of the scenes
 */

public class Lang extends Properties {
    private static final long serialVersionUID = 1L;

    public Lang(String lang) {
        switch (lang) {
            case "Spanish":
                getProperties("spanish.properties");
                break;
                case "English":
            default:
                getProperties("english.properties");
                break;
        }
    }

    private void getProperties(String lang) {
        try {
            this.load(getClass().getResourceAsStream(lang));
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @SuppressWarnings("exports")
    public static Label[] langChangeLabel(String langName, Label[] labelList, String scene) throws IOException{
        ArrayList<String> keys = new ArrayList<String>();
        if (scene.equals("admMenu")) {
            keys = setKeys(scene);
        } else if (scene.equals("userManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("register")) {
            keys = setKeys(scene); 
        } else if (scene.equals("userModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineAdd")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationAdd")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("employeeMenu")) {
            keys = setKeys(scene);
        }

        Lang lang = new Lang(langName);

        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).contains("lbl")) {
                labelList[i].setText(lang.getProperty(keys.get(i)));
            }
        }
        return labelList;
    }

    @SuppressWarnings("exports")
    public static Button[] langChangeButton(String langName, Button[] buttonsList, String scene) throws IOException{
        ArrayList<String> keys = new ArrayList<String>();
        if (scene.equals("admMenu")) {
            keys = setKeys(scene);
        } else if (scene.equals("userManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("register")) {
            keys = setKeys(scene);
        } else if (scene.equals("userModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineAdd")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationAdd")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("employeeMenu")) {
            keys = setKeys(scene);
        }

        Lang lang = new Lang(langName);

        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).contains("btn")) {
                for (int j = 0; j < buttonsList.length; j++) {
                    buttonsList[j].setText(lang.getProperty(keys.get(i)));
                    i++;
                }
            }
        }
        return buttonsList;
    }

    @SuppressWarnings("exports")
    public static TextField[] langChangeTextField(String langName, TextField[] textFieldList, String scene) throws IOException{
        ArrayList<String> keys = new ArrayList<String>();
        if (scene.equals("admMenu")) {
            keys = setKeys(scene);
        } else if (scene.equals("userManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("register")) {
            keys = setKeys(scene);
        } else if (scene.equals("userModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineAdd")) {
            keys = setKeys(scene);
        } else if (scene.equals("machineModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationManagement")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationAdd")) {
            keys = setKeys(scene);
        } else if (scene.equals("reservationModify")) {
            keys = setKeys(scene);
        } else if (scene.equals("employeeMenu")) {
            keys = setKeys(scene);
        }
        
        Lang lang = new Lang(langName);

        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).contains("txf")) {
                for (int j = 0; j < textFieldList.length; j++) {
                    textFieldList[j].setPromptText(lang.getProperty(keys.get(i)));
                    i++;
                }
            }
        }
        return textFieldList;
    }

    private static ArrayList<String> setKeys(String scene) {
        ArrayList<String> keys = new ArrayList<String>();
        if (scene.equals("admMenu")) {
            keys.addAll(Arrays.asList("lblTitle", "lblName", "lblSurname", "lblUserName", "lblEmail", "lblPhoneNumber", "lblUserType", "lblUserManagement",
                "lblMachineManagement", "lblReservesManagement", "btnRestartPassword"));
        } else if (scene.equals("userManagement")) {
            keys.addAll(Arrays.asList("lblUserTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "btnSearch",
                "btnModify", "btnUserCreate", "txfSearch"));
        } else if (scene.equals("register")) {
            keys.addAll(Arrays.asList("lblUserTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "lblId",
                "lblName", "lblSurname", "lblEmail", "lblPhoneNumber", "lblUserName", "lblPassword1", "lblPassword2",
                "lblUserType", "btnRegister", "btnGenerateEmail", "btnGenerateUser", "btnReset", "btnVisualice", "btnModify",
                "txfId", "txfName", "txfSurname", "txfEmail", "txfTlfNumber", "txfUser"));
        } else if (scene.equals("userModify")) {
            keys.addAll(Arrays.asList("lblUserTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "lblId",
                "lblName", "lblSurname", "lblEmail", "lblPhoneNumber", "lblUserName", "lblUserType", "lblRegisterDate",
                "btnPasswordRestart", "btnVisualice", "btnModify", "btnCreate", "btnSearch",
                "txfId", "txfName", "txfSurname", "txfEmail", "txfTlfNumber", "txfUser"));
        } else if (scene.equals("machineManagement")) {
            keys.addAll(Arrays.asList("lblMachineTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "btnSearch",
                "btnModify", "btnCreate", "txfSearch"));
        } else if (scene.equals("machineAdd")) {
            keys.addAll(Arrays.asList("lblMachineTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "lblSerialNumber", "lblName",
                "lblMachineType", "lblStatus",
                "btnAdd", "btnReset", "btnVisualice", "btnModify", "txfSerialNumber", "txfName"));
        } else if (scene.equals("machineModify")) {
            keys.addAll(Arrays.asList("lblSerialNumber", "lblName", "lblMachineTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "lblAdquisitionDate",
                "lblMachineType", "lblStatus", "btnSearch", "btnModify", "btnCreate", "btnVisualize", "txfSerialNumber", "txfName", "txfAdquisitionDate"));
        } else if (scene.equals("reservationManagement")) {
            keys.addAll(Arrays.asList("lblReservationTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "btnSearch",
                "btnModify", "btnCreate", "txfSearch"));
        } else if (scene.equals("reservationAdd")) {
            keys.addAll(Arrays.asList("lblReservationTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "lblStartDate", "lblEndDate",
                "btnReset", "btnModify", "btnCreate", "btnVisualize", "txfStartDate", "txfId", "txfEndDate", "txfSerialNumber"));
        } else if (scene.equals("reservationModify")) {
            keys.addAll(Arrays.asList("lblReservationTitle", "lblUserManagement", "lblMachineManagement", "lblReservesManagement", "lblStartDate", "lblEndDate", "lblSerialNumber", "lblId",
                "btnSearch", "btnModify", "btnCreate", "btnVisualize", "txfStartDate", "txfId", "txfEndDate", "txfSerialNumber"));
        } else if (scene.equals("employeeMenu")) {
            keys.addAll(Arrays.asList("lblName", "lblSurname", "lblEmail", "lblPhoneNumber", "lblUserName", "lblStartDate", "lblEndDate", "lblSerialNumber", "lblTitle",
                "btnAddReservation", "btnPasswordRestart", "txfStartDate", "txfEndDate", "txfSerialNumber"));
        }
        return keys;
    }
}
