package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ejb.LogTest;
import ejb.MyExeception;
import ejb.RestaurantRemote;
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
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Employee;
import objects.Meal;
import objects.Restaurant;
import objects.Zip;

public class restController {

	@FXML
    private Button btn_lang;
	
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
    private Button btn_help1;

    @FXML
    private Tooltip tool_tip1;

    @FXML
    private Spinner<Integer> spin_cap;

    @FXML
    private ComboBox<String> cmbox_zip;

    @FXML
    private Button btn_add_meal;

    @FXML
    private Button btn_del_meal;

    @FXML
    private ListView<String> list_emp;

    @FXML
    private ListView<String> list_meal;

    @FXML
    private ComboBox<String> cmb_meal;

    @FXML
    private ComboBox<String> cmb_emp;

    @FXML
    private Button btn_add_emp;

    @FXML
    private Button btn_del_emp;

    @FXML
    private ComboBox<String> cmb_order;

    @FXML
    private Button btn_add_meal_to_order;

    @FXML
    private Button btn_del_meal_to_order;

    @FXML
    private Button btn_confirm_order;

    @FXML
    private ListView<String> list_orders;
    
    Restaurant rest=null;
    Integer id=0;
    Context ctx;
    RestaurantRemote RestaurantRemote;

    @FXML
    void initialize() {
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'restWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'restWindow.fxml'.";
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


        try {
			ctx = new InitialContext();
			RestaurantRemote=(RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote"); 
			
		} catch (NamingException e2) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to connect to RestaurantRemote", e2);
		}
        
        btn_help1.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
        
        btn_back.setOnMouseClicked(e ->{openWindow("restSearchWindow.fxml",e);});
        
        btn_delete.setOnMouseClicked(e ->{
        	if (id!=0) {
        		RestaurantRemote.deleteRestaurant(id);
        	}
	        openWindow("restSearchWindow.fxml",e);
        });
        
        btn_undo.setOnMouseClicked(e ->{
        	fill();
        });
        
        btn_save.setOnMouseClicked(e ->{
        	Dictionary <String, String>  args = new Hashtable <>();
        	args.put("id", id.toString());
        	if(rest==null || cmbox_zip.getSelectionModel().getSelectedItem()!=rest.getZip().toString()) {
        		args.put("zip", cmbox_zip.getValue().split(",")[0].split(" ")[1]);
        	}
        	if(rest==null || spin_cap.getValue()!=rest.getCapacity()) {
        		args.put("capacity", spin_cap.getValue().toString());
        	}
        	ObservableList<String> meals = list_meal.getItems();
        	String menu="";
        	for(int i =0; i<meals.size(); i++) {
        			menu+=meals.get(i).split(":")[0]+",";
        	}
        	args.put("menu", menu);
        	ObservableList<String> emps = list_emp.getItems();
        	String staff="";
        	for(int i =0; i<emps.size(); i++) {
        			staff+=emps.get(i).split(",")[0]+",";
        	}
        	args.put("staff", staff);
        	System.out.println(args);
        	
        	RestaurantRemote.updateRestaurant(args);
	        openWindow("restSearchWindow.fxml",e);
			
        });
        
        btn_add_meal_to_order.setOnMouseClicked(e ->{
        	if(cmb_order.getSelectionModel().getSelectedItem()!=null) {
//        		System.out.println(cmb_meal.getSelectionModel().getSelectedItem().split(":")[0]);
        		ObservableList<String> orders = list_orders.getItems();
        		orders.add(cmb_order.getSelectionModel().getSelectedItem());
        		list_orders.setItems(orders);
        	}   
        });
        
        btn_del_meal_to_order.setOnMouseClicked(e ->{
        	if(cmb_order.getSelectionModel().getSelectedItem()!=null) {
//        		System.out.println(cmb_meal.getSelectionModel().getSelectedItem().split(":")[0]);
        		ObservableList<String> orders = list_orders.getItems();
        		orders.remove(cmb_order.getSelectionModel().getSelectedItem());
        		list_orders.setItems(orders);
        	}   
        });
        
        btn_confirm_order.setOnMouseClicked(e ->{
        	if(rest!=null) {
//        		System.out.println(cmb_meal.getSelectionModel().getSelectedItem().split(":")[0]);
        		Dictionary <String, String>  args = new Hashtable <>();
        		ObservableList<String> orders = list_orders.getItems();
            	String menu="";
            	for(int i =0; i<orders.size(); i++) {
            			menu+=orders.get(i).split(":")[0]+",";
            	}
            	args.put("id", id.toString());
            	args.put("menu", menu);
            	RestaurantRemote.addCheque(args);
        		orders.clear();
        		list_orders.setItems(orders);
        	}   
        });
        
        btn_add_meal.setOnMouseClicked(e ->{
        	if(cmb_meal.getSelectionModel().getSelectedItem()!=null) {
//        		System.out.println(cmb_meal.getSelectionModel().getSelectedItem().split(":")[0]);
        		ObservableList<String> meals = list_meal.getItems();
        		meals.remove(cmb_meal.getSelectionModel().getSelectedItem());
        		meals.add(cmb_meal.getSelectionModel().getSelectedItem());
        		list_meal.setItems(meals);
        		cmb_order.setItems(meals);
        	}   
        });
        
        btn_del_meal.setOnMouseClicked(e ->{
        	String i = list_meal.getSelectionModel().getSelectedItem();
        	if(i!=null) {
        		ObservableList<String> meals = list_meal.getItems();
        		meals.remove(i);
    			list_meal.setItems(meals);
    			cmb_order.setItems(meals);
        	}
        });
        
        btn_add_emp.setOnMouseClicked(e ->{
        	if(cmb_emp.getSelectionModel().getSelectedItem()!=null) {
//        		System.out.println(cmb_meal.getSelectionModel().getSelectedItem().split(":")[0]);
        		ObservableList<String> emps = list_emp.getItems();
        		emps.remove(cmb_emp.getSelectionModel().getSelectedItem());
        		emps.add(cmb_emp.getSelectionModel().getSelectedItem());
        		list_emp.setItems(emps);
        	}   
        });
        
        btn_del_emp.setOnMouseClicked(e ->{
        	String i = list_emp.getSelectionModel().getSelectedItem();
        	if(i!=null) {
        		ObservableList<String> emps = list_emp.getItems();
        		emps.remove(i);
    			list_emp.setItems(emps);
        	}
        });
        fill();
    }
    
