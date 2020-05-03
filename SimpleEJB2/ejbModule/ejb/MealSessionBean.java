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
			String sql="select m.id, m.title, m.price, m.prep_time from meal m";
			if(args.get("rest_id")!="") {
				sql+=" join meal_rest mr on m.id=mr.meal_id where mr.rest_id="+args.get("rest_id")+
						" and m.price between "+args.get("price_from")+" and "+args.get("price_to");
			} else {
				sql+=" where m.price between "+args.get("price_from")+" and "+args.get("price_to");
			}
			if(args.get("title")!="") {
				sql+= " and m.title like \"%"+args.get("title")+"%\"";
			}
			System.out.println(sql);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
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
		Integer id=-1;
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
	        sql="SELECT MAX(id) FROM meal";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			resultSet.next();
			
			while(resultSet.next()) {
				id = resultSet.getInt("id");
			}
			
	        
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		return id;
	}

	@Override
	public void deleteMeal(int id) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Delete from meal where id=?";
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
			String sql="Update meal set ?=? where id = ?";
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
