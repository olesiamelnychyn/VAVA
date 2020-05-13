package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
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
import objects.Meal;
import objects.Restaurant;
import ejb.LogTest;
import ejb.MealRemote;
import ejb.MyExeception;

public class mealSearchController {

	@FXML
    private Button btn_lang;
	
    @FXML
    private ResourceBundle  rb =  ResourceBundle.getBundle("texts", Locale.forLanguageTag("en"));

    @FXML
    private URL location;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView <Meal> table;

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
    private Label price;
    
    @FXML
    private Label rest;
    @FXML
    private Label from;
    @FXML
    private Label to;

    @FXML
    private Tooltip tool_tip;

    Dictionary<Integer, Meal> result;
    ObservableList<Meal> data ;
    
    @FXML
    void initialize() {
    	assert to != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert price != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert from != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert rest != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    	assert btn_lang != null : "fx:id=\"btn_lang\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert txt_search != null : "fx:id=\"txt_search\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_search != null : "fx:id=\"btn_search\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert txt_from != null : "fx:id=\"txt_from\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert txt_to != null : "fx:id=\"txt_to\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert cmbox_rest != null : "fx:id=\"cmbox_rest\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_home != null : "fx:id=\"btn_home\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_new != null : "fx:id=\"btn_new\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_delete != null : "fx:id=\"btn_delete\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_export != null : "fx:id=\"btn_export\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_stat != null : "fx:id=\"btn_stat\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert tool_tip1 != null : "fx:id=\"tool_tip1\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert btn_help != null : "fx:id=\"btn_help\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
        assert tool_tip != null : "fx:id=\"tool_tip\" was not injected: check your FXML file 'mealSearchWindow.fxml'.";
    
        TableColumn <Meal, String> titleCol = new TableColumn <Meal, String> ("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn <Meal, Double> priceCol = new TableColumn <Meal, Double> ("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn <Meal, LocalTime> prepCol = new TableColumn <Meal, LocalTime> ("Preparation");
        prepCol.setCellValueFactory(new PropertyValueFactory<>("prep_time"));
        data = FXCollections.observableArrayList();
        table.getColumns().add(titleCol);
        table.getColumns().add(priceCol);
        table.getColumns().add(prepCol);
        table.setEditable(true);

        ObservableList<String> rests = FXCollections.observableArrayList();
        rests.add("Choose restaurant");
		
		try {
			Context ctx = new InitialContext();
			MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
			Dictionary<Integer, Restaurant> la = MealRemote.getRestMeal(0);
			
			Enumeration<Integer> enam = la.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            String rest = k+", cap: "+la.get(k).getCapacity()+ ","+la.get(k).getZip().getState();
	            //System.out.println(rest);
	            rests.add(rest);
	            
    		}
	        //System.out.println(rests);
	        cmbox_rest.setItems(rests);
		} catch (NamingException | SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to connect to MealRemote", e);
		}
    	cmbox_rest.getSelectionModel().select(0);
    	txt_from.setText("0.0");
    	txt_to.setText("50.0");
    	search();
    	
    	btn_home.setOnMouseClicked(e -> {openWindow("mainWindow.fxml", e);});
    	
    	btn_new.setOnMouseClicked(e -> {
    		Dictionary <Integer, Meal> transferedData = new Hashtable <Integer, Meal>();
    		try {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("mealWindow.fxml"));
    			Parent root = loader.load();
    			mealController scene2Controller = loader.getController();
    			scene2Controller.setMeal(transferedData);
    			Stage stage = new Stage();
    			stage.setScene(new Scene(root));
                stage.setTitle("Meal");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
            } catch (IOException ex) {
            	LogTest.LOGGER.log(Level.SEVERE, "Failed to open the mealWindow", e);
            }
    		});
    	
    	table.getSelectionModel().setCellSelectionEnabled(true);  // selects cell only, not the whole row
    	table.setOnMouseClicked(new EventHandler<MouseEvent>() {
    	 @Override
    	 public void handle(MouseEvent e) {
    	  if (e.getClickCount() == 2) {
    		ObservableList <Meal> selectedItems = table.getSelectionModel().getSelectedItems();
      		Dictionary <Integer, Meal> transferedData = new Hashtable <Integer, Meal>();
      		
      		try {
      			if(selectedItems.size()>0) {
      	    		Meal meal=selectedItems.get(0);
      				if(result == null) {
      					//System.out.println("dict is null");
      				} else {
      					Enumeration<Integer> enam = result.keys();
      					while(enam.hasMoreElements()) {
      						Integer k = enam.nextElement();
      						if(result.get(k).equals(meal)) {
      							//System.out.println("Gonna open this one"+k);
      							transferedData.put(k, meal);
      							break;
      						}
      					}
      				}
      			}
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mealWindow.fxml"));
                Parent root = loader.load();
                mealController scene2Controller = loader.getController();
                scene2Controller.setMeal(transferedData);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Meal");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
              } catch (IOException ex) {
            	  LogTest.LOGGER.log(Level.SEVERE, "Failed to open the mealWindow", e);
              }
    	  }
    	 }
    	});
    	
