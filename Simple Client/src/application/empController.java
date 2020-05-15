package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.EmployeeRemote;
import ejb.LogTest;
import ejb.MyExeception;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import objects.Employee;
import objects.Reservation;
import objects.Restaurant;


public class empController {
	
	@FXML
    private Button btn_lang;

	@FXML
	private ResourceBundle rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

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
	
	@FXML
	private Label lb_fname;
	    
    @FXML
	private Label lb_lname;
	    
    @FXML
    private Label lb_pos;
	    
    @FXML
    private Label lb_rest;
	    
    @FXML
    private Label lb_wage;
	    
    @FXML
    private Label lb_birth;
    
    @FXML
    private Label lb_reserv;
	    
    @FXML
    private Label lb_email;
	    
    @FXML
    private Label lb_phone;

	Employee emp=null;
	Integer id=-1;
	Context ctx;
	EmployeeRemote EmployeeRemote;
	
	@FXML
	void initialize() {
		assert lb_rest != null : "fx:id=\"lb_rest\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_pos != null : "fx:id=\"lb_pos\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_lname != null : "fx:id=\"lb_lname\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_fname != null : "fx:id=\"lb_fname\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_phone != null : "fx:id=\"lb_phone\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_email != null : "fx:id=\"lb_email\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_reserv != null : "fx:id=\"lb_reserv\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_birth != null : "fx:id=\"lb_birth\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert lb_wage != null : "fx:id=\"lb_wage\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert txt_fname != null : "fx:id=\"txt_fname\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert cmbox_pos != null : "fx:id=\"cmbox_pos\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert btn_back != null : "fx:id=\"btn_back\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert btn_save != null : "fx:id=\"btn_save\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'empWindow.fxml'.";
		assert btn_undo != null : "fx:id=\"btn_undo\" was not injected: check your FXML file 'empWindow.fxml'.";
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
			LogTest.LOGGER.log(Level.SEVERE, "Failed to connect to EmployeeRemote", e2);
		}
        
        img_view.setOnMouseClicked(e ->{
        	if(e.getClickCount()==2) {
                
            	FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg");
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.getExtensionFilters().add(imageFilter);
            	Stage stage = new Stage();
    	        stage.setTitle("New Window");
            	File file = fileChooser.showOpenDialog(stage);
            	
				try {
					RandomAccessFile f = new RandomAccessFile(file.getAbsolutePath(), "r");
					byte[] b = new byte[(int)f.length()];
	            	f.readFully(b);
	            	ByteArrayInputStream bis = new ByteArrayInputStream(b);
	    	        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
	    	 
	    	        ImageReader reader = (ImageReader) readers.next();
	    	        Object source = bis; 
	    	        ImageInputStream iis = ImageIO.createImageInputStream(source); 
	    	        reader.setInput(iis, true);
	    	        ImageReadParam param = reader.getDefaultReadParam();
	    	 
	    	        BufferedImage image = reader.read(0, param);
	    			Image n =  SwingFXUtils.toFXImage(image, null );
	    			
	    			img_view.setImage(n);
	    			
	    			EmployeeRemote.setImage(id, b);
				} catch ( IOException e1) {
					LogTest.LOGGER.log(Level.SEVERE, "Failed to get image", e1);
				}
            	
            }
        });
        
        btn_lang.setText("en");
        btn_lang.setOnMouseClicked(e ->{ lang();});
        lang();       

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
        	if(emp==null || !txt_phone.getText().equals(emp.getPhone())) {
    			args.put("phone", "\""+txt_phone.getText()+"\"");
        	}
        	if(emp==null || !txt_email.getText().equals(emp.getE_mail())) {
    			args.put("e_mail", "\""+txt_email.getText()+"\"");
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
	
	private void lang() {
    	
    	if(btn_lang.getText().equals("en")) {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));
    		btn_lang.setText("sk");
    	} else {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("sk"));
    		btn_lang.setText("en");
    	}
    	
    	btn_back.setText(rb.getString("btn.back"));
    	btn_save.setText(rb.getString("btn.save"));
    	btn_delete.setText(rb.getString("btn.del"));
    	btn_undo.setText(rb.getString("btn.undo"));
    	btn_help.setText(rb.getString("btn.help"));
    	rbtn_male.setText(rb.getString("emp.m"));
    	rbtn_female.setText(rb.getString("emp.f"));
    	
    	lb_fname.setText(rb.getString("emp.first_name"));
    	lb_lname.setText(rb.getString("emp.last_name"));
    	lb_wage.setText(rb.getString("emp.wage"));
    	lb_reserv.setText(rb.getString("reservs"));
    	lb_rest.setText(rb.getString("rest"));
    	lb_phone.setText(rb.getString("supp.phone"));
    	lb_pos.setText(rb.getString("emp.pos"));
    	lb_birth.setText(rb.getString("emp.birthdate"));
	
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
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get positions", e2);
		} catch (NamingException e2) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get positions", e2);
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
    		txt_phone.setText(emp.getPhone());
    		txt_email.setText(emp.getE_mail());
    		spin_wage.getValueFactory().setValue(emp.getWage());
    		if(emp.getGender().equals("M")) {
    			rbtn_male.setSelected(true);
    			rbtn_female.setSelected(false);
    		}
    		if(emp.getGender().equals("F")) {
    			rbtn_female.setSelected(true);
    			rbtn_male.setSelected(false);
    		}
    		   		
			try {
				byte [] data = EmployeeRemote.getImage(id);
				if(data!=null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(data);
		        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
		 
		        ImageReader reader = (ImageReader) readers.next();
		        Object source = bis; 
		        ImageInputStream iis;
				iis = ImageIO.createImageInputStream(source);
				reader.setInput(iis, true);
		        ImageReadParam param = reader.getDefaultReadParam();
		 
		        BufferedImage image = reader.read(0, param);
				Image n =  SwingFXUtils.toFXImage(image, null );
				img_view.setImage(n);
				}
			} catch (IOException e) {
				LogTest.LOGGER.log(Level.SEVERE, "Failed to get image from database", e);
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
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window , ex);
    	}
    }
}
