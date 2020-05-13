package application;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.logging.Level;

import ejb.LogTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class mainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Button btn_lang;

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

    ResourceBundle rbSk =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));
    @FXML
    void initialize() {
    	
        assert btn_emp != null : "fx:id=\"btn_emp\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_rest != null : "fx:id=\"btn_rest\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_meal != null : "fx:id=\"btn_meal\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_prod != null : "fx:id=\"btn_prod\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_supp != null : "fx:id=\"btn_supp\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_reserv != null : "fx:id=\"btn_reserv\" was not injected: check your FXML file 'mainWindow.fxml'.";
        assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'mainWindow.fxml'.";
        btn_lang.setText("en");
        btn_lang.setOnMouseClicked(e ->{ lang();});
        lang();
        
        btn_emp.setOnMouseClicked(e -> { openWindow("empSearchWindow.fxml", e); });
        btn_rest.setOnMouseClicked(e -> { openWindow("restSearchWindow.fxml", e); });
        btn_meal.setOnMouseClicked(e -> { openWindow("mealSearchWindow.fxml", e); });
        btn_prod.setOnMouseClicked(e -> { openWindow("prodSearchWindow.fxml",e); });
        btn_supp.setOnMouseClicked(e -> { openWindow("suppSearchWindow.fxml", e); });
        btn_reserv.setOnMouseClicked(e -> { openWindow("reservSearchWindow.fxml", e); });
        
    }
    
    private void lang() {
    	
    	if(btn_lang.getText().equals("en")) {
    		rbSk =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));
    		btn_lang.setText("sk");
    	} else {
    		rbSk =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("sk"));
    		btn_lang.setText("en");
    	}
    	
    	btn_emp.setText(rbSk.getString("emps"));
    	
    }
    
    private void openWindow(String window, MouseEvent event) {
    	
    	try {
			Parent root = FXMLLoader.load(getClass().getResource(window));
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        stage.setTitle("New Window");
	        stage.setScene(scene);
	        stage.show();
	        ((Node)(event.getSource())).getScene().getWindow().hide(); 
	        
    	} catch (IOException e) {
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window, e);
    	}
}
}
