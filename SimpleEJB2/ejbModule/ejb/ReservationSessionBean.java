package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import objects.Reservation;

@Stateless(name = "ReservSessionEJB")
public class ReservationSessionBean implements ReservationRemote {

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public List<Dictionary<Integer, Reservation>> searchReserv(Dictionary<String, String> args) {
		List<Dictionary<Integer, Reservation>>result = new ArrayList <Dictionary<Integer, Reservation>>();
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select r.id, r.rest_id, r.date_start, r.date_end, r.visitors from reservation r";
			System.out.println(sql);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(" Y:MM:dd H:mm:ss");
			while(resultSet.next()) {
				Integer id = resultSet.getInt("id");
                Integer rest_id = resultSet.getInt("rest_id");
                Integer visitors = resultSet.getInt("visitors");
                LocalDate date_start = LocalDate.parse(resultSet.getString("date_start"), dateFormat);
                LocalDate date_end = LocalDate.parse(resultSet.getString("date_end"), dateFormat);
				Dictionary <Integer, Reservation> item = new Hashtable <Integer, Reservation> ();
				Reservation res = new Reservation(rest_id, date_start, date_end, visitors);
				item.put(id, res);
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
	public int addReserv(Dictionary<String, String> args) {
		Integer id=-1;
		try {
			Connection con = dataSource.getConnection();
			String sql = "INSERT INTO reservation (rest_id, date_start, date_end,visitors) Values (?, ?, ?,?)";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, args.get("title"));
	        preparedStatement.setString(2, args.get("price"));
	        preparedStatement.setString(3, args.get("prep_time"));
	        preparedStatement.setString(4, args.get("visitors"));
	        preparedStatement.executeUpdate();
	        sql="SELECT MAX(id) FROM reservation";
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
	public void deleteReserv(int id) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Delete from reservation where id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void updateReserv(Dictionary<String, String> args) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Update reservation set ?=? where id = ?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(3, args.get("id"));
			if(args!=null){
		        Enumeration<String> e = args.keys();
		        while(e.hasMoreElements()) {
		            String k = e.nextElement();
		            if (k!="staff" &&  k!="menu") {
		            	preparedStatement.setString(1, k);
		            	preparedStatement.setString(2, args.get(k));
		            	preparedStatement.executeUpdate();
		            }
		        } 
		        if(args.get("staff")!="") {
		        	sql="DELETE FROM emp_reserv where reserv_id=?";
		        	preparedStatement = con.prepareStatement(sql);
					preparedStatement.setString(1, args.get("id"));
		        	preparedStatement.executeUpdate();
		        	sql="INSERT INTO emp_reserv (reserv_id, emp_id) VALUES(?,?)";
		        	preparedStatement.setString(1, args.get("id")); 
		            String[] arrOfStr = args.get("staff").split(" ", 0);
		            for(String str : arrOfStr) {
		            	preparedStatement.setString(2, str);
		            	preparedStatement.executeUpdate();
		            }
					
		        }
		        if(args.get("menu")!="") {
		        	sql="DELETE FROM meal_reserv where reserv_id=?";
		        	preparedStatement = con.prepareStatement(sql);
					preparedStatement.setString(1, args.get("id"));
		        	preparedStatement.executeUpdate();
		        	sql="INSERT INTO meal_reserv (reserv_id, meal_id) VALUES(?,?)";
		        	preparedStatement.setString(1, args.get("id")); 
		            String[] arrOfStr = args.get("meal").split(" ", 0);
		            for(String str : arrOfStr) {
		            	preparedStatement.setString(2, str);
		            	preparedStatement.executeUpdate();
		            }
		        	
		        }
		    }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
