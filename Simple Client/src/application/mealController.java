package application;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.converter.LocalTimeStringConverter;

public class mealController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txt_title;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_add;

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
    private ListView<?> list_prod;

    @FXML
    private ImageView img_view;

    @FXML
    private Spinner<?> spint_time;

    @FXML
    private ComboBox<?> cmbox_prod;

    @FXML
    private Button btn_add_prod;

    @FXML
    private Button btn_del_prod;

    @FXML
    private ListView<?> list_rest;

    @FXML
    private ListView<?> list_reserv;
    
    
    @FXML
    void initialize() {    
        assert txt_title != null : "fx:id=\"txt_title\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_add != null : "fx:id=\"btn_add\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert spin_price != null : "fx:id=\"spin_price\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert list_prod != null : "fx:id=\"list_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert img_view != null : "fx:id=\"img_view\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert spint_time != null : "fx:id=\"spint_time\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert cmbox_prod != null : "fx:id=\"cmbox_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_add_prod != null : "fx:id=\"btn_add_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_del_prod != null : "fx:id=\"btn_del_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert list_rest != null : "fx:id=\"list_rest\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert list_reserv != null : "fx:id=\"list_reserv\" was not injected: check your FXML file 'mealWindow.fxml'.";

    }
}
