package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class suppSearchController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView<?> table;

    @FXML
    private Button btn_search;

    @FXML
    private TextField txt_title;

    @FXML
    private TextField txt_email;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_delete;

    @FXML
    private SplitMenuButton btn_export;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;

    @FXML
    private TextField txt_phone;

    @FXML
    private Button btn_new;

    @FXML
    void initialize() {
        assert txt_search != null : "fx:id=\"txt_search\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert txt_title != null : "fx:id=\"txt_title\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert txt_email != null : "fx:id=\"txt_email\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert txt_phone != null : "fx:id=\"txt_phone\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";

    }
}
