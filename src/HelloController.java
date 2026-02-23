import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class HelloController {

    @FXML
    private TextArea hintArea;

    @FXML
    private void onHintClicked() {
        hintArea.setText("Hint: blah blah blah");
    }

    @FXML
    private void onClearErrorsClicked() {
       // TODO actually implement this method
    }
}