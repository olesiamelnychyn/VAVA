package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ejb.LogTest;
import ejb.ReservationRemote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import objects.Employee;
import objects.Meal;
import objects.Reservation;
import objects.Restaurant;

public class reservController {

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
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;

    @FXML
    private Spinner<Integer> spin_vis;

    @FXML
    private ComboBox<String> cmbox_rest;

    @FXML
    private Button btn_add_meal;

    @FXML
    private Button btn_del_meal;

    @FXML
    private ListView<String> list_emp;

    @FXML
    private ListView<String> list_meal;

    @FXML
    private DatePicker date_start;

    @FXML
    private DatePicker date_end;

    @FXML
    private ComboBox<String> cmb_meal;

    @FXML
    private ComboBox<String> cmb_emp;

    @FXML
    private Button btn_add_emp;

    @FXML
    private Button btn_del_emp;

    @FXML
    private Spinner<LocalTime> spin_time_from;

    @FXML
    private Spinner<LocalTime> spin_time_to;

    Reservation reservation=null;
    String rest_res="";
    Integer id=0;
    Context ctx;
	ReservationRemote ReservRemote;
    
    @FXML
    void initialize() {
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert spin_vis != null : "fx:id=\"spin_price\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert cmbox_rest != null : "fx:id=\"cmbox_rest\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_add_meal != null : "fx:id=\"btn_add_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_del_meal != null : "fx:id=\"btn_del_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert list_emp != null : "fx:id=\"list_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert list_meal != null : "fx:id=\"list_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert date_start != null : "fx:id=\"date_start\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert date_end != null : "fx:id=\"date_end\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert cmb_meal != null : "fx:id=\"cmb_meal\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert cmb_emp != null : "fx:id=\"cmb_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_add_emp != null : "fx:id=\"btn_add_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert btn_del_emp != null : "fx:id=\"btn_del_emp\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert spin_time_from != null : "fx:id=\"spin_time_from\" was not injected: check your FXML file 'reservWindow.fxml'.";
        assert spin_time_to != null : "fx:id=\"spin_time_to\" was not injected: check your FXML file 'reservWindow.fxml'.";

        
        try {
			ctx = new InitialContext();
			ReservRemote=  (ReservationRemote) ctx.lookup("ejb:/SimpleEJB2//ReservSessionEJB!ejb.ReservationRemote"); 
			
		} catch (NamingException e2) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to connect to EmployeeRemote", e2);
		}
        
        btn_help.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
        
        btn_back.setOnMouseClicked(e ->{openWindow("reservSearchWindow.fxml",e);});
        
