package application;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.LogTest;
import ejb.MealRemote;
import ejb.ReservationRemote;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import objects.StatisticData;

public class statisticsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnBack;

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    
    @FXML
    private ScrollPane scrollPane;
    
    
    
    @FXML
    private StackedBarChart<String, Number> s_bar_char = new StackedBarChart<String, Number>(xAxis, yAxis);;

    Integer type=1; //1=reserv, 2=rest
    
    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'statisticsRes.fxml'.";
        assert s_bar_char != null : "fx:id=\"s_bar_char\" was not injected: check your FXML file 'statisticsRes.fxml'.";  
        
        btnBack.setOnAction(e ->{
        	if(type==1) {
        		openWindow("reservSearchWindow.fxml",e);
        	}
        	if(type==2) {
        		openWindow("restSearchWindow.fxml",e);
        	}
        	if(type==3) {
        		openWindow("mealSearchWindow.fxml",e);
        	}
        });
    }
    
    private void openWindow(String window, ActionEvent e) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource(window));
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        stage.setTitle("New Window");
	        stage.setScene(scene);
	        stage.show();
	        (((Node) e.getSource())).getScene().getWindow().hide(); 
	        
    	} catch (IOException | java.lang.ClassCastException ex) {
    		LogTest.LOGGER.log(Level.SEVERE, "Failed to open the window "+window, ex);
    	}
    }
    
    public void transferMessage(Integer message) {
        //Display the message
       System.out.println(message);
       type=message;
       if(type==1) {
       	reservStat();
       }
       if(type==3) {
          mealStat();
       }
    }
    
    private void reservStat() {
    	List <StatisticData> data = new ArrayList <StatisticData> ();
		try {
			Context ctx = new InitialContext();
		
			ReservationRemote ReservationRemote = (ReservationRemote) ctx.lookup("ejb:/SimpleEJB2//ReservSessionEJB!ejb.ReservationRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
			System.out.print("process");
			data=ReservationRemote.statReserv();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		List <String> rests = new  ArrayList <String>() ;
		List <Double> profit = new ArrayList <Double>() ;
		List <Double> spends = new ArrayList <Double>() ;
		
		for(StatisticData item : data) {
			rests.add(item.attribute);
			profit.add(item.data2-item.data1);
			spends.add(item.data1);
		}
//		System.out.println(rests+" "+profit+" "+spends);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
     
        
        xAxis.setLabel("Restaurants");
        xAxis.setCategories(FXCollections.<String>observableArrayList(rests));
        
        yAxis.setLabel("Value");
        series1.setName("profit");
        series2.setName("spends");
        for (int i =0; i<rests.size(); i++) {
        	series1.getData().add(new XYChart.Data<>(rests.get(i), profit.get(i)));
        	series2.getData().add(new XYChart.Data<>(rests.get(i), spends.get(i)));
        }

        s_bar_char.getData().add(series1);
        s_bar_char.getData().add(series2);
        
    }
    
    private void mealStat() {
    	List <StatisticData> data = new ArrayList <StatisticData> ();
		try {
			Context ctx = new InitialContext();
		
			MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
			System.out.print("process");
			data=MealRemote.statMeal();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		List <String> meals = new  ArrayList <String>() ;
		List <Double> prices = new ArrayList <Double>() ;
		List <Double> spends = new ArrayList <Double>() ;
		
		for(StatisticData item : data) {
			meals.add(item.attribute);
			prices.add(item.data2-item.data1);
			spends.add(item.data1);
		}
//		System.out.println(rests+" "+profit+" "+spends);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
     
        
        xAxis.setLabel("Restaurants");
        xAxis.setCategories(FXCollections.<String>observableArrayList(meals));
        
        yAxis.setLabel("Value");
        series1.setName("profit");
        series2.setName("spends");
        for (int i =0; i<meals.size(); i++) {
        	series1.getData().add(new XYChart.Data<>(meals.get(i), prices.get(i)));
        	series2.getData().add(new XYChart.Data<>(meals.get(i), spends.get(i)));
        }

        s_bar_char.getData().add(series1);
        s_bar_char.getData().add(series2);
        
    }
    
    
  
}