    public void setRest(Dictionary <Integer, Restaurant> dict) {
    	Enumeration<Integer> enam = dict.keys();
        while(enam.hasMoreElements()) {
        	id = enam.nextElement();
        	rest=dict.get(id);
        }
        fill();
    }
    
    private void fill() {
		spin_cap.getValueFactory().setValue(0);
		
		//Zip
		ObservableList<String> zip = FXCollections.observableArrayList();
        try {
			for (Zip p : this.getZIP()) { 		      
				zip.add(p.toString());		
			}
		} catch (MyExeception | NamingException e2) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get zip", e2);
		} 
        cmbox_zip.setItems(zip);
        cmbox_zip.getSelectionModel().select(0);
        
        //Employee
        ObservableList<String> emps = FXCollections.observableArrayList();
		Dictionary<Integer, Employee> la2 = RestaurantRemote.getEmpRest(0);
		Enumeration<Integer> enam = la2.keys();
		enam = la2.keys();
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+", "+la2.get(k).getFirst_name()+ " "+la2.get(k).getLast_name()+", "+la2.get(k).getPosition();
            emps.add(rest);
        }
        cmb_emp.setItems(emps);
        
        //Meals
        ObservableList<String> meals = FXCollections.observableArrayList();
		Dictionary<Integer, Meal> la3 = RestaurantRemote.getMealRest(0);
		
		enam = la3.keys();
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+": "+la3.get(k).getTitle()+ ", "+la3.get(k).getPrice();
            meals.add(rest);
        }
        cmb_meal.setItems(meals);
        
        if(rest!=null) {
        	cmbox_zip.getSelectionModel().select(rest.getZip().toString());
        	spin_cap.getValueFactory().setValue(rest.getCapacity());
        	fillLists();
        }
        
	}
    
    private ArrayList<Zip> getZIP() throws MyExeception, NamingException {
    	ArrayList<Zip> zip = RestaurantRemote.getZip();
        System.out.println(zip);
    	return zip;
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
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open window "+window, ex);
    	}
    }
	
	private void fillLists() {
    	ObservableList<String> emps = FXCollections.observableArrayList();
    	Dictionary<Integer, Employee> la2 = RestaurantRemote.getEmpRest(id);
		
    	Enumeration<Integer> enam = la2.keys();
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+", "+la2.get(k).getFirst_name()+ " "+la2.get(k).getLast_name()+", "+la2.get(k).getPosition();
            emps.add(rest);
        }
        list_emp.setItems(emps);
		
        ObservableList<String> meals = FXCollections.observableArrayList();
        Dictionary<Integer, Meal> la3 = RestaurantRemote.getMealRest(id);
		
		enam = la3.keys();
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+": "+la3.get(k).getTitle()+ ", "+la3.get(k).getPrice();
            meals.add(rest);
        }
        list_meal.setItems(meals);
        cmb_order.setItems(meals);
    }
}
