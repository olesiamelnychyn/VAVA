package application;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ejb.ProductRemote;
import ejb.SupplierRemote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Meal;
import objects.Product;
import objects.Reservation;
import objects.Supplier;

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
    private Spinner<Double> spin_price;

    @FXML
    private ComboBox<String> cmbox_supp;

    @FXML
    private ListView<String> list_meal;

    @FXML
    private ListView<String> list_reserv;
    
    Product prod=null;
    Integer id=0;
    Context ctx;
    ProductRemote ProductRemote;

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
        assert list_meal != null : "fx:id=\"list_meal\" was not injected: check your FXML file 'prodWindow.fxml'.";
        assert list_reserv != null : "fx:id=\"list_reserv\" was not injected: check your FXML file 'prodWindow.fxml'.";

        try {
			ctx = new InitialContext();
			ProductRemote =  (ProductRemote) ctx.lookup("ejb:/SimpleEJB2//ProductSessionEJB!ejb.ProductRemote"); 
			
		} catch (NamingException e2) {
			e2.printStackTrace();
		}
        
        btn_help.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
        
        btn_back.setOnMouseClicked(e ->{openWindow("empSearchWindow.fxml",e);});
        
        btn_delete.setOnMouseClicked(e ->{
        	if (id!=-1) {
        		ProductRemote.deleteProduct(id);
        	}
	        openWindow("prodSearchWindow.fxml",e);
        });
        
        btn_save.setOnMouseClicked(e ->{
        	Dictionary <String, String>  args = new Hashtable <>();
        	args.put("id", id.toString());
        	if(prod==null || !txt_title.getText().equals(prod.getTitle())) {
    			args.put("title", "\""+txt_title.getText()+"\"");
        	}
        	if(prod==null || !cmbox_supp.getValue().equals(prod.getSupp_id().toString())) {
        		args.put("supp_id", "\""+cmbox_supp.getValue().split(":")[0]+"\"");
        	}
  	
        	if(prod==null || spin_price.getValue()!=prod.getPrice()) {
        		args.put("price", spin_price.getValue().toString());
        	}
        	
        	ProductRemote.updateProduct(args);
	        openWindow("prodSearchWindow.fxml",e);
			
        });
        
        btn_undo.setOnMouseClicked(e ->{
        	fill();
        });
        
        fill();
    }
    
    private void fill() {
    	spin_price.getValueFactory().setValue(0.0);
    	
    	//Suppliers
    	ObservableList<String> supps = FXCollections.observableArrayList();
    	Context ctx2;
    	try {
    		ctx2 = new InitialContext();
    		SupplierRemote SupplierRemote =  (SupplierRemote) ctx2.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote"); 
        	Dictionary<Integer, Supplier> la = SupplierRemote.searchSupplier(null);
        	
        	Enumeration<Integer> enam = la.keys();
    		String supp_p = null;
            while(enam.hasMoreElements()) {
                Integer k = enam.nextElement();
                String supp = k+": "+la.get(k).getTitle();
                if (prod!=null && prod.getSupp_id().toString().equals(la.get(k).getTitle())) {supp_p=supp;}
                supps.add(supp);
            }
            cmbox_supp.setItems(supps);
            if(prod!=null && supp_p!=null)
            	cmbox_supp.getSelectionModel().select(supp_p);
            else {
            	cmbox_supp.getSelectionModel().select(0);
            }
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(prod!=null) {
    		txt_title.setText(prod.getTitle());
    		spin_price.getValueFactory().setValue(prod.getPrice());
    		
        	//Meals
        	ObservableList<String> meals = FXCollections.observableArrayList();
    		Dictionary<Integer, Meal> la2 = ProductRemote.getMealProduct(id);
    		
    		Enumeration<Integer> enam = la2.keys();
            while(enam.hasMoreElements()) {
                Integer k = enam.nextElement();
                String rest = k+": "+la2.get(k).getTitle()+ " - "+la2.get(k).getPrice();
                meals.add(rest);
            }
            list_meal.setItems(meals);
            
            //Reservations
        	ObservableList<String> rese = FXCollections.observableArrayList();
    		Dictionary<Integer, Reservation> la3 = ProductRemote.getReservProduct(id);
    		
    		enam = la3.keys();
            while(enam.hasMoreElements()) {
                Integer k = enam.nextElement();
                String r = k+": "+la3.get(k).getDate_start()+ " - "+la3.get(k).getDate_end()+", "+la3.get(k).getVisitors();
                rese.add(r);
            }
            list_reserv.setItems(rese);
    	}
    	
    }
    
    public void setProd(Dictionary <Integer, Product> dict) {
    	Enumeration<Integer> enam = dict.keys();
        while(enam.hasMoreElements()) {
        	id = enam.nextElement();
        	prod=dict.get(id);
        }
        fill();
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
    		ex.printStackTrace();
    	}
    }
}
