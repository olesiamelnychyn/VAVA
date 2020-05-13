package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import ejb.ReservationRemote;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Reservation;
import objects.Restaurant;

public class reservSearchController {

	@FXML
    private Button btn_lang;
	
    @FXML
    private ResourceBundle  rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

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
    private DatePicker date_from;

    @FXML
    private DatePicker date_to;
    
    @FXML
    private Label date_range;
    
    @FXML
    private Label amount_vis;
    
    @FXML
    private Label from;
    
    @FXML
    private Label to;
    
    @FXML
    private Label rest;
    
    
    Dictionary<Integer, Reservation> result;
    ObservableList<Reservation> data ;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    Context ctx;
    ReservationRemote ReservationRemote ; 
    
    @FXML
    void initialize() {
    	
    	assert date_range != null : "fx:id=\"date_range\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
    	assert amount_vis != null : "fx:id=\"amount_vis\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
    	assert from != null : "fx:id=\"from\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
    	assert to != null : "fx:id=\"to\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
    	assert rest != null : "fx:id=\"res\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'reservSearchWindow.fxml'.";
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
     
        
        try {
			ctx = new InitialContext();
			ReservationRemote = (ReservationRemote) ctx.lookup("ejb:/SimpleEJB2//ReservSessionEJB!ejb.ReservationRemote");
		} catch (NamingException e3) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to connect to ReservationRemote", e3);
		}
        
