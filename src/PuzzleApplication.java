// Written by: Roberto Baez
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * PuzzleApplication is the entrypoint class to this project. It loads
 * the first Scene to the Stage provided by the JavaFX runtime.
 *
 * @author Roberto Baez
 */
public class PuzzleApplication extends Application {
    private Stage primaryStage;
    /**
     * Stores the reference to the Stage,
     * and sets the Stage to a menu Scene.
     * @param stage the primary Stage for the application
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        showMenuScene();
    }
    /**
     * Sets the primaryStage to a menu wih no status message.
     */
    public void showMenuScene() {
        showMenuScene("");
    }
    /**
     * Sets the primaryStage to a menu Scene with the given
     * status message. The user has two options:
     * start a new puzzle game or exit the program.
     * @param status the message to display to the user
     */
    public void showMenuScene(String status) {
        FXMLLoader fxmlLoader = new FXMLLoader(PuzzleApplication.class.getResource("menu-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to parse fxml file");
        }
        MenuController menuController = fxmlLoader.<MenuController>getController();
        menuController.setApp(this);
        if (!status.equals(""))
            menuController.setStatus(status);
        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Sets the primaryStage to a logic puzzle gameplay Scene.
     */
    public void showGameScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(PuzzleApplication.class.getResource("game-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to parse fxml file");
        }
        fxmlLoader.<GameController>getController().setApp(this);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
