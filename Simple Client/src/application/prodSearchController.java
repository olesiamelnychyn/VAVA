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
import ejb.ProductRemote;
import ejb.SupplierRemote;
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
import objects.Product;
import objects.Supplier;

public class prodSearchController {

	@FXML
    private Button btn_lang;
	
    @FXML
    private ResourceBundle rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

    @FXML
    private URL location;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView<Product> table;

    @FXML
    private Button btn_search;

    @FXML
    private TextField txt_from;

    @FXML
    private TextField txt_to;

    @FXML
    private ComboBox<String> cmbox_supp;

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
	private Label lb_price;
	    
    @FXML
	private Label lb_from;
	    
    @FXML
    private Label lb_to;
	    
    @FXML
    private Label lb_supp;
    
    Dictionary<Integer, Product> result;
    ObservableList<Product> data ;

    @FXML
    void initialize() {
    	assert lb_from != null : "fx:id=\"lb_from\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert lb_to != null : "fx:id=\"lb_to\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert lb_supp != null : "fx:id=\"lb_supp\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert lb_price != null : "fx:id=\"lb_price\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert txt_search != null : "fx:id=\"txt_search\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert txt_from != null : "fx:id=\"txt_from\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert txt_to != null : "fx:id=\"txt_to\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert cmbox_supp != null : "fx:id=\"cmbox_supp\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'prodSearchWindow.fxml'.";
        

        TableColumn <Product, String> titleCol = new TableColumn <Product, String> ("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn <Product, Double> priceCol = new TableColumn <Product, Double> ("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn <Product, String> suppCol = new TableColumn <Product, String> ("Supplier");
        suppCol.setCellValueFactory(new PropertyValueFactory<>("supp_id"));
        
        data = FXCollections.observableArrayList();
        table.getColumns().add(titleCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(suppCol);
        table.setEditable(true);
        
        ObservableList<String> supps = FXCollections.observableArrayList();
        supps.add("Choose supplier");
		
		try {
			Context ctx = new InitialContext();
			SupplierRemote SupplierRemote = (SupplierRemote) ctx.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
			Dictionary <String, String> args = new Hashtable <String, String> ();
	    	args.put("", "");
			Dictionary<Integer, Supplier> la = SupplierRemote.searchSupplier(args);
			
			
			Enumeration<Integer> enam = la.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            String supp = String.valueOf(k)+": "+la.get(k).toString();
	            supps.add(supp);
	            
    		}
	        cmbox_supp.setItems(supps);
		} catch (NamingException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get suppliers", e);
		}
		cmbox_supp.setItems(supps);
		cmbox_supp.getSelectionModel().select(0);
		txt_from.setText("0.0");
		double price;
		try {
			price = this.getMaxPrice();
			txt_to.setText(String.valueOf(price));
		} catch (MyExeception | NamingException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get max price", e);
		}
    	
    	
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {openWindow("prodWindow.fxml", e);});
    	
    	btn_export.setOnMouseClicked(e -> {
    		try {
            	Document document = new Document();
    			PdfWriter.getInstance(document, new FileOutputStream("products.pdf"));
    			 document.open();
    		        PdfPTable table1 = new PdfPTable(4);
    		        Font f = new Font();
    		        f.setColor(BaseColor.RED);
    		        f.setStyle(java.awt.Font.BOLD);
    		        
    		        table1.addCell(new Phrase("id", f));
    		        table1.addCell(new Phrase("Title", f));
    		        table1.addCell(new Phrase("Price", f));
    		        table1.addCell(new Phrase("Supplier",f));
    		        
    		        Enumeration<Integer> enam = result.keys();
    		      
    		        while(enam.hasMoreElements()) {
    		            Integer k = enam.nextElement();
    		            table1.addCell(k.toString());
    			        table1.addCell(result.get(k).getTitle());
    			        table1.addCell(result.get(k).getPrice().toString());
    			        table1.addCell(result.get(k).getSupp_id().toString());
    	    		}
    		    
    		        document.add(table1);
    		        document.close();
    		        
    		        File file = new File("products.pdf");
    		        
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
    		
    		ObservableList <Product> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Product Product_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(Product_del)) {
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
    	table.setOnMouseClicked(new EventHandler<MouseEvent>() {
    	 @Override
    	 public void handle(MouseEvent e) {
    	  if (e.getClickCount() == 2) {
    		ObservableList <Product> selectedItems = table.getSelectionModel().getSelectedItems();
      		Dictionary <Integer, Product> transferedData = new Hashtable <Integer, Product>();
      		
      		try {
      			if(selectedItems.size()>0) {
      				Product prod=selectedItems.get(0);
      				if(result == null) {
      					System.out.println("dict is null");
      				} else {
      					Enumeration<Integer> enam = result.keys();
      					while(enam.hasMoreElements()) {
      						Integer k = enam.nextElement();
      						if(result.get(k).equals(prod)) {
      							transferedData.put(k, prod);
      							break;
      						}
      					}
      				}
      			}
                FXMLLoader loader = new FXMLLoader(getClass().getResource("prodWindow.fxml"));
                Parent root = loader.load();
                prodController eC = loader.getController();
                eC.setProd(transferedData);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Product");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
              } catch (IOException ex) {
            	  LogTest.LOGGER.log(Level.SEVERE, "Failed to open product", ex);
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

    	table.getColumns().get(0).setText(rb.getString("prod.title"));
    	table.getColumns().get(1).setText(rb.getString("prop.price"));
    	table.getColumns().get(2).setText(rb.getString("supp"));
    	
    	lb_from.setText(rb.getString("filt.from"));
    	lb_to.setText(rb.getString("filt.to"));
    	lb_price.setText(rb.getString("prop.price"));
    	txt_search.setPromptText(rb.getString("prompt"));
    	lb_supp.setText(rb.getString("supp"));
    	cmbox_supp.getItems().set(0, rb.getString("prod.choose_supp"));	
    }
    
    private void search() {
		if(result!=null) {
			((Hashtable<Integer, Product>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
    	args.put("title", txt_search.getText());
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
    		double price;
    		try {
    			price = this.getMaxPrice();
    			txt_to.setText(String.valueOf(price));
    			args.put("price_to", String.valueOf(price));
    		} catch (MyExeception e) {
    			e.printStackTrace();
    			args.put("price_to", String.valueOf(50.0));
    		} catch (NamingException e) {
    			e.printStackTrace();
    			args.put("price_to", String.valueOf(50.0));
    		}
    	}
    	if(!cmbox_supp.getValue().equals(rb.getString("prod.choose_supp"))) {
    		args.put("supp_id", cmbox_supp.getValue().split(":")[0]);
    	}else {
    		args.put("supp_id", "");
    	}  
    	try {
    		final Dictionary<Integer, Product> result1 = doRequest(args);
    		result=result1;
    		
    		Enumeration<Integer> enam = result.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            data.add(result.get(k));
    		}
        } catch (NamingException | MyExeception ex) {
        	LogTest.LOGGER.log(Level.SEVERE, "Failed to get products", ex);
        }
    	table.setItems(data);
    }
    
    private Dictionary<Integer, Product> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
        
        Context ctx = new InitialContext();
        ProductRemote ProductRemote = (ProductRemote) ctx.lookup("ejb:/SimpleEJB2//ProductSessionEJB!ejb.ProductRemote");   
    	Dictionary<Integer, Product> la = ProductRemote.searchProduct(args);
    	return la;
    }
    
    private double getMaxPrice() throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
    	ProductRemote ProductRemote = (ProductRemote) ctx.lookup("ejb:/SimpleEJB2//ProductSessionEJB!ejb.ProductRemote");  
    	double price = ProductRemote.getMaxPrice();
    	return price;
    }
    
    private void delete(Integer id) throws MyExeception, NamingException {
//      
      Context ctx = new InitialContext();
      ProductRemote ProductRemote = (ProductRemote) ctx.lookup("ejb:/SimpleEJB2//ProductSessionEJB!ejb.ProductRemote");   
      ProductRemote.deleteProduct(id);
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
