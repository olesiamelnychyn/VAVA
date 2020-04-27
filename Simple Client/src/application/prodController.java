package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class prodController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txt_title;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_undo;

    @FXML
    private SplitMenuButton btn_export;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;

    @FXML
    private Spinner<?> spin_price;

    @FXML
    private ComboBox<?> cmbox_supp;

    @FXML
    private ListView<?> list_rest;

    @FXML
    private ListView<?> list_reserv;

    @FXML
    void initialize() {
        assert txt_title != null : "fx:id=\"txt_title\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert spin_price != null : "fx:id=\"spin_price\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert cmbox_supp != null : "fx:id=\"cmbox_supp\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert list_rest != null : "fx:id=\"list_rest\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert list_reserv != null : "fx:id=\"list_reserv\" was not injected: check your FXML file 'prodWindow.fxml'.";

    }
}
