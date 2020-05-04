import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class helpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txt;

    @FXML
    void initialize() {
        assert txt != null : "fx:id=\"txt\" was not injected: check your FXML file 'helpWindow.fxml'.";

    }
}