    	btn_export.setOnMouseClicked(e -> {
    		TablePrincipale("meals.pdf");
    	});
    	
    	btn_stat.setOnMouseClicked(e -> {
    		try {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("statisticsRes.fxml"));
                Parent root = loader.load();
                statisticsController scene2Controller = loader.getController();
                scene2Controller.transferMessage(Integer.valueOf(3));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Statistics - Meal");
                stage.show();
                ((Node)(e.getSource())).getScene().getWindow().hide(); 
            } catch (IOException ex) {
            	LogTest.LOGGER.log(Level.SEVERE, "Failed to open statistics", e);
            }
    	});
    	
    	
    	btn_help.setOnMouseClicked(e->{openWindow("helpWindow.fxml", e);});
    	
    	btn_delete.setOnMouseClicked(e->{
    		
    		ObservableList <Meal> selectedItems = table.getSelectionModel().getSelectedItems();
    		for (Meal meal_del : selectedItems) {
    		int break1=0;
				if(result == null) {
	    	        //System.out.println("dict is null");
	    	    } else {
	    	        Enumeration<Integer> enam = result.keys();
	    	        while(enam.hasMoreElements()) {
	    	            Integer k = enam.nextElement();
	    	            if(result.get(k).equals(meal_del)) {
	    	            	//System.out.println("Gonna delete this one"+k);
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
        	btn_new.setText(rb.getString("btn.new"));
        	btn_delete.setText(rb.getString("btn.del"));
        	btn_stat.setText(rb.getString("btn.stat"));
        	btn_help.setText(rb.getString("btn.help"));
        	btn_search.setText(rb.getString("btn.search"));
        	table.getColumns().get(0).setText(rb.getString("meal.title"));
        	table.getColumns().get(1).setText(rb.getString("meal.price"));
        	table.getColumns().get(2).setText(rb.getString("meal.prep_time"));
        	txt_search.setPromptText(rb.getString("prompt"));
        	price.setText(rb.getString("meal.price"));
        	from.setText(rb.getString("filt.from"));
        	to.setText(rb.getString("filt.to"));
        	rest.setText(rb.getString("rest"));
        	cmbox_rest.getItems().set(0, rb.getString("reserv.choose_rest"));	
        }
     
	private Dictionary<Integer, Meal> doRequest(Dictionary <String, String> args) throws NamingException, MyExeception {
    
        Context ctx = new InitialContext();
        MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
    	Dictionary<Integer, Meal> la = MealRemote.searchMeal(args);
    	return la;
    }
    
	private void search() {
		if(result!=null) {
			((Hashtable<Integer, Meal>) result).clear();
			data.clear();
		}
		Dictionary <String, String> args = new Hashtable <String, String> ();
    	args.put("title", txt_search.getText());
		if(cmbox_rest.getValue()!="Choose restaurant") {
			System.out.println(cmbox_rest.getValue().split(",")[0]);
			args.put("rest_id", cmbox_rest.getValue().split(",")[0]);
		} else {
			args.put("rest_id", "");
		}
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
    		txt_to.setText("50.0");
    		args.put("price_to", String.valueOf(50.0));
    	}
    	
    	try {
    		final Dictionary<Integer, Meal> result1 = doRequest(args);
    		result=result1;
    		
    		Enumeration<Integer> enam = result.keys();
	        while(enam.hasMoreElements()) {
	            Integer k = enam.nextElement();
	            data.add(result.get(k));
    		}
        } catch (NamingException | MyExeception ex) {
        	LogTest.LOGGER.log(Level.SEVERE, "Failed to get meals", ex);
        } 
    	table.setItems(data);
    }
	
    private void delete(Integer id) throws MyExeception, NamingException {
     
    	Context ctx = new InitialContext();
        MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        System.out.print("process");
        MealRemote.deleteMeal(id);
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
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window, e);
    	}
    }
    
    
    public void TablePrincipale(String dest) {
        try {
        	Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(dest));
			 document.open();
		        PdfPTable table1 = new PdfPTable(4);
		        Font f = new Font();
		        f.setColor(BaseColor.RED);
		        f.setStyle(java.awt.Font.BOLD);
		        
		        table1.addCell(new Phrase("id", f));
		        table1.addCell(new Phrase("Title", f));
		        table1.addCell(new Phrase("Price", f));
		        table1.addCell(new Phrase("Preparation Time",f));
		        
		        Enumeration<Integer> enam = result.keys();
		      
		        while(enam.hasMoreElements()) {
		            Integer k = enam.nextElement();
		            table1.addCell(k.toString());
			        table1.addCell(result.get(k).getTitle());
			        table1.addCell(result.get(k).getPrice().toString());
			        table1.addCell(result.get(k).getPrep_time().toString());
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
