import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the start menu screen.
 * Handles menu button clicks and delegates scene changes to the app.
 * @author Gabriel Ferreira
 */
public class MenuController {

    private HelloApplication app;

    @FXML
    private Label statusLabel;

    /**
     * JavaFX no-arg constructor.
     */
    public MenuController() {
        this.app = null;
    }

    /**
     * Injects the application so the controller can request scene changes.
     *
     * @param app application instance that owns the stage
     */
    public void setApp(HelloApplication app) {
        this.app = app;
    }

    /**
     * Starts a new puzzle round by asking the application to show the game scene.
     */
    @FXML
    private void handleStartPuzzle() {
        if (app == null) {
            setStatus("Menu is not connected to the application.");
            return;
        }

        // TODO: when GameApplication gets a startNewGame() method,
        // call it here (and optionally catch errors to show in statusLabel).
        setStatus("Start clicked. Next step: wire this to app.startNewGame().");
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleQuit() {
        Platform.exit();
    }

    // Status label is optional in FXML, so guard against null.
    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
