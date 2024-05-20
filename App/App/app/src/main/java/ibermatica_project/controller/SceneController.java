package ibermatica_project.controller;

import java.io.IOException;

import ibermatica_project.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneController {
    private static Scene scene;

    private static Stage mainStage;

    @SuppressWarnings({ "exports", "static-access" })
    public SceneController(Stage stage) throws IOException {
        this.mainStage = stage;
        loadIndexScene();
    }

    public void loadIndexScene() throws IOException {
        scene = new Scene(loadFXML("fxml/index"), 640, 480);
        mainStage.setTitle("GESTIÓN IBERMÁTICA");
        // mainStage.getIcons().add(new Image(getClass().getResourceAsStream("favicon.png")));
        mainStage.initStyle(StageStyle.DECORATED);
        mainStage.setResizable(false);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void loadAdmMenuScene() throws IOException {
        scene = new Scene(loadFXML("fxml/admMenu"), 900, 600);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
