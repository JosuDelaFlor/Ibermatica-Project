package proposal.controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import proposal.App;
import proposal.model.Reservation;
import proposal.model.base.DataBase;

public class ReservationViewController {
    @FXML
    Button btnback, btnRegister, btnResign, btnReservation, btnLogOut, btnModify;

    @SuppressWarnings("rawtypes")
    @FXML
    TableView tbtReservation;

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    @SuppressWarnings("unchecked")
    @FXML
    protected void initialize() {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        reservationList = db.searchAllReservations();

        TableColumn<Reservation, String> column1 = new TableColumn<>("DNI");
        column1.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<Reservation, String> column2 = new TableColumn<>("Numero de serie");
        column2.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        TableColumn<Reservation, String> column3 = new TableColumn<>("Fecha inicial");
        column3.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Reservation, String> column4 = new TableColumn<>("Fecha final");
        column4.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Reservation, String> column5 = new TableColumn<>("Id de reservacion");
        column5.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        
        tbtReservation.getColumns().add(column1);
        tbtReservation.getColumns().add(column2);
        tbtReservation.getColumns().add(column3);
        tbtReservation.getColumns().add(column4);
        tbtReservation.getColumns().add(column5);

        for (int i = 0; i < reservationList.size(); i++) {
            tbtReservation.getItems().add(new Reservation(reservationList.get(i).getUserId(), reservationList.get(i).getSerialNumber(), reservationList.get(i).getStartDate(),
                    reservationList.get(i).getEndDate(), reservationList.get(i).getReservationId()));
        }

        column1.setPrefWidth(120.5);
        column2.setPrefWidth(120.5);
        column3.setPrefWidth(120);
        column4.setPrefWidth(120);
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("fxml/index");
    }

    @FXML
    private void loadUserDataScene() throws IOException {
        App.setRoot("fxml/userData");
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("fxml/admMenu");
    }

    @FXML
    private void loadModifyReservationScene() throws IOException {
        App.setRoot("fxml/modifyReservation");
    }
}
