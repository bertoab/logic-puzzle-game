import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
* PuzzleApplication is the entrypoint class to this project. It loads
* the first Scene to the Stage provided by the JavaFX runtime.
* @author Roberto Baez
*/
public class PuzzleApplication extends Application {
    private Stage primaryStage;
    /**
    * Sets the Stage to a Scene with two menu options.
    * @param stage the primary Stage for the application
    */
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        showMenuScene();
    }

    public void showMenuScene() {
        showMenuScene("");
    }

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

    public void showGameScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(PuzzleApplication.class.getResource("hello-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to parse fxml file");
        }
        fxmlLoader.<HelloController>getController().setApp(this);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
