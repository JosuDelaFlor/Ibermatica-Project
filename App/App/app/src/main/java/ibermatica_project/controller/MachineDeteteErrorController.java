package ibermatica_project.controller;

import java.io.IOException;

import ibermatica_project.model.Machine;
import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.Validations;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MachineDeteteErrorController {
    @FXML
    Label lblInfo;

    @FXML
    Button btnDelete, btnBack;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    private static Machine deleteMachine = MachineManagementMenuController.getDeleteMachine();

    @FXML
    private void initialize() throws IOException {
        lblInfo.setText("La maquina "+deleteMachine.getName()+" no se puede eliminar por que tiene reservas a su cargo\ndesea eliminar "+
            "todas sus reservas?");
    }

    @FXML
    private void deleteReserver() throws IOException {
        boolean result = db.deleteReservationWithSerialNumber(deleteMachine.getSerialNumber());
        if (!result) {
            Validations.generateSuccessAltert("Se han eliminado correctamente todas las reservas agregadas a la maquina de "+deleteMachine.getName());
            loadMachineManagementMenuScene();
        } else {
            Validations.generateErrorAltert("Hemo tenido problemas al eliminar las reservas agregadas a la maquina de "+deleteMachine.getName());
        }
    }

    @FXML
    private void loadMachineManagementMenuScene() throws IOException {
        SceneController.loadMachineManagementMenuScene();
    }
}
