package ibermatica_project;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import ibermatica_project.controller.SceneController;

/**
 * JavaFX App
 */

public class App extends Application {

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/favicon.png")));
        new SceneController(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}