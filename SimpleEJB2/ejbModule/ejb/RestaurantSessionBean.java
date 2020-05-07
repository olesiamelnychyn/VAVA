package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import objects.Restaurant;
import objects.Zip;

@Stateless(name = "RestaurantSessionEJB")
public class RestaurantSessionBean implements RestaurantRemote {
	
	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	

	@Override
	public Dictionary<Integer, Restaurant> searchRestaurant(Dictionary<String, String> args) {
		Dictionary<Integer, Restaurant>result = new Hashtable <Integer, Restaurant>();
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select r.id, r.capacity, r.zip, z.state from restaurant r join zip z on z.code=r.zip ";
			sql+="where r.capacity between "+args.get("vis_from")+" and "+args.get("vis_to");
			if(args.get("zip")!="") {
				sql+= " and r.zip ="+args.get("zip");
			}
			sql+=" order by r.id";
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				Integer id = resultSet.getInt("id");
                Integer capacity = resultSet.getInt("capacity");
                String code = resultSet.getString("zip");
                String state = resultSet.getString("state");
                Zip z = new Zip(code, state);
                Restaurant rest = new Restaurant(z, capacity);
				result.put(id, rest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(result);
		return result;
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
			String sql="select r.id from reservation r where r.rest_id="+String.valueOf(id);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			try {
				Context ctx;
				ctx = new InitialContext();
				ReservationRemote ReservationRemote = (ReservationRemote) ctx.lookup("ejb:/SimpleEJB2//ReservationSessionEJB!ejb.ReservationRemote");
				while(resultSet.next()) {
					ReservationRemote.deleteReserv(resultSet.getInt("id"));
				}
			} catch (NamingException e) {				
				e.printStackTrace();
			}
			
			sql="select e.id from employee e where e.rest_id="+String.valueOf(id);
			stmt = con.createStatement();
			resultSet = stmt.executeQuery(sql);
			try {
				Context ctx;
				ctx = new InitialContext();
				EmployeeRemote EmployeeRemote = (EmployeeRemote) ctx.lookup("ejb:/SimpleEJB2//EmployeeSessionEJB!ejb.EmployeeRemote");
				while(resultSet.next()) {
					EmployeeRemote.deleteEmployee(resultSet.getInt("id"));
				}
			} catch (NamingException e) {				
				e.printStackTrace();
			}
			
			sql="Delete from meal_rest where rest_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			sql="Delete from cheque where rest_id=?";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			
			sql="Delete from restaurant where id=?";
			preparedStatement = con.prepareStatement(sql);
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

	@Override
	public ArrayList<Zip> getZip() {
		ArrayList<Zip>result = new ArrayList<Zip>();
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select z.code, z.state from zip z ";
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
                String code = resultSet.getString("code");
                String state = resultSet.getString("state");
                Zip z = new Zip(code, state);
				result.add(z);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	@Override
	public int getMaxCapacity() {
		int capacity = 0;
		try {
			Connection con = dataSource.getConnection();
			String sql="SELECT MAX(capacity) FROM restaurant";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			resultSet.next();
			
			while(resultSet.next()) {
				capacity = resultSet.getInt("capacity");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return capacity;
	}

}
