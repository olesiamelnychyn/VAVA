package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.EmployeeRemote;
import ejb.MyExeception;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Employee;
import objects.Reservation;
import objects.Restaurant;

public class empController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txt_fname;

    @FXML
    private ComboBox<String> cmbox_pos;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_undo;

    @FXML
    private Button btn_export;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;

    @FXML
    private TextField txt_lname;

    @FXML
    private Spinner<Double> spin_wage;

    @FXML
    private ComboBox<String> cmbox_rest;

    @FXML
    private DatePicker birthdate;

    @FXML
    private ListView<String> list;

    @FXML
    private ImageView img_view;

    @FXML
    private RadioButton rbtn_male;

    @FXML
    private RadioButton rbtn_female;
    
    @FXML
    private TextField txt_phone;

    @FXML
    private TextField txt_email;

    Employee emp=null;
    Integer id=-1;
    Context ctx;
    EmployeeRemote EmployeeRemote;

    @FXML
    void initialize() {
        assert txt_fname != null : "fx:id=\"txt_fname\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert cmbox_pos != null : "fx:id=\"cmbox_pos\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert txt_lname != null : "fx:id=\"txt_lname\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert spin_wage != null : "fx:id=\"spin_wage\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert cmbox_rest != null : "fx:id=\"cmbox_rest\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert birthdate != null : "fx:id=\"birthdate\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert img_view != null : "fx:id=\"img_view\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert rbtn_male != null : "fx:id=\"rbtn_male\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert rbtn_female != null : "fx:id=\"rbtn_female\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert txt_phone != null : "fx:id=\"txt_phone\" was not injected: check your FXML file 'empWindow.fxml'.";
        assert txt_email != null : "fx:id=\"txt_email\" was not injected: check your FXML file 'empWindow.fxml'.";
        
        try {
			ctx = new InitialContext();
			EmployeeRemote =  (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote"); 
			
		} catch (NamingException e2) {
			e2.printStackTrace();
		}
       

        btn_help.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
        
        btn_back.setOnMouseClicked(e ->{openWindow("empSearchWindow.fxml",e);});
        
        btn_delete.setOnMouseClicked(e ->{
        	if (id!=-1) {
        		EmployeeRemote.deleteEmployee(id);
        	}
	        openWindow("empSearchWindow.fxml",e);
        });
        
        rbtn_male.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) { 
                	rbtn_female.setSelected(false);
                }
            }
        });
        
        rbtn_female.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) { 
                	rbtn_male.setSelected(false);
                }
            }
        });
        
        btn_save.setOnMouseClicked(e ->{
        	Dictionary <String, String>  args = new Hashtable <>();
        	args.put("id", id.toString());
        	if(!cmbox_pos.getValue().equals("Choose position") &&(emp==null || cmbox_rest.getSelectionModel().getSelectedItem().split(",")[0]!=emp.getRest_id().toString())) {
        		args.put("rest_id", cmbox_rest.getSelectionModel().getSelectedItem().split(",")[0]);
        	}
        	if(emp==null || !txt_fname.getText().equals(emp.getFirst_name())) {
        			args.put("first_name", "\""+txt_fname.getText()+"\"");
        	}
        	if(emp==null || !txt_lname.getText().equals(emp.getLast_name())) {
    			args.put("last_name", "\""+txt_lname.getText()+"\"");
        	}
        	if(emp==null || (rbtn_male.isSelected() && emp.getGender()!="M")) {
        		args.put("gender", "\"M\"");
        	}
        	if(emp==null || (rbtn_female.isSelected() && emp.getGender()!="F")) {
        		args.put("gender", "\"F\"");
        	}
        	DateTimeFormatter formatte1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	if(emp==null || !birthdate.getValue().toString().equals(LocalDate.parse(emp.getBirthdate().toString(),  formatte1).toString())){
            	args.put("birthdate", "\""+LocalDate.parse(birthdate.getValue().toString(),  formatte1).toString()+"\"");
            }
        	if(emp==null) {
        		args.put("e_mail", " ");
        		args.put("phone", " ");
        	}
        	if(!cmbox_pos.getValue().equals("Choose position") &&(emp==null || !cmbox_pos.getValue().equals(emp.getPosition()))) {
        		args.put("position", "\""+cmbox_pos.getValue()+"\"");
        	}
  	
        	if(emp==null || spin_wage.getValue()!=emp.getWage()) {
        		args.put("wage", spin_wage.getValue().toString());
        	}
        	
        	EmployeeRemote.updateEmployee(args);
	        openWindow("empSearchWindow.fxml",e);
			
        });
        
        btn_undo.setOnMouseClicked(e ->{
        	fill();
        });
        
        fill();
    }
    
    public void setEmp(Dictionary <Integer, Employee> dict) {
    	Enumeration<Integer> enam = dict.keys();
        while(enam.hasMoreElements()) {
        	id = enam.nextElement();
        	emp=dict.get(id);
        }
        fill();
    }
    
    private void fill() {
    
    	spin_wage.getValueFactory().setValue(0.0);
    	//Restaurant
    	ObservableList<String> rests = FXCollections.observableArrayList();
    	int rest_id = -1;
    	if(emp!=null) {rest_id = emp.getRest_id();}
		Dictionary<Integer, Restaurant> la = EmployeeRemote.getRest(rest_id);
		
		Enumeration<Integer> enam = la.keys();
		
        while(enam.hasMoreElements()) {
            Integer k = enam.nextElement();
            String rest = k+", cap: "+la.get(k).getCapacity()+ ","+la.get(k).getZip().getState();
            rests.add(rest);
        }
        cmbox_rest.setItems(rests);
        if(rest_id!=-1)
        	cmbox_rest.getSelectionModel().select(cmbox_rest.getItems().get(0).toString());
        else {
        	cmbox_rest.getSelectionModel().select(0);
        }
		
        //Position
        ObservableList<String> poss = FXCollections.observableArrayList();
        
        try {
			for (String p : this.getPositions()) { 		      
				poss.add(p);		
			}
		} catch (MyExeception e2) {
			e2.printStackTrace();
		} catch (NamingException e2) {
			e2.printStackTrace();
		}
    	cmbox_pos.setItems(poss);
    	if(emp!=null) {
    		cmbox_pos.getSelectionModel().select(emp.getPosition());
    	}
    	cmbox_pos.getSelectionModel().select(0);
    	

    	
    	//not null
    	if(id!=-1) {
    		txt_fname.setText(emp.getFirst_name());
    		txt_lname.setText(emp.getLast_name());
    		spin_wage.getValueFactory().setValue(emp.getWage());
    		if(emp.getGender()=="M") {
    			rbtn_male.setSelected(true);
    			rbtn_female.setSelected(false);
    		}
    		if(emp.getGender()=="F") {
    			rbtn_female.setSelected(true);
    			rbtn_male.setSelected(false);
    		}
    		DateTimeFormatter formatte1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    		birthdate.setValue(LocalDate.parse(emp.getBirthdate().toString(),  formatte1));
        	//Reservations
        	ObservableList<String> rese = FXCollections.observableArrayList();
    		Dictionary<Integer, Reservation> la2 = EmployeeRemote.getEmpReserv(id);
    		
    		enam = la2.keys();
            while(enam.hasMoreElements()) {
                Integer k = enam.nextElement();
                String rest = k+": "+la2.get(k).getDate_start()+ " - "+la2.get(k).getDate_end()+", "+la2.get(k).getVisitors();
                rese.add(rest);
            }
            list.setItems(rese);
    	}
	}
    
    private ArrayList<String> getPositions() throws MyExeception, NamingException {
    	ArrayList<String> pos = EmployeeRemote.getPositions();
    	return pos;
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