        TableColumn <Reservation, Integer> restCol = new TableColumn <Reservation, Integer> ("Restaurant");
        restCol.setCellValueFactory(new PropertyValueFactory<>("rest_id"));
        TableColumn <Reservation, LocalDateTime> fromCol = new TableColumn <Reservation, LocalDateTime> ("Date From");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("date_start"));
        TableColumn <Reservation, LocalDateTime> toCol = new TableColumn <Reservation, LocalDateTime> ("Date To");
        toCol.setCellValueFactory(new PropertyValueFactory<>("date_end"));
        TableColumn <Reservation, Integer> visCol = new TableColumn <Reservation, Integer> ("Visitors");
        visCol.setCellValueFactory(new PropertyValueFactory<>("visitors"));
        data = FXCollections.observableArrayList();
        table.getColumns().add(restCol);
        table.getColumns().add(fromCol);
        table.getColumns().add(toCol);
        table.getColumns().add(visCol);
        table.setEditable(true);
        
        
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
       	 @Override
       	 public void handle(MouseEvent e) {
       	  if (e.getClickCount() == 2) {
       		ObservableList <Reservation> selectedItems = table.getSelectionModel().getSelectedItems();
         		Dictionary <Integer, Reservation> transferedData = new Hashtable <Integer, Reservation>();
         		
         		try {
         			if(selectedItems.size()>0) {
         				Reservation res=selectedItems.get(0);
         				if(result == null) {
         					//System.out.println("dict is null");
         				} else {
         					Enumeration<Integer> enam = result.keys();
         					while(enam.hasMoreElements()) {
         						Integer k = enam.nextElement();
         						if(result.get(k).equals(res)) {
         							System.out.println("Gonna open this one"+k);
         							transferedData.put(k, res);
         							break;
         						}
         					}
         				}
         			}
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("reservWindow.fxml"));
                   Parent root = loader.load();
                   reservController scene2Controller = loader.getController();
                   scene2Controller.setReserv(transferedData);
                   Stage stage = new Stage();
                   stage.setScene(new Scene(root));
                   stage.setTitle("Meal");
                   stage.show();
                   ((Node)(e.getSource())).getScene().getWindow().hide(); 
                 } catch (IOException ex) {
                	 LogTest.LOGGER.log(Level.SEVERE, "Failed to open reservation", e);
                	 ex.printStackTrace();;
                 }
       	  }
       	 }
       	});

		try {
			ObservableList<String> rests = FXCollections.observableArrayList();
			rests.add(rb.getString("reserv.choose_rest"));
			Dictionary<Integer, Restaurant> la = ReservationRemote.getRestReserv(0);
			Enumeration<Integer> enam1 = la.keys();
	        while(enam1.hasMoreElements()) {
	            Integer k = enam1.nextElement();
	            String rest = k+", cap: "+la.get(k).getCapacity()+ ","+la.get(k).getZip().getState();
	            rests.add(rest);
	        }
	        cmbox_rest.setItems(rests);
		} catch (SQLException e2) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get restaurants", e2);
		}
	
    	cmbox_rest.getSelectionModel().select(0);
    	date_from.setValue(LocalDate.parse("01.01.2019", formatter));
    	date_to.setValue(LocalDate.parse("31.12.2021",  formatter));
    	txt_from.setText("0");
    	txt_to.setText("100");
    	search();
    	
        btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {
    		Dictionary <Integer, Reservation> transferedData = new Hashtable <Integer, Reservation>();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("reservWindow.fxml"));
			Parent root = loader.load();
			reservController scene2Controller = loader.getController();
			scene2Controller.setReserv(transferedData);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
            stage.setTitle("Meal");
            stage.show();
            ((Node)(e.getSource())).getScene().getWindow().hide(); 
        } catch (IOException ex) {
        	LogTest.LOGGER.log(Level.SEVERE, "Failed to open reservation window", e);
        }
		});
    	
    	btn_export.setOnMouseClicked(e -> {
    		TablePrincipale("reservations.pdf");
    	});
    	
    	btn_stat.setOnMouseClicked(e -> {
    		try {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("statisticsRes.fxml"));
                Parent root = loader.load();
                statisticsController scene2Controller = loader.getController();
                scene2Controller.transferMessage(Integer.valueOf(1));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Second Window");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
            } catch (IOException ex) {
            	LogTest.LOGGER.log(Level.SEVERE, "Failed to open statistics", e);
            }
    	});
    	
    	btn_help.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
    	
    	btn_delete.setOnMouseClicked(e->{
    		
    		ObservableList <Reservation> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Reservation Reservation_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(Reservation_del)) {
	    	            	
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
    	btn_new.setText(rb.getString("btn.new"));
    	btn_delete.setText(rb.getString("btn.del"));
    	btn_stat.setText(rb.getString("btn.stat"));
    	btn_help.setText(rb.getString("btn.help"));
    	btn_search.setText(rb.getString("btn.search"));
    	table.getColumns().get(0).setText(rb.getString("rest"));
    	table.getColumns().get(1).setText(rb.getString("reserv.date_start"));
    	table.getColumns().get(2).setText(rb.getString("reserv.date_end"));
    	table.getColumns().get(3).setText(rb.getString("reserv.visit"));
    	date_range.setText(rb.getString("reserv.date_range"));
    	
    	from.setText(rb.getString("filt.from"));
    	to.setText(rb.getString("filt.to"));
    	amount_vis.setText(rb.getString("reserv.amount_vis"));
    	rest.setText(rb.getString("rest"));
    	cmbox_rest.getItems().set(0, rb.getString("reserv.choose_rest"));	
    }
 
    private Dictionary<Integer, Reservation> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
       
    	Dictionary<Integer, Reservation> la = ReservationRemote.searchReserv(args);
    	return la;
    }
    
    private void search() {
    	if(result!=null) {
			((Hashtable<Integer, Reservation>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
		if(!cmbox_rest.getValue().equals("Choose restaurant")) {
			System.out.println(cmbox_rest.getValue().split(",")[0]);
			args.put("rest_id", cmbox_rest.getValue().split(",")[0]);
		} else {
			args.put("rest_id", "");
		}
		
    	try {
    		args.put("vis_from", Integer.valueOf(txt_from.getText()).toString());
    		
    	} catch (NumberFormatException ex) {
    		txt_from.setText("0");
    		args.put("vis_from", "0");
    		
    	}
    	
    	try {
    		args.put("vis_to", Integer.valueOf(txt_to.getText()).toString());
    		
    	} catch (NumberFormatException ex) {
    		args.put("vis_to", "100");
    		txt_to.setText("100");
    	}
    	
    	try {
    		args.put("date_from", date_from.getValue().toString());
    	} catch (NumberFormatException ex) {
    		args.put("date_from","01.01.2019");
    		date_from.setValue(LocalDate.parse("01.01.2019", formatter));
    	}
    	
    	try {
    		args.put("date_to", date_to.getValue().toString());
    		
    	} catch (NumberFormatException ex) {
    		args.put("date_to","31.12.2021");
    		date_to.setValue(LocalDate.parse("31.12.2021",  formatter));
    	}
    	
    	try {
    		final Dictionary<Integer, Reservation> result1 = doRequest(args);
    		result=result1;
    		
    		Enumeration<Integer> enam = result.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            data.add(result.get(k));
    		}
        } catch (NamingException | MyExeception ex) {
        	LogTest.LOGGER.log(Level.SEVERE, "Failed to get reservations", ex);
        } 
    	table.setItems(data);
		
    }
    
    private void delete(Integer id) throws MyExeception, NamingException {     

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
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window , ex);
    	}
    }
    
    public void TablePrincipale(String dest) { 	
        try {
        	Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(dest));
			 document.open();
		        PdfPTable table1 = new PdfPTable(5);
		        Font f = new Font();
		        f.setColor(BaseColor.RED);
		        f.setStyle(java.awt.Font.BOLD);
		        table1.addCell(new Phrase("Id", f));
		        table1.addCell(new Phrase("Title", f));
		        table1.addCell(new Phrase("Price", f));
		        table1.addCell(new Phrase("Preparation Time", f));
		        
		        Enumeration<Integer> enam = result.keys();
		      
		        while(enam.hasMoreElements()) {
		            Integer k = enam.nextElement();
		            table1.addCell(k.toString());
			        table1.addCell(String.valueOf(result.get(k).getRest_id()));
			        table1.addCell(result.get(k).getDate_start().toString().split("T")[0]+" "+result.get(k).getDate_start().toString().split("T")[1]);
			        table1.addCell(result.get(k).getDate_end().toString().split("T")[0]+" "+result.get(k).getDate_end().toString().split("T")[1]);
			        table1.addCell(String.valueOf(result.get(k).getVisitors()));
	    		}
		    
		        document.add(table1);
		        document.close();
		        
		        File file = new File(dest);
		        
		        //first check if Desktop is supported by Platform or not
		        if(!Desktop.isDesktopSupported()){
		        	LogTest.LOGGER.log(Level.INFO, "Desktop is not supported");
		            return;
		        }
		        
		        Desktop desktop = Desktop.getDesktop();
		        if(file.exists()) desktop.open(file);
		        
		       
		     
		} catch (DocumentException | IOException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to create document", e);
		}
       
	}
}
