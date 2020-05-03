package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ejb.MyExeception;
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
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Reservation;

public class reservSearchController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Reservation> table;

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

    @FXML
    private DatePicker date_from;

    @FXML
    private DatePicker date_to;
    
    List<Dictionary<Integer, Reservation>> result;
    ObservableList<Reservation> data ;
    
    @FXML
    void initialize() {
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert txt_from != null : "fx:id=\"txt_from\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert txt_to != null : "fx:id=\"txt_to\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert cmbox_rest != null : "fx:id=\"cmbox_rest\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert btn_stat != null : "fx:id=\"btn_stat\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert tool_tip1 != null : "fx:id=\"tool_tip1\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert date_from != null : "fx:id=\"date_from\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        assert date_to != null : "fx:id=\"date_to\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
        
        TableColumn <Reservation, Integer> restCol = new TableColumn <Reservation, Integer> ("Restaurant");
        restCol.setCellValueFactory(new PropertyValueFactory<>("rest_id"));
        TableColumn <Reservation, LocalDate> fromCol = new TableColumn <Reservation, LocalDate> ("Date From");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("date_start"));
        TableColumn <Reservation, LocalDate> toCol = new TableColumn <Reservation, LocalDate> ("Date To");
        toCol.setCellValueFactory(new PropertyValueFactory<>("date_to"));
        TableColumn <Reservation, Integer> visCol = new TableColumn <Reservation, Integer> ("Date To");
        visCol.setCellValueFactory(new PropertyValueFactory<>("date_to"));
        data = FXCollections.observableArrayList();
        table.getColumns().add(restCol);
        table.getColumns().add(fromCol);
        table.getColumns().add(toCol);
        table.getColumns().add(visCol);
        table.setEditable(true);
        
        ObservableList<String> rests = FXCollections.observableArrayList();
        rests.add("Choose restaurant");
        for (int i =1; i< 10; i++) {
    		rests.add(String.valueOf(i));
    	}
        cmbox_rest.setItems(rests);
    	cmbox_rest.getSelectionModel().select(0);
    	txt_from.setText("2018-01-01 0:00:00");
    	txt_from.setText("2021-12-31 23:59:59");
    	
        btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {openWindow("reservWindow.fxml", e);});
    	
    	btn_export.setOnMouseClicked(e -> {
    		//TODO
    	});
    	btn_stat.setOnMouseClicked(e -> {
    		//TODO open window where charts are shown and pass suitable data
    	});
    	
    	btn_help.setOnMouseClicked(e->{
    		//TODO open window with info about the Reservations and the work with them
    	});
    	
btn_delete.setOnMouseClicked(e->{
    		
    		
    		ObservableList <Reservation> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Reservation Reservation_del : selectedItems) {
    		int break1=0;
			for (Dictionary<Integer, Reservation> dict : result) {
				if(dict == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = dict.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(dict.get(k).equals(Reservation_del)) {
	    	            	
	    	            	try {
	    	            		System.out.println("Gonna delete this one"+k);
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
    		}
    		search();
    	});
    	
	btn_search.setOnMouseClicked(e ->{search();});
    }
    
//    private List<Dictionary<Integer,Reservation>> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
//        
//        Context ctx = new InitialContext();
//        ReservationRemote reservationRemote = (ReservationRemote) ctx.lookup("ejb:/SimpleEJB2//ReservSessionEJB!ejb.ReservationRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
//    	List<Dictionary<Integer, Reservation>> la = reservationRemote.searchReserv(args);
//    	return la;
//    }
    
    private void search() {
		if(result!=null) {
			result.clear();
			data.clear();
		}
		
    }
    
    private void delete(Integer id) throws MyExeception, NamingException {
//      
  	Context ctx = new InitialContext();
      ReservationRemote ReservationRemote = (ReservationRemote) ctx.lookup("ejb:/SimpleEJB2//ReservSessionEJB!ejb.ReservationRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
      System.out.print("process");
      ReservationRemote.deleteReserv(id);
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
