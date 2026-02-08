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

    private static final String TITLE = "Echo, your friendly task tracker";
    private static final int SCREEN_MIN_HEIGHT = 220;
    private static final int SCREEN_MIN_WIDTH = 417;

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
            createScene(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Main.MAIN_WINDOW_FXML_PATH));
        AnchorPane ap = fxmlLoader.load();
        Scene scene = new Scene(ap);

        stage.setTitle(Main.TITLE);
        stage.setScene(scene);
        stage.setMinHeight(Main.SCREEN_MIN_HEIGHT);
        stage.setMinWidth(Main.SCREEN_MIN_WIDTH);
        fxmlLoader.<MainWindow>getController().setEcho(this.echo);
        stage.show();
    }
}
