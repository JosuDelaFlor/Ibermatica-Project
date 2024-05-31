package demo.lang;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@SuppressWarnings({"exports", "rawtypes"})
public class Input {
    private Button button;
    private Label label;
    private TextField textField;
    private ComboBox comboBox;
    private CheckBox checkBox;
    private DatePicker datePicker;
    
    public Input(Button button, Label label) { // Index&Material
        this.button = button;
        this.label = label;
    }

    public Input(Button button, Label label, TextField textField, ComboBox comboBox, CheckBox checkBox) { // student
        this.button = button;
        this.label = label;
        this.textField = textField;
        this.comboBox = comboBox;
        this.checkBox = checkBox;
    }

    public Input(Button button, Label label, TextField textField, DatePicker datePicker) { // Meeting
        this.button = button;
        this.label = label;
        this.textField = textField;
        this.datePicker = datePicker;
    }

    public Button getButton() {
        return button;
    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }
}
