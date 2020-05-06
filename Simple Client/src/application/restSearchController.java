package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.RestaurantRemote;
import ejb.MyExeception;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Restaurant;
import objects.Zip;

public class restSearchController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Restaurant> table;

    @FXML
    private Button btn_search;

    @FXML
    private TextField txt_from;

    @FXML
    private TextField txt_to;

    @FXML
    private ComboBox<String> cmbox_zip;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_new;

    @FXML
    private Button btn_delete;

    @FXML
    private SplitMenuButton btn_export;

    @FXML
    private Button btn_stat;

    @FXML
    private Tooltip tool_tip1;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;

    Dictionary<Integer, Restaurant> result;
    ObservableList<Restaurant> data ;

    @FXML
    void initialize() {
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert txt_from != null : "fx:id=\"txt_from\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert txt_to != null : "fx:id=\"txt_to\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert cmbox_zip != null : "fx:id=\"cmbox_zip\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_stat != null : "fx:id=\"btn_stat\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert tool_tip1 != null : "fx:id=\"tool_tip1\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        
        TableColumn <Restaurant, Integer> capCol = new TableColumn <Restaurant, Integer> ("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        TableColumn <Restaurant, String> zip_codeCol = new TableColumn <Restaurant, String> ("code");
        zip_codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn <Restaurant, String> stateCol = new TableColumn <Restaurant, String> ("State");
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        
        data = FXCollections.observableArrayList();
        table.getColumns().add(capCol);
        table.getColumns().add(zip_codeCol);
        table.getColumns().add(stateCol);
        table.setEditable(true);
        
        ObservableList<String> zip = FXCollections.observableArrayList();
        zip.add("Choose zip");
        
        try {
			for (String p : this.getZIP()) { 		      
				zip.add(p);		
			}
		} catch (MyExeception e2) {
			e2.printStackTrace();
		} catch (NamingException e2) {
			e2.printStackTrace();
		}
        cmbox_zip.setItems(zip);
        cmbox_zip.getSelectionModel().select(0);
    	txt_from.setText("0");
    	int capacity;
		try {
			capacity = this.getMaxCap();
			txt_to.setText(String.valueOf(capacity));
		} catch (MyExeception e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}

    	search();
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {openWindow("empWindow.fxml", e);});
    	
    	btn_export.setOnMouseClicked(e -> {
    		//TODO
    	});
    	
    	btn_help.setOnMouseClicked(e->{
    	});
    	btn_delete.setOnMouseClicked(e->{
    		
    		
    		ObservableList <Restaurant> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Restaurant Restaurant_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(Restaurant_del)) {
	    	            	System.out.println("Gonna delete this one"+k);
	    	            	try {
								delete(k);
							} catch (MyExeception | NamingException e1) {
								e1.printStackTrace();
							}
	    	            	break1=1;
	    	            	break;
	    	            }
	    	            
	    	        }
	    	        if (break1==1)
	    	        	break;
	    	    	}
				}
    		search();
    	});
    	
        btn_search.setOnMouseClicked(e ->{search();});
    }
    
    private void search() {
		if(result!=null) {
			((Hashtable<Integer, Restaurant>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
    	try {
    		args.put("vis_from", Integer.valueOf(txt_from.getText()).toString());
    		txt_from.setText(Integer.valueOf(txt_from.getText()).toString());
    	} catch (NumberFormatException ex) {
    		txt_from.setText("0");
    		args.put("vis_from", String.valueOf(0));
    	}
    	try {
    		args.put("vis_to", Integer.valueOf(txt_to.getText()).toString());
    		txt_to.setText(Double.valueOf(txt_to.getText()).toString());
    	} catch (NumberFormatException ex) {
    		int cap;
			try {
				cap = this.getMaxCap();
				txt_to.setText(String.valueOf(cap));
				args.put("vis_to", String.valueOf(cap));
			} catch (MyExeception e) {
				e.printStackTrace();
				args.put("vis_to", String.valueOf(50));
			} catch (NamingException e) {
				e.printStackTrace();
				args.put("vis_to", String.valueOf(50));
			}
    	}
    	if(cmbox_zip.getValue()!="Choose zip") {
    		args.put("zip", cmbox_zip.getValue().split(" ")[0]);
    	}else {
    		args.put("zip", "");
    	}
    	
//    System.out.println(args);
//      DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
       
    	try {
    		final Dictionary<Integer, Restaurant> result1 = doRequest(args);
    		result=result1;
    		
    		Enumeration<Integer> enam = result.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            data.add(result.get(k));
    		}
        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (MyExeception ex) {
           System.out.print(ex.getMessage());
        }
    	table.setItems(data);
    }
    
    private Dictionary<Integer, Restaurant> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
        
        Context ctx = new InitialContext();
        RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
    	Dictionary<Integer, Restaurant> la = RestaurantRemote.searchRestaurant(args);
    	return la;
    }
    
    private int getMaxCap() throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
    	RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        int cap = RestaurantRemote.getMaxCapacity();
    	return cap;
    }
    
    private ArrayList<String> getZIP() throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
    	RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        ArrayList<Zip> zip = RestaurantRemote.getZip();
        ArrayList<String> z = new ArrayList<String>();
        for (Zip p : zip) { 		      
			z.add(p.getCode()+" "+p.getState());		
		}
    	return z;
    }
    
    private void delete(Integer id) throws MyExeception, NamingException {  
      Context ctx = new InitialContext();
      RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
      System.out.print("process");
      RestaurantRemote.deleteRestaurant(id);
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
