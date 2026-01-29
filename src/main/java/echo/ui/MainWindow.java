package echo.ui;

import echo.Echo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Echo echo;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    public void initialize() {
        this.scrollPane.vvalueProperty().bind(this.dialogContainer.heightProperty());
    }

    /**
     * Injects the Echo instance
     *
     * @param e The Echo instance to be injected
     */
    public void setEcho(Echo e) {
        this.echo = e;
        this.greetUser();
    }

    /**
     * Greets the user in the GUI.
     */
    private void greetUser() {
        String greeting = this.echo.greetUser();
        this.dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(greeting, this.dukeImage));
    }

    /**
     * Closes the GUI window after a delay specified
     *
     * @param delay Amount of time in milliseconds, to wait before closing the GUI.
     */
    private void closeWindowAfterDelay(int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.exit();
        }).start();
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container.
     * Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = this.userInput.getText();
        // nothing to show if user did not type anything but press enter
        // or send
        if (input.length() == 0) {
            return;
        }
        String response = this.echo.getResponse(input);
        this.dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, this.userImage),
                DialogBox.getDukeDialog(response, this.dukeImage)
        );
        this.userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            // delay for 5000ms before exiting the user
            this.closeWindowAfterDelay(5000);
        }
    }
}
