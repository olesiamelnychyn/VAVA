package application;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.ReservationRemote;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import objects.StatisticData;

public class statisticsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu btnBack;

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    
    @FXML
    private ScrollPane scrollPane;
    
    
    
    @FXML
    private StackedBarChart<String, Number> s_bar_char = new StackedBarChart<String, Number>(xAxis, yAxis);;

    Integer type=0; //1=reserv, 2=rest
    
    @FXML
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'statisticsRes.fxml'.";
        assert s_bar_char != null : "fx:id=\"s_bar_char\" was not injected: check your FXML file 'statisticsRes.fxml'.";    
    }
    
    public void transferMessage(Integer message) {
        //Display the message
       System.out.println(message);
       type=message;
       if(type==1) {
       	reservStat();
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
}
