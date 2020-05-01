package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import objects.Meal;

@Stateless(name = "MealSessionEJB")
public class MealSessionBean implements MealRemote{

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public List<Dictionary<Integer, Meal>> searchMeal(Dictionary <String, String>  args) {
		List<Dictionary<Integer, Meal>>result = new ArrayList <Dictionary<Integer, Meal>>();
		
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery("select id, title, price, prep_time from meal");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
			while(resultSet.next()) {
				Integer id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Double price = resultSet.getDouble("price");
                LocalTime prep_time = LocalTime.parse(resultSet.getString("prep_time"), dateFormat);
				Dictionary <Integer, Meal> item = new Hashtable <Integer, Meal> ();
				Meal meal = new Meal(title, price, prep_time);
				item.put(id, meal);
				System.out.println(item);
				result.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	@Override
	public int addMeal(Dictionary <String, String> args) {

		try {
			Connection con = dataSource.getConnection();
			String sql = "INSERT INTO meal (title, price, prep_time) Values (?, ?, ?)";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, args.get("title"));
	        preparedStatement.setString(2, args.get("price"));
	        preparedStatement.setString(3, args.get("prep_time"));
	        preparedStatement.executeUpdate();
	        String image=args.get("image");
	        if(image != null) {
	        	System.out.println("Will be implemented latter");
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		

		
		return 0;
	}

	@Override
	public void deleteMeal(int id) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Delete from meals where id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateMeal(Dictionary <String, String>  args) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Update column ?=? where id = ?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(3, args.get("id"));
			if(args!=null){
		        Enumeration<String> e = args.keys();
		        while(e.hasMoreElements()) {
		            String k = e.nextElement();
		            preparedStatement.setString(1, k);
		            preparedStatement.setString(2, args.get(k));
					preparedStatement.executeUpdate();
		        }
		    }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
