package echo.ui;

import java.io.IOException;

import echo.Echo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Echo using FXML.
 * This class serves as the entry point for the JavaFX application,
 * loading the main window layout and initializing the Echo chatbot.
 */
public class Main extends Application {

    private static final String MAIN_WINDOW_FXML_PATH = "/view/MainWindow.fxml";
    private Echo echo = new Echo();

    /**
     * Starts the JavaFX application and displays the main window.
     * Loads the FXML layout, creates the scene, configures the stage,
     * and injects the Echo instance into the controller.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Main.MAIN_WINDOW_FXML_PATH));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setEcho(echo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
