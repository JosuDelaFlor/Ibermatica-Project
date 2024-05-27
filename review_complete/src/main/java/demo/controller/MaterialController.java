package demo.controller;

import java.io.IOException;

import demo.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class MaterialController {
    @FXML
    Label lblTotalPrice;

    @FXML
    VBox vboxCheckBoxes;

    @FXML
    TextArea txtAreaInfo;

    @FXML
    Button btnBack, btnBuy;

    CheckBox checkBox;

    /**
     * Tienen que aparecer nada mas iniciar la escena una lista de los Materiales
     * en forma de CheckBox en Vbox
     * @throws IOException
     */

    @FXML
    private void initialize() throws IOException {
        // TODO
    }

    /**
     * Nos tiene que generar un ticket con el formato que se ve en el README
     * @throws IOException
     */

    @FXML
    private void buy() throws IOException {
        // TODO
    }

    /**
     * Tiene que actualizar el label de (lblTotalPrice) cada vez que le seleccionemos
     * una CheckBox (Nos tiene que ir sumando el dinero)
     * @throws IOException
     */

    @FXML
    private void setTotalPrice() throws IOException {
        checkBox.setOnAction(event -> {
            // TODO
        });
    }

    @FXML
    private void loadIndexScene() throws IOException {
        App.setRoot("fxml/index");
    }
}
