package application;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class helpController {
	
	@FXML
    private Button btn_lang;

    @FXML
    private ResourceBundle  rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

    @FXML
    private URL location;

    @FXML
    private TextArea txt;

    @FXML
    void initialize() {
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'helpWindow.fxml'.";
        assert txt != null : "fx:id=\"txt\" was not injected: check your FXML file 'helpWindow.fxml'.";

        btn_lang.setText("en");
        btn_lang.setOnMouseClicked(e ->{ lang();});
        lang();
    }
    
    private void lang() {
    	
    	if(btn_lang.getText().equals("en")) {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));
    		btn_lang.setText("sk");
    	} else {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("sk"));
    		btn_lang.setText("en");
    	}
    	
    	txt.setText(rb.getString("help"));
    	
    }
}
