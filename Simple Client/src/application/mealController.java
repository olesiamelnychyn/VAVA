package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ejb.MealRemote;
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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import objects.Meal;
import objects.Product;
import objects.Reservation;
import objects.Restaurant;

public class mealController {

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
    private ListView<String>list_prod;

    @FXML
    private ImageView img_view;

    @FXML
    private Spinner<LocalTime> spint_time;

    @FXML
    private ComboBox<String> cmbox_prod;

    @FXML
    private Button btn_add_prod;

    @FXML
    private Button btn_del_prod;

    @FXML
    private ListView<String> list_rest;

    @FXML
    private ListView<String> list_reserv;
    
    Meal meal=null;
    Integer id=0;
    Context ctx;
	MealRemote MealRemote;
    
    @FXML
    void initialize() {    
        assert txt_title != null : "fx:id=\"txt_title\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert spin_price != null : "fx:id=\"spin_price\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert list_prod != null : "fx:id=\"list_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert img_view != null : "fx:id=\"img_view\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert spint_time != null : "fx:id=\"spint_time\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert cmbox_prod != null : "fx:id=\"cmbox_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_add_prod != null : "fx:id=\"btn_add_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert btn_del_prod != null : "fx:id=\"btn_del_prod\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert list_rest != null : "fx:id=\"list_rest\" was not injected: check your FXML file 'mealWindow.fxml'.";
        assert list_reserv != null : "fx:id=\"list_reserv\" was not injected: check your FXML file 'mealWindow.fxml'.";
        
        try {
			ctx = new InitialContext();
			 MealRemote=  (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote"); 
			
		} catch (NamingException e2) {
			e2.printStackTrace();
		}
       
        btn_help.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
        
        btn_back.setOnMouseClicked(e ->{openWindow("mealSearchWindow.fxml",e);});
        
        btn_save.setOnMouseClicked(e ->{
//        	DateTimeFormatter time_p = DateTimeFormatter.ofPattern("H:mm:ss");
        	Dictionary <String, String>  args = new Hashtable <>();
        	args.put("id", id.toString());
        	if(meal==null || !txt_title.getText().equals(meal.getTitle())) {
        			args.put("title", "\""+txt_title.getText()+"\"");
        	}
//        	System.out.println(spint_time.getValue()+" "+meal.getPrep_time());
        	if(meal==null || !((LocalTime)spint_time.getValue()).equals(meal.getPrep_time())) {
        		String time=spint_time.getValue().toString();
        		if(spint_time.getValue().toString().length()<8) {
        			time+=":00";
        		}
        		args.put("prep_time", "\""+time+"\"");
        	}
  	
        	if(meal==null || spin_price.getValue()!=meal.getPrice()) {
        		args.put("price", spin_price.getValue().toString());
        	}
        	ObservableList<String> prods = list_prod.getItems();
        	String ingr="";
        	for(int i =0; i<prods.size(); i++) {
        		ingr+=prods.get(i).split(",")[0]+",";
        	}
        	args.put("ingr", ingr);
        	MealRemote.updateMeal(args);
	        openWindow("mealSearchWindow.fxml",e);
			
        });
        
        btn_delete.setOnMouseClicked(e ->{
			MealRemote.deleteMeal(id);
	        openWindow("mealSearchWindow.fxml",e);
        });
        
        btn_undo.setOnMouseClicked(e ->{
        	fill();
        });
        
        btn_add_prod.setOnMouseClicked(e ->{
        	if(cmbox_prod.getSelectionModel().getSelectedItem()!=null) {
//        		System.out.println(cmb_meal.getSelectionModel().getSelectedItem().split(":")[0]);
        		ObservableList<String> prods= list_prod.getItems();
        		prods.remove(cmbox_prod.getSelectionModel().getSelectedItem());
        		prods.add(cmbox_prod.getSelectionModel().getSelectedItem());
        		list_prod.setItems(prods);
        	}   
        });
        
        btn_del_prod.setOnMouseClicked(e ->{
        	String i = list_prod.getSelectionModel().getSelectedItem();
        	if(i!=null) {
        		ObservableList<String> prods = list_prod.getItems();
        		prods.remove(i);
        		list_prod.setItems(prods);
        	}
        });
        
    }
    
    public void setMeal(Dictionary <Integer, Meal> dict) {
    	//System.out.println("here");
    	Enumeration<Integer> enam = dict.keys();
        while(enam.hasMoreElements()) {
        	id = enam.nextElement();
        	meal=dict.get(id);
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
    
	private void fill() {
    	SpinnerValueFactory<LocalTime> value = new SpinnerValueFactory<LocalTime>() {
            {
            setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm:ss"), DateTimeFormatter.ofPattern("H:mm:ss")));
            }
            @Override
            public void decrement(int steps) {
                if (getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.plusMinutes(steps));
                }
            }
        };
        spint_time.setValueFactory(value);
        spint_time.setEditable(true);
        
        ObservableList<String> prods = FXCollections.observableArrayList();
 	   Dictionary<Integer, Product> la2 = MealRemote.getProdMeal(0);
		
 	  Enumeration<Integer>  enam = la2.keys();
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+", "+la2.get(k).getTitle()+ " "+la2.get(k).getPrice();
            prods.add(rest);
        }
            
        cmbox_prod.setItems(prods);
        
    	if(id!=0) {
    		txt_title.setText(meal.getTitle());
    		spin_price.getValueFactory().setValue(meal.getPrice());
    		spint_time.getValueFactory().setValue(meal.getPrep_time());
    		//TODO get prod
    		ObservableList<String> rests = FXCollections.observableArrayList();
    		
    		try {
    			Dictionary<Integer, Restaurant> la = MealRemote.getRestMeal(id);
    			
    			enam = la.keys();
    	        while(enam.hasMoreElements()) {
    	            Integer k = enam.nextElement();
    	            String rest = k+", cap: "+la.get(k).getCapacity()+ ","+la.get(k).getZip().getState();
   
    	            rests.add(rest);
    	            
        		}
    	        list_rest.getItems().addAll(rests);
			} catch ( SQLException e) {
				e.printStackTrace();
			}
            
    		ObservableList<String> reserv = FXCollections.observableArrayList();
    		Dictionary<Integer, Reservation> la = MealRemote.getReservMeal(id);
    			
    			enam = la.keys();
    	        while(enam.hasMoreElements()) {
    	            Integer k = enam.nextElement();
    	            String res = k+", rest: "+la.get(k).getRest_id()+ ", "+la.get(k).getDate_start()+"-"+la.get(k).getDate_end();
    	            //System.out.println(rest);
    	            reserv.add(res);
    	            
        		}
    	        //System.out.println(rests);
    	        list_reserv.getItems().addAll(reserv);
			
    	   prods = FXCollections.observableArrayList();
    	   la2 = MealRemote.getProdMeal(id);
    			
    	   enam = la2.keys();
    	   while(enam.hasMoreElements()) {
    	        Integer k = enam.nextElement();
    	        String rest = k+", "+la2.get(k).getTitle()+ " "+la2.get(k).getPrice();
    	            prods.add(rest);
    	        }
    	   list_prod.setItems(prods);
  
    			
    	} else {
    		DateTimeFormatter time_p = DateTimeFormatter.ofPattern("H:mm:ss");
    		spin_price.getValueFactory().setValue(0.00);
    		spint_time.getValueFactory().setValue(LocalTime.parse("0:00:00", time_p));
    	}
    	
    }
    
}
