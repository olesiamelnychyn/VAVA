import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Menu;

public class statisticsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu btnBack;

    @FXML
    private StackedBarChart<?, ?> s_bar_char;

    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'statisticsRes.fxml'.";
        assert s_bar_char != null : "fx:id=\"s_bar_char\" was not injected: check your FXML file 'statisticsRes.fxml'.";

    }
}
