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

import ejb.RestaurantRemote;
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
import objects.Restaurant;
import objects.Zip;

public class restSearchController {

	@FXML
    private Button btn_lang;
	
    @FXML
    private ResourceBundle rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

    @FXML
    private URL location;

    @FXML
    private TableView<Restaurant> table;

    @FXML
    private Button btn_search;

    @FXML
    private TextField txt_from;

    @FXML
    private TextField txt_to;

    @FXML
    private ComboBox<String> cmbox_zip;

    @FXML
    private Button btn_home;

    @FXML
    private Button btn_new;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_export;

    @FXML
    private Button btn_stat;

    @FXML
    private Tooltip tool_tip1;

    @FXML
    private Button btn_help;

    @FXML
    private Tooltip tool_tip;
    
    @FXML
    private Label lb_from;
    
    @FXML
    private Label lb_zip;
    
    @FXML
    private Label lb_capacity;
    
    @FXML
    private Label lb_to;
    

    Dictionary<Integer, Restaurant> result;
    ObservableList<Restaurant> data ;

    @FXML
    void initialize() {
    	assert lb_zip != null : "fx:id=\"lb_zip\" was not injected: check your FXML file 'mealWindow.fxml'.";
    	assert lb_capacity != null : "fx:id=\"lb_capacity\" was not injected: check your FXML file 'mealWindow.fxml'.";
    	assert lb_from != null : "fx:id=\"lb_from\" was not injected: check your FXML file 'mealWindow.fxml'.";
    	assert lb_to != null : "fx:id=\"lb_to\" was not injected: check your FXML file 'mealWindow.fxml'.";
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert txt_from != null : "fx:id=\"txt_from\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert txt_to != null : "fx:id=\"txt_to\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert cmbox_zip != null : "fx:id=\"cmbox_zip\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_stat != null : "fx:id=\"btn_stat\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert tool_tip1 != null : "fx:id=\"tool_tip1\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'restSearchWindow.fxml'.";
        
        TableColumn <Restaurant, Integer> capCol = new TableColumn <Restaurant, Integer> ("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        TableColumn <Restaurant, String> zip_codeCol = new TableColumn <Restaurant, String> ("Code");
        zip_codeCol.setCellValueFactory(new PropertyValueFactory<>("zip"));
        
        data = FXCollections.observableArrayList();
        table.getColumns().add(capCol);
        table.getColumns().add(zip_codeCol);
        table.setEditable(true);
        
        ObservableList<String> zip = FXCollections.observableArrayList();
        zip.add("Choose zip");
        
        try {
			for (Zip p : this.getZIP()) { 		      
				zip.add(p.toString());		
			}
		} catch (MyExeception | NamingException e2) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get zip", e2);
		} 
        cmbox_zip.setItems(zip);
        cmbox_zip.getSelectionModel().select(0);
    	txt_from.setText("0");
    	int capacity;
		try {
			capacity = this.getMaxCap();
			txt_to.setText(String.valueOf(capacity));
		} catch (MyExeception e) {
			e.printStackTrace();
			txt_to.setText(String.valueOf(100));
		} catch (NamingException e) {
			e.printStackTrace();
			txt_to.setText(String.valueOf(100));
		}

    	search();
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {openWindow("restWindow.fxml", e);});
    	
