package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import objects.Restaurant;

@Stateless(name = "RestaurantSessionEJB")
public class RestaurantSessionBean implements RestaurantRemote {
	
	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	

	@Override
	public Dictionary<Integer, Restaurant> searchRestaurant(Dictionary<String, String> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addRestaurant(Dictionary<String, String> args) {
		Integer id=-1;
		try {
			Connection con = dataSource.getConnection();
			String sql = "INSERT INTO restaurant (capacity, zip) VALUES (?,?)";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, args.get("capacity"));
	        preparedStatement.setString(2, args.get("zip"));
	        preparedStatement.executeUpdate();
	        
	        sql="SELECT MAX(id) FROM restaurant";
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
	public void deleteRestaurant(int id) {
		try {
			Connection con = dataSource.getConnection();
//			String sql="Delete from emp_reserv where emp_id=?";
//			PreparedStatement preparedStatement = con.prepareStatement(sql);
//			preparedStatement.setInt(1, id);
//			preparedStatement.executeUpdate();
			
			String sql="Delete from restaurant where id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateRestaurant(Dictionary<String, String> args) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Update restaurant set ?=? where id = ?";
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