        btn_save.setOnMouseClicked(e ->{
        	DateTimeFormatter formatte1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	DateTimeFormatter formatte2 = DateTimeFormatter.ofPattern("HH:mm:ss");
        	Dictionary <String, String> args = new Hashtable<String, String> ();
        	args.put("id", id.toString());
        	
        	try {
        		if(reservation==null || spin_vis.getValueFactory().getValue()!=reservation.getVisitors()) {
        			args.put("visitors", spin_vis.getValueFactory().getValue().toString());
        		}
        	} catch (NumberFormatException ex) {
        		if(reservation!=null) {
        			spin_vis.getValueFactory().setValue(reservation.getVisitors());
        		}else {
        			spin_vis.getValueFactory().setValue(0);
        		}
        		args.put("visitors", spin_vis.getValueFactory().getValue().toString());
        			
        	}
        	
        	try {
        		if(reservation==null || !date_start.getValue().toString().equals(LocalDate.parse(reservation.getDate_start().toString().split("T")[0],  formatte1).toString()) ||
            			!spin_time_from.getValueFactory().getValue().toString().equals(LocalTime.parse(reservation.getDate_start().toString().split("T")[1], formatte2).toString())) {
            		args.put("date_start", "\""+date_start.getValue().toString()+" "+spin_time_from.getValueFactory().getValue()+"\"");
            	}
        	} catch (NumberFormatException ex) {
        		if(reservation!=null) {
        			date_start.setValue(LocalDate.parse(reservation.getDate_start().toString().split("T")[0],  formatte1));
        			spin_time_from.getValueFactory().setValue(LocalTime.parse(reservation.getDate_start().toString().split("T")[1], formatte2));
        		} else {
        			date_start.setValue(LocalDate.parse("2019-01-01",  formatte1));
            		spin_time_from.getValueFactory().setValue(LocalTime.parse("00:00:00", formatte2));
            	}
        		args.put("date_start", "\""+date_start.getValue().toString()+" "+spin_time_from.getValueFactory().getValue()+"\"");
        	}
        	
        	try {
        		if(reservation==null || !date_end.getValue().toString().equals(LocalDate.parse(reservation.getDate_end().toString().split("T")[0],  formatte1).toString()) ||
        				!spin_time_to.getValueFactory().getValue().toString().equals(LocalTime.parse(reservation.getDate_end().toString().split("T")[1], formatte2).toString())) {
        			args.put("date_end", "\""+date_end.getValue()+" "+spin_time_to.getValueFactory().getValue()+"\"");
        		}
        	}catch(NumberFormatException ex) {
        		if(reservation!=null) {
        			date_end.setValue(LocalDate.parse(reservation.getDate_end().toString().split("T")[0],  formatte1));
            		spin_time_to.getValueFactory().setValue(LocalTime.parse(reservation.getDate_end().toString().split("T")[1], formatte2));
        		}else{
        			date_end.setValue(LocalDate.parse("2021-12-31",  formatte1));
        			spin_time_to.getValueFactory().setValue(LocalTime.parse("00:00:00", formatte2));
        		}
        		args.put("date_end", "\""+date_end.getValue()+" "+spin_time_to.getValueFactory().getValue()+"\"");
        	}
        	
        	if(reservation==null || cmbox_rest.getSelectionModel().getSelectedItem()!=rest_res) {
        		args.put("rest_id", cmbox_rest.getSelectionModel().getSelectedItem().split(",")[0]);
        	} else{
        		cmbox_rest.getSelectionModel().select(0);
        		args.put("rest_id", cmbox_rest.getSelectionModel().getSelectedItem().split(",")[0]);
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
        	ReservRemote.updateReserv(args);
        	openWindow("reservSearchWindow.fxml",e);
        });
        
        btn_delete.setOnMouseClicked(e ->{
        	ReservRemote.deleteReserv(id);
        	openWindow("reservSearchWindow.fxml",e);
        });
        
        btn_undo.setOnMouseClicked(e ->{
        	fill();
        });
        
        btn_add_meal.setOnMouseClicked(e ->{
        	if(cmb_meal.getSelectionModel().getSelectedItem()!=null) {
//        		System.out.println(cmb_meal.getSelectionModel().getSelectedItem().split(":")[0]);
        		ObservableList<String> meals = list_meal.getItems();
        		meals.remove(cmb_meal.getSelectionModel().getSelectedItem());
        		meals.add(cmb_meal.getSelectionModel().getSelectedItem());
        		list_meal.setItems(meals);
        	}   
        });
        
        btn_del_meal.setOnMouseClicked(e ->{
        	String i = list_meal.getSelectionModel().getSelectedItem();
        	if(i!=null) {
        		ObservableList<String> meals = list_meal.getItems();
        		meals.remove(i);
    			list_meal.setItems(meals);
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
            
    }
    
    public void setReserv(Dictionary <Integer, Reservation> dict) {
    	Enumeration<Integer> enam = dict.keys();
        while(enam.hasMoreElements()) {
        	id = enam.nextElement();
        	reservation=dict.get(id);
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
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window , ex);
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
       
        SpinnerValueFactory<LocalTime> value1= new SpinnerValueFactory<LocalTime>() {
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
        spin_time_from.setValueFactory(value);
        spin_time_from.setEditable(true);
        spin_time_to.setValueFactory(value1);
        spin_time_to.setEditable(true);
        
    	try {
    		ObservableList<String> rests = FXCollections.observableArrayList();
			Dictionary<Integer, Restaurant> la = ReservRemote.getRestReserv(0);
			
			Enumeration<Integer> enam = la.keys();
			
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            
	            String rest = k+", cap: "+la.get(k).getCapacity()+ ","+la.get(k).getZip().getState();
	            if (reservation!=null && k==reservation.getRest_id()) {
	            	rest_res=rest;
	            }
	            rests.add(rest);
	        }
	        cmbox_rest.setItems(rests);
	        if(rest_res!="")
	        	cmbox_rest.getSelectionModel().select(rest_res);
	        else {
	        	cmbox_rest.getSelectionModel().select(0);
	        }
	        
	        ObservableList<String> emps = FXCollections.observableArrayList();
			Dictionary<Integer, Employee> la2 = ReservRemote.getEmpReserv(0);
			
			enam = la2.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            String rest = k+", "+la2.get(k).getFirst_name()+ " "+la2.get(k).getLast_name()+", "+la2.get(k).getPosition();
	            emps.add(rest);
	        }
	        cmb_emp.setItems(emps);
	        
	        ObservableList<String> meals = FXCollections.observableArrayList();
			Dictionary<Integer, Meal> la3 = ReservRemote.getMealReserv(0);
			
			enam = la3.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            String rest = k+": "+la3.get(k).getTitle()+ ", "+la3.get(k).getPrice();
	            meals.add(rest);
	        }
	        cmb_meal.setItems(meals);
	        
	
    	DateTimeFormatter formatte1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	DateTimeFormatter formatte2 = DateTimeFormatter.ofPattern("HH:mm:ss");
    	if(id!=0) {
    		spin_vis.getValueFactory().setValue(reservation.getVisitors());
    		date_start.setValue(LocalDate.parse(reservation.getDate_start().toString().split("T")[0],  formatte1));
    		date_end.setValue(LocalDate.parse(reservation.getDate_end().toString().split("T")[0],  formatte1));
//    		System.out.println(LocalTime.parse(reservation.getDate_start().toString().split("T")[1], formatte2)+ " "+LocalTime.parse(reservation.getDate_end().toString().split("T")[1], formatte2));
    		spin_time_from.getValueFactory().setValue(LocalTime.parse(reservation.getDate_start().toString().split("T")[1], formatte2));
    		spin_time_to.getValueFactory().setValue(LocalTime.parse(reservation.getDate_end().toString().split("T")[1], formatte2));
    		
    		fillLists();
    	}else {
    		date_start.setValue(LocalDate.parse("2019-01-01",  formatte1));
    		date_end.setValue(LocalDate.parse("2021-12-31",  formatte1));
    		spin_time_from.getValueFactory().setValue(LocalTime.parse("00:00:00", formatte2));
    		spin_time_to.getValueFactory().setValue(LocalTime.parse("00:00:00", formatte2));

    	}
    	} catch ( SQLException e) {
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to get restaurants or employees or reservations", e);
		}
    	
    }
    
    private void fillLists() {
    	ObservableList<String> emps = FXCollections.observableArrayList();
    	Dictionary<Integer, Employee> la2 = ReservRemote.getEmpReserv(id);
		
    	Enumeration<Integer> enam = la2.keys();
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+", "+la2.get(k).getFirst_name()+ " "+la2.get(k).getLast_name()+", "+la2.get(k).getPosition();
            emps.add(rest);
        }
        list_emp.setItems(emps);
		
        ObservableList<String> meals = FXCollections.observableArrayList();
        Dictionary<Integer, Meal> la3 = ReservRemote.getMealReserv(id);
		
		enam = la3.keys();
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+": "+la3.get(k).getTitle()+ ", "+la3.get(k).getPrice();
            meals.add(rest);
        }
        list_meal.setItems(meals);
    }
}
