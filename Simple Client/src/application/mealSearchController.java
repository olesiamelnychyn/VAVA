package application;

import java.net.URL;
import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import objects.Meal;
//import ejb.CalcRemote;
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
    private ComboBox<?> cmbox_rest;

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
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
        ObservableList<Meal> data = FXCollections.observableArrayList();
         
        
        btn_search.setOnMouseClicked(e -> {
        	Dictionary <String, String> args = new Hashtable <String, String> ();
        	args.put("here", "here");
        	
        	try {
        		List<Dictionary<Integer, Meal>> result = doRequest(args);
        		for (int i=0; i<result.size(); i++) { 
        			Enumeration<Meal> enu = result.get(i).elements();
        			data.add(enu.nextElement());
        		}
            } catch (NamingException ex) {
                ex.printStackTrace();
            } catch (MyExeception ex) {
               System.out.print(ex.getMessage());
            }
        	table.setItems(data);
            table.getColumns().add(titleCol);
            table.getColumns().add(priceCol);
            table.getColumns().add(prepCol);
            table.setEditable(true);
        });
    }
    
	private List<Dictionary<Integer, Meal>> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
    
        Context ctx = new InitialContext();
        MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        return process(MealRemote, args);
    }
    
    private List<Dictionary<Integer, Meal>> process(MealRemote mealRemote, Dictionary <String, String> args) throws MyExeception {
//        
    	System.out.print("process");
    	List<Dictionary<Integer, Meal>> la = mealRemote.searchMeal(args);
    	System.out.print("here");
    	System.out.print(la);
    	return la;
    }
}
