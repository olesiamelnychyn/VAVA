package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
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
import objects.StatisticData;

@Stateless(name = "ReservSessionEJB")
public class ReservationSessionBean implements ReservationRemote {

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public Dictionary<Integer, Reservation> searchReserv(Dictionary<String, String> args) {
		Dictionary<Integer, Reservation>result = new Hashtable<Integer, Reservation>();
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select r.id, r.rest_id, r.date_start, r.date_end, r.visitors from reservation r";
			sql+=" where r.date_start > \""+args.get("date_from")+"\" and r.date_end < \""+args.get("date_to")+"\"";
			sql+=" and r.visitors between "+args.get("vis_from")+" and "+args.get("vis_to");
			
			if(args.get("rest_id")!="") {
				sql+=" and r.rest_id="+args.get("rest_id");
			}
			
			//System.out.println(sql);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
			while(resultSet.next()) {
				Integer id = resultSet.getInt("id");
                Integer rest_id = resultSet.getInt("rest_id");
                Integer visitors = resultSet.getInt("visitors");
                LocalDateTime date_start = LocalDateTime.parse(resultSet.getString("date_start"), dateFormat);
                LocalDateTime date_end = LocalDateTime.parse(resultSet.getString("date_end"), dateFormat);
				
				Reservation res = new Reservation(rest_id, date_start, date_end, visitors);
				result.put(id, res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(result);
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
			String sql="Delete from emp_reserv where reserv_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			sql="Delete from meal_reserv where reserv_id=?";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			sql="Delete from reservation where id=?";
			preparedStatement = con.prepareStatement(sql);
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

	@Override
	public List<StatisticData> statReserv() {
		List <StatisticData> stat = new ArrayList <StatisticData> ();
		
		try {
		List <String> rests = new ArrayList <String> ();
		String sql="select distinct r.rest_id , rest.capacity, zip.state from reservation r join restaurant rest on rest.id=r.rest_id join zip on rest.zip=zip.code";
		Connection con = dataSource.getConnection();
		Statement stmt = con.createStatement();
		ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				
				String rest= resultSet.getString("r.rest_id")+" "+ resultSet.getString("rest.capacity")+" "+resultSet.getString("zip.state");
			    
//			    String state = resultSet.getString("z.state");
			    
				rests.add(rest);
		
			}
		
			
		sql = "select r.visitors*(select sum(meal.price) from meal_reserv mr join meal on mr.meal_id=meal.id where mr.reserv_id=r.id) debit, r.visitors*( select sum(p.price) from meal_reserv mr join meal m on m.id=mr.meal_id join meal_product mp on mp.meal_id=m.id join product p on p.id=mp.prod_id where mr.reserv_id=r.id) credit  from reservation r where r.rest_id=?";
		con = dataSource.getConnection();
		PreparedStatement stmt1 = con.prepareStatement(sql);
		for(String rest: rests) {
			stmt1.setString(1, rest.split(" ", 1)[0]);  
			resultSet = stmt1.executeQuery();
			double deb=0, pro=0;
			while(resultSet.next()) {
				Double credit = resultSet.getDouble("credit");
			    Double debit = resultSet.getDouble("debit");
			    deb+=debit;
			    pro+=credit;
//			    String state = resultSet.getString("z.state");
			   
			}
			StatisticData item = new StatisticData(rest, deb, pro);
			//System.out.println(rest+" "+deb+" "+pro);
			stat.add(item);
			
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stat;
	}

}
