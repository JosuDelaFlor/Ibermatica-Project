package design;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import design.controller.ScenesController;

/**
 * JavaFX App
 */

public class App extends Application {

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        new ScenesController(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}