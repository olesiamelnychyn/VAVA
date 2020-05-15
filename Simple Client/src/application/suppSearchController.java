package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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

import ejb.LogTest;
import ejb.MyExeception;
import ejb.SupplierRemote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import  javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Supplier;

public class suppSearchController {

	@FXML
    private Button btn_lang;
	
    @FXML
    private ResourceBundle  rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

    @FXML
    private URL location;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView<Supplier> table;

    @FXML
    private Button btn_search;

    @FXML
    private TextField txt_title;

    @FXML
    private TextField txt_email;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_export;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;

    @FXML
    private TextField txt_phone;

    @FXML
    private Button btn_new;
    
    @FXML
    private Label lb_price;
    
    @FXML
    private Label lb_title;
    
    @FXML
    private Label lb_phone;
    
    @FXML
    private Label lb_email;
    
    Dictionary<Integer, Supplier> result;
    ObservableList<Supplier> data ;

    @FXML
    void initialize() {
    	assert lb_price != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
    	assert lb_title != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
    	assert lb_phone != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
    	assert lb_email != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert txt_search != null : "fx:id=\"txt_search\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert txt_title != null : "fx:id=\"txt_title\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert txt_email != null : "fx:id=\"txt_email\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert txt_phone != null : "fx:id=\"txt_phone\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'suppSearchWindow.fxml'.";
        
        
        TableColumn <Supplier, Integer> titleCol = new TableColumn <Supplier, Integer> ("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn <Supplier, String> phoneCol = new TableColumn <Supplier, String> ("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn <Supplier, String> emCol = new TableColumn <Supplier, String> ("E-mail");
        emCol.setCellValueFactory(new PropertyValueFactory<>("e_mail"));
        data = FXCollections.observableArrayList();
        table.getColumns().add(titleCol);
        table.getColumns().add(phoneCol);
        table.getColumns().add(emCol);
        table.setEditable(true);
        search();
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {
    		Dictionary <String, String>  args = new Hashtable <>();
    		if(txt_title.getText()!="" && txt_phone.getText()!="" && txt_email.getText()!=""
    				&& txt_title.getText().toString().length()<=30 
    				&& txt_phone.getText().toString().length()<=13
    				&& txt_email.getText().toString().length()<=320) {
    			args.put("title", txt_title.getText());
    			args.put("phone", txt_phone.getText());
    			args.put("e_mail", txt_email.getText());
    			try {
					this.addSupp(args);
				} catch (MyExeception e1) {
					e1.printStackTrace();
				} catch (NamingException e1) {
					e1.printStackTrace();
				}
    		}
    		txt_title.setText("");
    		txt_phone.setText("");
    		txt_email.setText("");
    		search();
    	});
    	
    	btn_export.setOnMouseClicked(e -> {
    		try {
            	Document document = new Document();
    			PdfWriter.getInstance(document, new FileOutputStream("suppliers.pdf"));
    			 document.open();
    		        PdfPTable table1 = new PdfPTable(4);
    		        Font f = new Font();
    		        f.setColor(BaseColor.RED);
    		        f.setStyle(java.awt.Font.BOLD);
    		        
    		        table1.addCell(new Phrase("id", f));
    		        table1.addCell(new Phrase("Title", f));
    		        table1.addCell(new Phrase("Phone", f));
    		        table1.addCell(new Phrase("E-mail",f));
    		        
    		        Enumeration<Integer> enam = result.keys();
    		      
    		        while(enam.hasMoreElements()) {
    		            Integer k = enam.nextElement();
    		            table1.addCell(k.toString());
    			        table1.addCell(result.get(k).getTitle().toString());
    			        table1.addCell(result.get(k).getPhone().toString());
    			        table1.addCell(result.get(k).getE_mail().toString());
    	    		}
    		    
    		        document.add(table1);
    		        document.close();
    		        
    		        File file = new File("suppliers.pdf");
    		        
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
    	
    	btn_help.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
    	
    	btn_delete.setOnMouseClicked(e->{
    		
    		
    		ObservableList <Supplier> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Supplier Supplier_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(Supplier_del)) {
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
        
        btn_lang.setText("en");
        btn_lang.setOnMouseClicked(e ->{ lang();});
        lang();
        
        }
        
        
       private void lang() {
        	
        	if(btn_lang.getText().equals("en")) {
        		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));
        		btn_lang.setText("sk");
        	} else {
        		rb =	ResourceBundle.getBundle("texts", Locale.forLanguageTag("sk"));
        		btn_lang.setText("en");
        	}
        	
        	btn_home.setText(rb.getString("btn.home"));
        	btn_new.setText(rb.getString("supp.add_new"));
        	btn_delete.setText(rb.getString("btn.del"));
        	btn_help.setText(rb.getString("btn.help"));
        	btn_search.setText(rb.getString("btn.search"));
        	table.getColumns().get(0).setText(rb.getString("supp.title"));
        	table.getColumns().get(1).setText(rb.getString("supp.e_mail"));
        	table.getColumns().get(2).setText(rb.getString("supp.phone"));
        	txt_search.setPromptText(rb.getString("prompt"));
        	lb_title.setText(rb.getString("supp.title"));
        	lb_email.setText(rb.getString("supp.e_mail"));
        	lb_phone.setText(rb.getString("supp.phone"));
        	
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
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window, ex);
    	}
		
	}
    private int addSupp(Dictionary <String, String>  args) throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
    	SupplierRemote SupplierRemote = (SupplierRemote) ctx.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote");  
        int id = SupplierRemote.addSupplier(args);
    	return id;
    }
	private Dictionary<Integer, Supplier> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
        
        Context ctx = new InitialContext();
        SupplierRemote SupplierRemote = (SupplierRemote) ctx.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote");    
    	Dictionary<Integer, Supplier> la = SupplierRemote.searchSupplier(args);
    	return la;
    }
    
    private void search() {
		if(result!=null) {
			((Hashtable<Integer, Supplier>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
    	args.put("title", txt_search.getText());
   
    	try {
    		final Dictionary<Integer, Supplier> result1 = doRequest(args);
    		result=result1;
    		
    		Enumeration<Integer> enam = result.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            data.add(result.get(k));
    		}
        } catch (NamingException | MyExeception ex) {
        	LogTest.LOGGER.log(Level.SEVERE, "Failed to get suppliers", ex);
        } 
    	table.setItems(data);
    }
    private void delete(Integer id) throws MyExeception, NamingException {
//      
      Context ctx = new InitialContext();
      SupplierRemote SupplierRemote = (SupplierRemote) ctx.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote");    

      SupplierRemote.deleteSupplier(id);
    }
}