    	btn_export.setOnMouseClicked(e -> {
    		try {
            	Document document = new Document();
    			PdfWriter.getInstance(document, new FileOutputStream("restaurants.pdf"));
    			 document.open();
    		        PdfPTable table1 = new PdfPTable(3);
    		        Font f = new Font();
    		        f.setColor(BaseColor.RED);
    		        f.setStyle(java.awt.Font.BOLD);
    		        
    		        table1.addCell(new Phrase("id", f));
    		        table1.addCell(new Phrase("Capacity", f));
    		        table1.addCell(new Phrase("Zip", f));
    		        
    		        Enumeration<Integer> enam = result.keys();
    		      
    		        while(enam.hasMoreElements()) {
    		            Integer k = enam.nextElement();
    		            table1.addCell(k.toString());
    			        table1.addCell(result.get(k).getCapacity().toString());
    			        table1.addCell(result.get(k).getZip().toString());
    	    		}
    		    
    		        document.add(table1);
    		        document.close();
    		        
    		        File file = new File("restaurants.pdf");
    		        
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
    		
    		
    		ObservableList <Restaurant> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Restaurant Restaurant_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(Restaurant_del)) {
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
        
        btn_stat.setOnMouseClicked(e ->{
        	try {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("statisticsRes.fxml"));
                Parent root = loader.load();
                statisticsController scene2Controller = loader.getController();
                scene2Controller.transferMessage(Integer.valueOf(2));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Statistics for Restaurants");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
            } catch (IOException ex) {
            	LogTest.LOGGER.log(Level.SEVERE, "Failed to open statistics", e);
            }
        });
        
        table.getSelectionModel().setCellSelectionEnabled(true);  // selects cell only, not the whole row
    	table.setOnMouseClicked(new EventHandler<MouseEvent>() {
    	 @Override
    	 public void handle(MouseEvent e) {
    	  if (e.getClickCount() == 2) {
    		ObservableList <Restaurant> selectedItems = table.getSelectionModel().getSelectedItems();
      		Dictionary <Integer, Restaurant> transferedData = new Hashtable <Integer, Restaurant>();
      		
      		try {
      			if(selectedItems.size()>0) {
      				Restaurant rest=selectedItems.get(0);
      				if(result == null) {
      					System.out.println("dict is null");
      				} else {
      					Enumeration<Integer> enam = result.keys();
      					while(enam.hasMoreElements()) {
      						Integer k = enam.nextElement();
      						if(result.get(k).equals(rest)) {
      							transferedData.put(k, rest);
      							break;
      						}
      					}
      				}
      			}
                FXMLLoader loader = new FXMLLoader(getClass().getResource("restWindow.fxml"));
                Parent root = loader.load();
                restController rC = loader.getController();
                rC.setRest(transferedData);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Restaurant");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
              } catch (IOException ex) {
            	  LogTest.LOGGER.log(Level.SEVERE, "Failed to open restaurant", ex);
              }
    	  }
    	 }
    	});

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
    	btn_new.setText(rb.getString("btn.new"));
    	btn_delete.setText(rb.getString("btn.del"));
    	btn_help.setText(rb.getString("btn.help"));
    	btn_search.setText(rb.getString("btn.search"));

    	table.getColumns().get(0).setText(rb.getString("rest.cap"));
    	table.getColumns().get(1).setText(rb.getString("rest.zip"));
    	
    	lb_from.setText(rb.getString("filt.from"));
    	lb_to.setText(rb.getString("filt.to"));
    	lb_capacity.setText(rb.getString("rest.cap"));
    	cmbox_zip.getItems().set(0, rb.getString("rest.choose_zip"));
	
    }
    
    private void search() {
		if(result!=null) {
			((Hashtable<Integer, Restaurant>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
    	try {
    		args.put("vis_from", Integer.valueOf(txt_from.getText()).toString());
    		txt_from.setText(Integer.valueOf(txt_from.getText()).toString());
    	} catch (NumberFormatException ex) {
    		txt_from.setText("0");
    		args.put("vis_from", String.valueOf(0));
    	}
    	try {
    		args.put("vis_to", Integer.valueOf(txt_to.getText()).toString());
    		txt_to.setText(Integer.valueOf(txt_to.getText()).toString());
    	} catch (NumberFormatException ex) {
    		int cap;
			try {
				cap = this.getMaxCap();
				txt_to.setText(String.valueOf(cap));
				args.put("vis_to", String.valueOf(cap));
			} catch (MyExeception e) {
				e.printStackTrace();
				args.put("vis_to", String.valueOf(100));
				txt_to.setText(String.valueOf(100));
			} catch (NamingException e) {
				e.printStackTrace();
				args.put("vis_to", String.valueOf(100));
				txt_to.setText(String.valueOf(100));
			}
    	}
    	if(cmbox_zip.getValue()!=rb.getString("rest.choose_zip")) {
    		args.put("zip", cmbox_zip.getValue().split(",")[0].split(" ")[1]);
    	}else {
    		args.put("zip", "");
    	}
    	
//    System.out.println(args);
       
    	try {
    		final Dictionary<Integer, Restaurant> result1 = doRequest(args);
    		result=result1;
    		
    		Enumeration<Integer> enam = result.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            data.add(result.get(k));
    		}
        } catch (NamingException | MyExeception ex) {
        	LogTest.LOGGER.log(Level.SEVERE, "Failed to get restaurants", ex);
        } 
    	table.setItems(data);
    }
    
    private Dictionary<Integer, Restaurant> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
        
        Context ctx = new InitialContext();
        RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
    	Dictionary<Integer, Restaurant> la = RestaurantRemote.searchRestaurant(args);
    	return la;
    }
    
    private int getMaxCap() throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
    	RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        int cap = RestaurantRemote.getMaxCapacity();
        System.out.println(cap);
    	return cap;
    }
    
    private ArrayList<Zip> getZIP() throws MyExeception, NamingException {
    	Context ctx = new InitialContext();
    	RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        ArrayList<Zip> zip = RestaurantRemote.getZip();
        System.out.println(zip);
//        ArrayList<String> z = new ArrayList<String>();
//        for (Zip p : zip) { 		      
//			z.add(p.getCode()+" "+p.getState());		
//		}
//        System.out.println(z);
    	return zip;
    }
    
    private void delete(Integer id) throws MyExeception, NamingException {  
      Context ctx = new InitialContext();
      RestaurantRemote RestaurantRemote = (RestaurantRemote) ctx.lookup("ejb:/SimpleEJB2//RestaurantSessionEJB!ejb.RestaurantRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
      System.out.print("process");
      RestaurantRemote.deleteRestaurant(id);
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
}
