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

import ejb.EmployeeRemote;
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
import objects.Employee;

public class empSearchController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView<Employee> table;

    @FXML
    private Button btn_search;

    @FXML
    private TextField txt_from;

    @FXML
    private TextField txt_to;

    @FXML
    private ComboBox<String> cmbox_pos;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_new;

    @FXML
    private Button btn_delete;

    @FXML
    private SplitMenuButton btn_export;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;
    
    Dictionary<Integer, Employee> result;
    ObservableList<Employee> data ;
    
    @FXML
    void initialize() {
        assert txt_search != null : "fx:id=\"txt_search\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert txt_from != null : "fx:id=\"txt_from\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert txt_to != null : "fx:id=\"txt_to\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert cmbox_pos != null : "fx:id=\"cmbox_pos\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'empSearchWindow.fxml'.";

        
        TableColumn <Employee, Integer> restCol = new TableColumn <Employee, Integer> ("Restaurant");
        restCol.setCellValueFactory(new PropertyValueFactory<>("rest_id"));
        TableColumn <Employee, String> firstCol = new TableColumn <Employee, String> ("First Name");
        firstCol.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        TableColumn <Employee, String> lastCol = new TableColumn <Employee, String> ("Last Name");
        lastCol.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        TableColumn <Employee, String> posCol = new TableColumn <Employee, String> ("Position");
        posCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        TableColumn <Employee, Double> wageCol = new TableColumn <Employee, Double> ("Wage");
        wageCol.setCellValueFactory(new PropertyValueFactory<>("wage"));
        data = FXCollections.observableArrayList();
        table.getColumns().add(restCol);
        table.getColumns().add(firstCol);
        table.getColumns().add(lastCol);
        table.getColumns().add(posCol);
        table.getColumns().add(wageCol);
        table.setEditable(true);
        
        ObservableList<String> poss = FXCollections.observableArrayList();
        poss.add("Choose position");
        for (int i =1; i< 10; i++) {
    		poss.add(String.valueOf(i));
    	}
    	cmbox_pos.setItems(poss);
    	cmbox_pos.getSelectionModel().select(0);
    	txt_from.setText("0.0");
    	txt_to.setText("5000.0");
    	
    	search();
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {openWindow("empWindow.fxml", e);});
    	
    	btn_export.setOnMouseClicked(e -> {
    		//TODO
    	});
    	
    	btn_help.setOnMouseClicked(e->{
    		//TODO open window with info about the Employees and the work with them
    	});
    	btn_delete.setOnMouseClicked(e->{
    		
    		
    		ObservableList <Employee> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Employee Employee_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(Employee_del)) {
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
    private Dictionary<Integer, Employee> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
        
        Context ctx = new InitialContext();
        EmployeeRemote EmployeeRemote = (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
    	Dictionary<Integer, Employee> la = EmployeeRemote.searchEmployee(args);
    	return la;
    }
    
    private void search() {
		if(result!=null) {
			((Hashtable<Integer, Employee>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
    	args.put("name", txt_search.getText());
    	try {
    		args.put("wage_from", Double.valueOf(txt_from.getText()).toString());
    		txt_from.setText(Double.valueOf(txt_from.getText()).toString());
    	} catch (NumberFormatException ex) {
    		txt_from.setText("0.0");
    		args.put("wage_from", String.valueOf(0.0));
    	}
    	try {
    		args.put("wage_to", Double.valueOf(txt_to.getText()).toString());
    		txt_to.setText(Double.valueOf(txt_to.getText()).toString());
    	} catch (NumberFormatException ex) {
    		txt_to.setText("5000.0");
    		args.put("wage_to", String.valueOf(50.0));
    	}
    	if(cmbox_pos.getValue()!="Choose position") {
    		args.put("position", cmbox_pos.getValue());
    	}else {
    		args.put("position", "");
    	}
    	
//    System.out.println(args);
//      DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
       
    	try {
    		final Dictionary<Integer, Employee> result1 = doRequest(args);
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
      EmployeeRemote EmployeeRemote = (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
      System.out.print("process");
      EmployeeRemote.deleteEmployee(id);
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
