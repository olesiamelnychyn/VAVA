package application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class mainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_emp;

    @FXML
    private Button btn_rest;

    @FXML
    private Button btn_meal;

    @FXML
    private Button btn_prod;

    @FXML
    private Button btn_supp;

    @FXML
    private Button btn_reserv;

    @FXML
    void initialize() {
        assert btn_emp != null : "fx:id=\"btn_emp\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_rest != null : "fx:id=\"btn_rest\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_meal != null : "fx:id=\"btn_meal\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_prod != null : "fx:id=\"btn_prod\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_supp != null : "fx:id=\"btn_supp\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_reserv != null : "fx:id=\"btn_reserv\" was not injected: check your FXML file 'mainWindow.fxml'.";

    }
}
