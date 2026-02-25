// Gabriel Ferreira
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.Objects;

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
     * Creates a controller with an application reference.
     *
     * @param app application instance
     */
    public MenuController(PuzzleApplication app) {
        this.app = app;
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

    @Override
    public String toString() {
        return "MenuController{hasApp=" + (app != null)
                + ", hasStatusLabel=" + (statusLabel != null) + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MenuController other)) return false;
        return Objects.equals(app, other.app) && Objects.equals(statusLabel, other.statusLabel);
    }
}
