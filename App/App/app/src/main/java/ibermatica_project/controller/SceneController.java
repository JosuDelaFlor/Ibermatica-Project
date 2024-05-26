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

    public static void loadEmployeeMenuScene() throws IOException {
        scene = new Scene(loadFXML("fxml/employeeMenu"), 900, 600);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void loadLoginScene() throws IOException {
        scene = new Scene(loadFXML("fxml/index"), 640, 480);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void loadRestartPasswordScene() throws IOException {
        scene = new Scene(loadFXML("fxml/restartPassword"), 320, 240);
        mainStage.setScene(scene);
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setY(150);
        mainStage.setX(450);
        mainStage.show();
    }

    public static void loadRestartUserPasswordScene() throws IOException {
        scene = new Scene(loadFXML("fxml/restartUserPassword"), 320, 240);
        mainStage.setScene(scene);
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setY(150);
        mainStage.setX(450);
        mainStage.show();
    }

    public static void loadUserModifyMenuScene() throws IOException {
        scene = new Scene(loadFXML("fxml/userModifyMenu"), 900, 600);
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
