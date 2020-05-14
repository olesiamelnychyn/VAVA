package application;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import ejb.LogTest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class helpController {
	
	@FXML
    private Button btn_lang;
	
	@FXML
    private Button btn_home;

    @FXML
    private ResourceBundle  rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

    @FXML
    private URL location;

    @FXML
    private TextArea txt;

    @FXML
    void initialize() {
    	assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'helpWindow.fxml'.";
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'helpWindow.fxml'.";
        assert txt != null : "fx:id=\"txt\" was not injected: check your FXML file 'helpWindow.fxml'.";

        btn_lang.setText("en");
        btn_lang.setOnMouseClicked(e ->{ lang();});
        lang();
        
        btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    }
    
    private void lang() {
    	
    	if(btn_lang.getText().equals("en")) {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));
    		btn_lang.setText("sk");
    	} else {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("sk"));
    		btn_lang.setText("en");
    	}
    	
    	btn_home.setText(rb.getString("btn.home"));
    	txt.setText(rb.getString("help"));
    	
    }
    
    private void openWindow(String window, MouseEvent e) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource(window));
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        stage.setTitle("New Window");
	        stage.setScene(scene);
	        stage.show();
	        ((Node)(e.getSource())).getScene().getWindow().hide(); 
	        
    	} catch (IOException ex) {
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window , ex);
    	}
    }
}
