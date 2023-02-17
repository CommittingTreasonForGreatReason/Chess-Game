package ChessGroup.Chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application { 

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = load_scene();
        stage.setScene(scene);
        stage.show();
        scene.setOnKeyPressed(this::keyPressed);
    }
    private static Scene load_scene() throws IOException {
        final var primary = loadFXML("primary");
        if (primary == null) {
            throw new IllegalStateException();
        }
        return new Scene(primary, GUIController.initial_width, GUIController.initial_height);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private void keyPressed(KeyEvent e) {
        GameLogic.getGameLogicInstance().keyPressed(e);
    }

}