package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ejb.EmployeeRemote;
import ejb.LogTest;
import ejb.MyExeception;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private Button btn_lang;

    @FXML
    private ResourceBundle rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

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
    private Button btn_export;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;
    
    @FXML
	private Label lb_from;
	    
    @FXML
	private Label lb_to;
	    
    @FXML
    private Label lb_pos;
	    
    @FXML
    private Label lb_wage;
    
    Dictionary<Integer, Employee> result;
    ObservableList<Employee> data ;
    
    @FXML
    void initialize() {
    	assert lb_from != null : "fx:id=\"lb_from\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert lb_to != null : "fx:id=\"lb_to\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert lb_pos != null : "fx:id=\"lb_pos\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert lb_wage != null : "fx:id=\"lb_wage\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'empSearchWindow.fxml'.";
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

        //initialize table
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
        
        //get positions
        ObservableList<String> poss = FXCollections.observableArrayList();
        poss.add("Choose position");
        
        try {
			for (String p : this.getPositions()) { 		      
				poss.add(p);		
			}
		} catch (MyExeception | NamingException e2) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get positions", e2);
		} 
    	cmbox_pos.setItems(poss);
    	cmbox_pos.getSelectionModel().select(0);
    	
    	//wage default
    	txt_from.setText("0.0");
    	int wage;
		try {
			wage = this.getMaxWage();
			txt_to.setText(String.valueOf(wage));
		} catch (MyExeception | NamingException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get max wage", e);
		} 
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("Main", "mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {openWindow("Employee", "empWindow.fxml", e);});
    	
    	btn_export.setOnMouseClicked(e -> { //export table as a pdf
    		try {
            	Document document = new Document();
    			PdfWriter.getInstance(document, new FileOutputStream("employees.pdf"));
    			 document.open();
    		        PdfPTable table1 = new PdfPTable(6);
    		        Font f = new Font();
    		        f.setColor(BaseColor.RED);
    		        f.setStyle(java.awt.Font.BOLD);
    		        
    		        table1.addCell(new Phrase("id", f));
    		        table1.addCell(new Phrase("Restaurant", f));
    		        table1.addCell(new Phrase("First Name", f));
    		        table1.addCell(new Phrase("Last Name", f));
    		        table1.addCell(new Phrase("Position", f));
    		        table1.addCell(new Phrase("Wage", f));
    		        
    		        Enumeration<Integer> enam = result.keys();
    		      
    		        while(enam.hasMoreElements()) {
    		            Integer k = enam.nextElement();
    		            table1.addCell(k.toString());
    			        table1.addCell(result.get(k).getRest_id().toString());
    			        table1.addCell(result.get(k).getFirst_name());
    			        table1.addCell(result.get(k).getLast_name());
    			        table1.addCell(result.get(k).getPosition());
    			        table1.addCell(result.get(k).getWage().toString());
    	    		}
    		    
    		        document.add(table1);
    		        document.close();
    		        
    		        File file = new File("employees.pdf");
    		        
    		        if(!Desktop.isDesktopSupported()){
    		        	LogTest.LOGGER.log(Level.INFO, "Desktop is not supported");
    		            return;
    		        }
    		        
    		        Desktop desktop = Desktop.getDesktop();
    		        if(file.exists()) desktop.open(file);
    		        
    		     
    		} catch (DocumentException | IOException ex) {
    			LogTest.LOGGER.log(Level.SEVERE, "Failed to create document", ex);
    		}
    	});
    	
    	btn_help.setOnMouseClicked(e->{openWindow("Help", "helpWindow.fxml", e);});
    	
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
        
        table.getSelectionModel().setCellSelectionEnabled(true);  // selects cell only, not the whole row
    	table.setOnMouseClicked(new EventHandler<MouseEvent>() {  //after the cell was clicked twice
    	 @Override
    	 public void handle(MouseEvent e) {
    	  if (e.getClickCount() == 2) {
    		ObservableList <Employee> selectedItems = table.getSelectionModel().getSelectedItems();
      		Dictionary <Integer, Employee> transferedData = new Hashtable <Integer, Employee>();
      		
      		try {
      			if(selectedItems.size()>0) {
      				Employee emp=selectedItems.get(0);
      				if(result == null) {
      					System.out.println("dict is null");
      				} else {
      					Enumeration<Integer> enam = result.keys();
      					while(enam.hasMoreElements()) {
      						Integer k = enam.nextElement();
      						if(result.get(k).equals(emp)) {
      							transferedData.put(k, emp);
      							break;
      						}
      					}
      				}
      			}
      			
      			//open employee window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("empWindow.fxml"));
                Parent root = loader.load();
                empController eC = loader.getController();
                eC.setEmp(transferedData);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Employee");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
              } catch (IOException ex) {
            	  LogTest.LOGGER.log(Level.SEVERE, "Failed to open employee", ex);
              }
    	  }
    	 }
    	});
    	
    	btn_lang.setText("en");
        btn_lang.setOnMouseClicked(e ->{ lang();});
        lang();
        search();
    }
    
    private void lang() {
    	//change language
    	if(btn_lang.getText().equals("en")) {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));
    		btn_lang.setText("sk");
    	} else {
    		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("sk"));
    		btn_lang.setText("en");
    	}
    	
    	btn_home.setText(rb.getString("btn.home"));
    	btn_new.setText(rb.getString("btn.new"));
    	btn_delete.setText(rb.getString("btn.del"));
    	btn_help.setText(rb.getString("btn.help"));
    	btn_search.setText(rb.getString("btn.search"));

    	table.getColumns().get(0).setText(rb.getString("rest"));
    	table.getColumns().get(1).setText(rb.getString("emp.first_name"));
    	table.getColumns().get(2).setText(rb.getString("emp.last_name"));
    	table.getColumns().get(3).setText(rb.getString("emp.pos"));
    	table.getColumns().get(4).setText(rb.getString("emp.wage"));
    	
    	lb_from.setText(rb.getString("filt.from"));
    	lb_to.setText(rb.getString("filt.to"));
    	lb_wage.setText(rb.getString("emp.wage"));
    	txt_search.setPromptText(rb.getString("emp.prompt"));
    	lb_pos.setText(rb.getString("emp.pos"));
