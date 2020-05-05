package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tooltip;

public class reservController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private ComboBox<?> cmbox_rest;

    @FXML
    private Button btn_add_meal;

    @FXML
    private Button btn_del_meal;

    @FXML
    private ListView<?> list_emp;

    @FXML
    private ListView<?> list_meal;

    @FXML
    private DatePicker date_start;

    @FXML
    private DatePicker date_end;

    @FXML
    private ComboBox<?> cmb_meal;

    @FXML
    private ComboBox<?> cmb_emp;

    @FXML
    private Button btn_add_emp;

    @FXML
    private Button btn_del_emp;

    @FXML
    void initialize() {
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert spin_price != null : "fx:id=\"spin_price\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert cmbox_rest != null : "fx:id=\"cmbox_rest\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_add_meal != null : "fx:id=\"btn_add_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_del_meal != null : "fx:id=\"btn_del_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert list_emp != null : "fx:id=\"list_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert list_meal != null : "fx:id=\"list_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert date_start != null : "fx:id=\"date_start\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert date_end != null : "fx:id=\"date_end\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert cmb_meal != null : "fx:id=\"cmb_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert cmb_emp != null : "fx:id=\"cmb_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_add_emp != null : "fx:id=\"btn_add_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_del_emp != null : "fx:id=\"btn_del_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";

    }
}
