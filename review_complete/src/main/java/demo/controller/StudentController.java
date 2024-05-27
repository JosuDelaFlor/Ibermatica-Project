package demo.controller;

import java.io.IOException;
import java.util.ArrayList;

import demo.App;
import demo.model.Student;
import demo.model.base.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class StudentController {
    @FXML
    TextField txfUserId, txfAge, txfName;

    @SuppressWarnings("rawtypes")
    @FXML
    ComboBox comboCourse;

    @FXML
    CheckBox checkDebt;

    @FXML
    Button btnAdd;

    @FXML
    VBox vboxInfo;

    /*
     * PUEDES MODIFICAR LO QUE QUIERAS
     */

    ArrayList<Student> studentList = new ArrayList<>();

    /**
     * Insertar en la ComboBox los valores DAM y DAW y que aparezca un Label con la
     * informacion de los Estudiantes actuales en la VBox
     * @throws IOException
     */

    @SuppressWarnings("unchecked")
    @FXML
    private void initialize() throws IOException {
        comboCourse.getItems().addAll("DAM", "DAW");

        studentList = Student.studentInitialize();
        for (Student student : studentList) {
            Label label = new Label(student.toString());
            vboxInfo.getChildren().add(label);
        }
    }

    /**
     * Agrega un Label en la Vbox con la iformacion de los nuevos estudiantes que se
     * vayan agregando (Los estudiantes que se vayan agregando tienen que ir sumandose a los anteriores)
     * @throws IOException
     */

    @FXML
    private void add() throws IOException {
        if (validations()) {
            boolean debt = (checkDebt.isSelected()) ? true : false;
            Student student = new Student(txfUserId.getText().replaceAll("\\s", ""), 
                txfName.getText().replaceAll("\\s", ""), Integer.parseInt(txfAge.getText().replaceAll("\\s", "")),
                (String) comboCourse.getValue(), debt);

            Label label = new Label(student.toString());
            vboxInfo.getChildren().add(label);
            resetInputs();
        }
    }

    /*
     * Todo lo de abajo si quieres lo haces y si no pues happens
     */

    /**
     * Comprueba que los Datos insertados cumplan con los requisitos:
     *  1. El formato del DNI es el correcto
     *  2. La edad solo puede estar compuesta de numeros (Con un maximo de 2 digitos)
     *  3. El nombre no puede contener numeros
     *  4. Tiene que seleccionar un curso
     *  5. Todos los campos tienen que estar completados
     *  * Las validaciones ya estan hechas solo tienes que aplicarlas en el metodo
     *  * Estan hechas en el model de Validations
     * @throws IOException
     */

    @FXML
    private boolean validations() throws IOException {
        boolean userIdV = true, ageV = true, nameV = true, couseV = true;

        if (!Validation.checkUserId(txfUserId.getText())) {
            userIdV = false;
        } else if (!Validation.checkUserAge(txfAge.getText())) {
            ageV = false;
        } else if (!Validation.checkIsNumeric(txfName.getText())) {
            nameV = false;
        } else if (!Validation.checkCourse((String) comboCourse.getValue())) {
            couseV = false;
        }

        if (userIdV && ageV && nameV && couseV) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Cada vez que se agregen nuevos estudiantes, si se han agregado correctamente
     * se reiniciaran los valores de los inputs
     * @throws IOException
     */

    @FXML
    private void resetInputs() throws IOException {
        txfUserId.setText("");
        txfName.setText("");
        txfAge.setText("");
        checkDebt.setSelected(false);
    }

    @FXML
    private void loadIndexScene() throws IOException {
        App.setRoot("fxml/index");
    }
}   