//    	ObservableList <String> selectedItems = cmbox_pos.getItems();
//    	selectedItems.set(0, rb.getString("emp.choose_pos"));
    	cmbox_pos.getItems().set(0, rb.getString("emp.choose_pos"));;
    	cmbox_pos.getSelectionModel().select(0);
	
    }
    
    private Dictionary<Integer, Employee> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
        Context ctx = new InitialContext();
        EmployeeRemote EmployeeRemote = (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote");    
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
    	
    	//wage from
    	try {
    		args.put("wage_from", Double.valueOf(txt_from.getText()).toString());
    		txt_from.setText(Double.valueOf(txt_from.getText()).toString());
    	} catch (NumberFormatException ex) {
    		txt_from.setText("0.0");
    		args.put("wage_from", String.valueOf(0.0));
    	}

    	//wage to
    	try {
    		args.put("wage_to", Double.valueOf(txt_to.getText()).toString());
    		txt_to.setText(Double.valueOf(txt_to.getText()).toString());
    	} catch (NumberFormatException ex) {
    		int wage;
			try {
				wage = this.getMaxWage();
				txt_to.setText(String.valueOf(wage));
				args.put("wage_to", String.valueOf(wage));
			} catch (MyExeception e) {
				e.printStackTrace();
				args.put("wage_to", String.valueOf(50.0));
			} catch (NamingException e) {
				e.printStackTrace();
				args.put("wage_to", String.valueOf(50.0));
			}
    	}
    	
    	//position
    	if(!cmbox_pos.getValue().equals(rb.getString("emp.choose_pos"))) {
    		args.put("position", cmbox_pos.getValue());
    	}else {
    		args.put("position", "");
    	}
       
    	//get result
    	try {
    		final Dictionary<Integer, Employee> result1 = doRequest(args);
    		result=result1;
    		
    		Enumeration<Integer> enam = result.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            data.add(result.get(k));
    		}
        } catch (NamingException | MyExeception ex) {
        	LogTest.LOGGER.log(Level.SEVERE, "Failed to get employees", ex);
        }
    	table.setItems(data);
    }
    
    private int getMaxWage() throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
        EmployeeRemote EmployeeRemote = (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote");    
        int wage = EmployeeRemote.getMaxWage();
    	return wage;
    }
    
    private ArrayList<String> getPositions() throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
        EmployeeRemote EmployeeRemote = (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote");    
        ArrayList<String> pos = EmployeeRemote.getPositions();
    	return pos;
    }
    
    private void delete(Integer id) throws MyExeception, NamingException {
      Context ctx = new InitialContext();
      EmployeeRemote EmployeeRemote = (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote");    
      EmployeeRemote.deleteEmployee(id);
    }
    
    private void openWindow(String title, String window, MouseEvent e) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource(window));
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        stage.setTitle(title);
	        stage.setScene(scene);
	        stage.show();
	        ((Node)(e.getSource())).getScene().getWindow().hide(); 
	        
    	} catch (IOException ex) {
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window, e);
    	}
    }
}
