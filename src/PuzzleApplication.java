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
    /**
    * Sets the Stage to a Scene with two menu options.
    * @param stage the primary Stage for the application
    */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PuzzleApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }
}
