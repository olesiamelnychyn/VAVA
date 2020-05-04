package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import objects.Meal;
import ejb.MealRemote;
import ejb.MyExeception;

public class mealSearchController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView <Meal> table;

    @FXML
    private Button btn_search;

    @FXML
    private TextField txt_from;

    @FXML
    private TextField txt_to;

    @FXML
    private ComboBox<String> cmbox_rest;

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

    Dictionary<Integer, Meal> result;
    ObservableList<Meal> data ;
    
    @FXML
    void initialize() {
        assert txt_search != null : "fx:id=\"txt_search\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert txt_from != null : "fx:id=\"txt_from\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert txt_to != null : "fx:id=\"txt_to\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert cmbox_rest != null : "fx:id=\"cmbox_rest\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_stat != null : "fx:id=\"btn_stat\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert tool_tip1 != null : "fx:id=\"tool_tip1\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    
        TableColumn <Meal, String> titleCol = new TableColumn <Meal, String> ("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn <Meal, Double> priceCol = new TableColumn <Meal, Double> ("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn <Meal, LocalTime> prepCol = new TableColumn <Meal, LocalTime> ("Preparation");
        prepCol.setCellValueFactory(new PropertyValueFactory<>("prep_time"));
        data = FXCollections.observableArrayList();
        table.getColumns().add(titleCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(prepCol);
        table.setEditable(true);
       
        
    	//TODO get list of restaurants for the combo box
        ObservableList<String> rests = FXCollections.observableArrayList();
        rests.add("Choose restaurant");
        for (int i =1; i< 10; i++) {
    		rests.add(String.valueOf(i));
    	}
    	cmbox_rest.setItems(rests);
    	cmbox_rest.getSelectionModel().select(0);
    	txt_from.setText("0.0");
    	txt_to.setText("50.0");
    	search();
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {openWindow("mealWindow.fxml", e);});
    	
    	btn_export.setOnMouseClicked(e -> {
    		//TODO
    	});
    	btn_stat.setOnMouseClicked(e -> {
    		//TODO open window where charts are shown and pass suitable data
    	});
    	
    	btn_help.setOnMouseClicked(e->{
    		//TODO open window with info about the meals and the work with them
    	});
    	
    	btn_delete.setOnMouseClicked(e->{
    		
    		
    		ObservableList <Meal> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Meal meal_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(meal_del)) {
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
    
	private Dictionary<Integer, Meal> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
    
        Context ctx = new InitialContext();
        MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
    	Dictionary<Integer, Meal> la = MealRemote.searchMeal(args);
    	return la;
    }
    
	private void search() {
		if(result!=null) {
			((Hashtable<Integer, Meal>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
    	args.put("title", txt_search.getText());
		if(cmbox_rest.getValue()!="Choose restaurant") {
			args.put("rest_id", cmbox_rest.getValue());
		} else {
			args.put("rest_id", "");
		}
    	try {
    		args.put("price_from", Double.valueOf(txt_from.getText()).toString());
    		txt_from.setText(Double.valueOf(txt_from.getText()).toString());
    	} catch (NumberFormatException ex) {
    		txt_from.setText("0.0");
    		args.put("price_from", String.valueOf(0.0));
    	}
    	try {
    		args.put("price_to", Double.valueOf(txt_to.getText()).toString());
    		txt_to.setText(Double.valueOf(txt_to.getText()).toString());
    	} catch (NumberFormatException ex) {
    		txt_to.setText("50.0");
    		args.put("price_to", String.valueOf(50.0));
    	}
    	
//    System.out.println(args);
//      DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
       
    	try {
    		final Dictionary<Integer, Meal> result1 = doRequest(args);
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
	
    private void delete(Integer id) throws MyExeception, NamingException {
//        
    	Context ctx = new InitialContext();
        MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        System.out.print("process");
        MealRemote.deleteMeal(id);
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
