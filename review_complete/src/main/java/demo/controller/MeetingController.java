package demo.controller;

import java.io.IOException;

import demo.App;
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

    /**
     * Se tiene que agregar los en el TextField las reuniones en el formato que aparece en el README
     * @throws IOException
     */

    @FXML
    private void add() throws IOException {
        // TODO
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
        // TODO
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
        // TODO 
        return false;
    }

    @FXML
    private void loadIndexScene() throws IOException {
        App.setRoot("fxml/index");
    }
}
