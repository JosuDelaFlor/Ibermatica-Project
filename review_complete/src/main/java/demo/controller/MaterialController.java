package demo.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import demo.App;
import demo.model.Material;
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

    List<Material> materialList = Material.materialInitialize();
    ArrayList<CheckBox> checkBoxsList = new ArrayList<>();

    /**
     * Tienen que aparecer nada mas iniciar la escena una lista de los Materiales
     * en forma de CheckBox en Vbox
     * @throws IOException
     */

    @SuppressWarnings("unchecked")
    @FXML
    private void initialize() throws IOException {
        for (Material material : materialList) {
            checkBox = new CheckBox(material.toString());
            checkBox.setOnAction(setTotalPrice());
            vboxCheckBoxes.getChildren().add(checkBox);
            checkBoxsList.add(checkBox);
        }
    }

    /**
     * Nos tiene que generar un ticket con el formato que se ve en el README
     * @throws IOException
     */

    @FXML
    private void buy() throws IOException {
        ArrayList<Material> producList = new ArrayList<Material>();
        txtAreaInfo.setText("");

        int count = 0;
        for (int i = 0; i < checkBoxsList.size(); i++) {
            if (checkBoxsList.get(i).isSelected()) {
                txtAreaInfo.appendText(materialList.get(i).toString()+"\n");
                producList.add(materialList.get(i));
                count++;
            } 
        }
        txtAreaInfo.appendText("\t................\nCantidad de productos: "+Integer.toString(count)+
            "\nPrecio total: ");

        double totalPrice = 0;
        for (Material product : producList) {
            txtAreaInfo.appendText(product.getName()+"  ");
            totalPrice += product.getPrice();
        }

        txtAreaInfo.appendText(" =  "+totalPrice);
    }

    /**
     * Tiene que actualizar el label de (lblTotalPrice) cada vez que le seleccionemos
     * una CheckBox (Nos tiene que ir sumando el dinero)
     * @throws IOException
     */

    @SuppressWarnings("rawtypes")
    @FXML
    private EventHandler setTotalPrice() throws IOException {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                lblTotalPrice.setText(Double.toString(calculateTotalPrice()));
            }
        };
        return event;
    }

    private double calculateTotalPrice() {
        double count = 0;
        for (int i = 0; i < checkBoxsList.size(); i++) {
            if (checkBoxsList.get(i).isSelected()) {
                count += materialList.get(i).getPrice();
            }
        }
        return count;
    }

    @FXML
    private void loadIndexScene() throws IOException {
        App.setRoot("fxml/index");
    }
}
