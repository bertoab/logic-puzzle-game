import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the start menu screen.
 * Handles menu button clicks and delegates scene changes to the app.
 * @author Gabriel Ferreira
 */
public class MenuController {

    private PuzzleApplication app;

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
    public void setApp(PuzzleApplication app) {
        this.app = app;
    }

    /**
     * Starts a new puzzle round by asking the application to show the game scene.
     */
    @FXML
    private void handleStartPuzzle() {
        app.showGameScene();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleQuit() {
        Platform.exit();
    }

    // Status label is optional in FXML, so guard against null.
    public void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
