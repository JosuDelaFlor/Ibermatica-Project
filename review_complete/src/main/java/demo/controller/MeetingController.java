package demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import demo.App;
import demo.model.Meeting;
import demo.model.base.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MeetingController {
    @FXML
    DatePicker dpkDate;

    @FXML
    TextField txfName, txfAmount, txfDeleteInput;

    @FXML
    TextArea txtAreaInfo;

    @FXML
    Button btnBack, btnAdd, btnDelete;

    static ArrayList<Meeting> meetingList = new ArrayList<Meeting>();

    @FXML
    private void initialize() throws IOException {
        meetingList.clear();
    }

    /**
     * Se tiene que agregar los en el TextField las reuniones en el formato que aparece en el README
     * @throws IOException
     */

    @FXML
    private void add() throws IOException {
        if (validations()) {
            int count = 1;
            LocalDate date = dpkDate.getValue();
            String name = txfName.getText();
            int amount = Integer.parseInt(txfAmount.getText());
            Meeting meeting = new Meeting(name, amount, date);
            meetingList.add(meeting);
            txtAreaInfo.clear();
            for (Meeting meetingIt : meetingList) {
                txtAreaInfo.appendText(Integer.toString(count)+meetingIt.toString()+"\n");
                count++;
            }
            resetInputs();
        }
    }

    /*
     * OPCIONAL
     */

    /**
     * Se tiene que eliminar del TextArea la reunion dependiendo del nombre insertado
     * @throws IOException
     */

    @FXML
    private void delete() throws IOException {
        boolean exists = false;
        for (int i = 0; i < meetingList.size(); i++) {
            if (meetingList.get(i).getName().equals(txfDeleteInput.getText())) {
                meetingList.remove(i);
                exists = true;
            }
        }

        if (!exists) {
            Validation.generateAlert("No existe ninguna reunion con ese nombre");
            resetInputs();
        } else {
            int count = 1;
            txtAreaInfo.clear();
            for (Meeting meeting : meetingList) {
                txtAreaInfo.appendText(Integer.toString(count)+meeting.toString()+"\n");
                count++;
            }
            resetInputs();
        }
    }

    /**
     * Comprueba que los Datos insertados cumplan con los requisitos:
     *  1. El formato de la fecha es correcto
     *  2. La fecha tiene que ser superior a la fecha actual
     *  3. El nombre no puede contener numeros
     *  4. La cantidad de asistentes solo puede contener valores numericos y como maximo 2 digitos
     *  5. Todos los campos tienen que estar completados
     *  * Las validaciones ya estan hechas solo tienes que aplicarlas en el metodo
     *  * Estan hechas en el model de Validations
     * @return
     * @throws IOException
     */

    @FXML
    private boolean validations() throws IOException {
        boolean dateV = true, nameV = true, amountV = true;

        if (!Validation.checkDate(dpkDate.getValue())) {
            dateV = false;
        } else if (!Validation.checkIsNumeric(txfName.getText())) {
            nameV = false;
        } else if (!Validation.checkMeetingAmount(txfAmount.getText())) {
            amountV = false;
        }

        if (dateV && nameV && amountV) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 1. Comprobar si el nombre de la reunion que vamos a eliminar existe
     * @return
     * @throws IOException
     */

    @FXML
    private boolean checkMeetingName() throws IOException {
        return (!Validation.checkMeetingName(txfName.getText(), meetingList)) ? false : true;
    }

    /**
     * Al agregar una nueva reunion 
     * @throws IOException
     */

    @FXML
    private void resetInputs() throws IOException {
        txfAmount.setText("");
        txfDeleteInput.setText("");
        txfName.setText("");
        dpkDate.setValue(null);
    }

    @FXML
    private void loadIndexScene() throws IOException {
        App.setRoot("fxml/index");
    }
}
