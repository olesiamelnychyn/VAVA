package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tooltip;

public class restController {

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
    private Button btn_help1;

    @FXML
    private Tooltip tool_tip1;

    @FXML
    private Spinner<?> spin_cap;

    @FXML
    private ComboBox<?> cmbox_zip;

    @FXML
    private Button btn_add_meal;

    @FXML
    private Button btn_del_meal;

    @FXML
    private ListView<?> list_emp;

    @FXML
    private ListView<?> list_meal;

    @FXML
    private ComboBox<?> cmb_meal;

    @FXML
    private ComboBox<?> cmb_emp;

    @FXML
    private Button btn_add_emp;

    @FXML
    private Button btn_del_emp;

    @FXML
    private ComboBox<?> cmb_order;

    @FXML
    private Button btn_add_meal_to_order;

    @FXML
    private Button btn_del_meal_to_order;

    @FXML
    private Button btn_confirm_order;

    @FXML
    private ListView<?> list_orders;

    @FXML
    void initialize() {
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_help1 != null : "fx:id=\"btn_help1\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert tool_tip1 != null : "fx:id=\"tool_tip1\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert spin_cap != null : "fx:id=\"spin_cap\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert cmbox_zip != null : "fx:id=\"cmbox_zip\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_add_meal != null : "fx:id=\"btn_add_meal\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_del_meal != null : "fx:id=\"btn_del_meal\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert list_emp != null : "fx:id=\"list_emp\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert list_meal != null : "fx:id=\"list_meal\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert cmb_meal != null : "fx:id=\"cmb_meal\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert cmb_emp != null : "fx:id=\"cmb_emp\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_add_emp != null : "fx:id=\"btn_add_emp\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_del_emp != null : "fx:id=\"btn_del_emp\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert cmb_order != null : "fx:id=\"cmb_order\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_add_meal_to_order != null : "fx:id=\"btn_add_meal_to_order\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_del_meal_to_order != null : "fx:id=\"btn_del_meal_to_order\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_confirm_order != null : "fx:id=\"btn_confirm_order\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert list_orders != null : "fx:id=\"list_orders\" was not injected: check your FXML file 'restWindow.fxml'.";

    }
}
