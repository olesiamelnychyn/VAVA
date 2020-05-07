package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import objects.Employee;
import objects.Meal;
import objects.Reservation;
import objects.Restaurant;
import objects.StatisticData;
import objects.Zip;

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
//			resultSet.next();
			
			while(resultSet.next()) {
				id = resultSet.getInt("MAX(id)");
			}
			System.out.println("Id new: "+id);
			if(args.get("staff")!=null && args.get("staff")!="") {
	        	String [] staff = args.get("staff").split(",");
	        	for (int i =0; i<staff.length; i++) {
	        		if(staff[i]!=null) {
	        			sql="INSERT INTO emp_reserv (reserv_id, emp_id) VALUES(?,?)";
			        	preparedStatement = con.prepareStatement(sql);
			        	preparedStatement.setInt(1, id); 
			        	preparedStatement.setInt(2, Integer.valueOf(staff[i]));
			        	preparedStatement.executeUpdate();
	        		}
	        	}
	        	
	        }
				
	        
	        if(args.get("menu")!=null && args.get("menu")!="") {
	        	String [] menu = args.get("menu").split(",");
	        	for (int i =0; i<menu.length; i++) {
	        		if(menu[i] !=null) {
	        		sql="INSERT INTO meal_reserv (reserv_id, meal_id) VALUES(?,?)";
	        		preparedStatement = con.prepareStatement(sql);
	        		preparedStatement.setInt(1,id); 
	        		preparedStatement.setInt(2, Integer.valueOf(menu[i]));
	        		preparedStatement.executeUpdate();
	        		}
	        	}
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
			
			System.out.println(args.get("menu")+" "+ args.get("id"));
			if(args.get("id").equals("0")) {
				addReserv(args);
				return;
			}
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement;
			String sql;
			if(args!=null){
		        Enumeration<String> e = args.keys();
		        while(e.hasMoreElements()) {
		            String k = e.nextElement();
		            if (!k.equals("staff") &&  !k.equals("menu")) {
		            	sql="Update reservation set "+k+"="+args.get(k)+" where id = ?";
		            	preparedStatement = con.prepareStatement(sql);
		    			preparedStatement.setInt(1, Integer.valueOf(args.get("id")));
		            	preparedStatement.executeUpdate();
		            	
		            }
		        } 
		        if(args.get("staff")!=null && args.get("staff")!="") {
		        	sql="delete from emp_reserv where reserv_id = ?";
		        	preparedStatement = con.prepareStatement(sql);
		        	preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
		        	preparedStatement.executeUpdate();
		        	String [] staff = args.get("staff").split(",");
		        	for (int i =0; i<staff.length; i++) {
		        		if(staff[i]!=null) {
		        			sql="INSERT INTO emp_reserv (reserv_id, emp_id) VALUES(?,?)";
				        	preparedStatement = con.prepareStatement(sql);
				        	preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
				        	preparedStatement.setInt(2, Integer.valueOf(staff[i]));
				        	preparedStatement.executeUpdate();
		        		}
		        	}
		        	
		        }
					
		        
		        if(args.get("menu")!=null && args.get("menu")!="") {
		        	
		        	sql="delete from meal_reserv where reserv_id = ?";
		        	preparedStatement = con.prepareStatement(sql);
		        	preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
		        	preparedStatement.executeUpdate();
		        	String [] menu = args.get("menu").split(",");
		        	for (int i =0; i<menu.length; i++) {
		        		if(menu[i] !=null) {
		        		sql="INSERT INTO meal_reserv (reserv_id, meal_id) VALUES(?,?)";
		        		preparedStatement = con.prepareStatement(sql);
		        		preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
		        		preparedStatement.setInt(2, Integer.valueOf(menu[i]));
		        		preparedStatement.executeUpdate();
		        		}
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

	@Override
	public Dictionary<Integer, Restaurant> getRestReserv(Integer id) throws SQLException {
		Dictionary<Integer, Restaurant> rests = new Hashtable <Integer, Restaurant>();
		try {
		Connection con = dataSource.getConnection();
		
		String sql;
		ResultSet resultSet;
		
		sql="SELECT r.id, z.code, z.state, r.capacity FROM restaurant r join zip z on r.zip=z.code order by r.id desc";
		Statement stmt = con.createStatement();
		resultSet = stmt.executeQuery(sql);
		
		while(resultSet.next()) {
			Integer r_id = resultSet.getInt("r.id");
            String code = resultSet.getString("z.code");
            String state = resultSet.getString("z.state");
            Integer r_cap = resultSet.getInt("r.capacity");
            Zip zip = new Zip(code, state);
            Restaurant rest = new Restaurant(zip, r_cap);
			rests.put(r_id, rest);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(rests);
		return rests;
	}

	@Override
	public Dictionary<Integer, Meal> getMealReserv(Integer id) {
		Dictionary<Integer, Meal> meals = new Hashtable <Integer, Meal>();
		String sql;
		ResultSet resultSet;
		try {
		Connection con = dataSource.getConnection();
		
		if(id==0) {
			sql="SELECT m.id, m.title, m.prep_time, m.price FROM meal m";
			Statement stmt = con.createStatement();
			resultSet = stmt.executeQuery(sql);
		}else {
			sql="SELECT  m.id, m.title,  m.prep_time, m.price FROM meal m join meal_reserv mr on mr.meal_id=m.id where mr.reserv_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
		}
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
		while(resultSet.next()) {
			Integer r_id = resultSet.getInt("m.id");
			String title = resultSet.getString("m.title");
			Double price = resultSet.getDouble("m.price");
            LocalTime prep_time = LocalTime.parse(resultSet.getString("m.prep_time"), dateFormat);
            
            Meal meal = new Meal(title, price, prep_time);
			meals.put(r_id, meal);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meals;
	}

	@Override
	public Dictionary<Integer, Employee> getEmpReserv(Integer id) {
		Dictionary<Integer, Employee> emps = new Hashtable <Integer, Employee>();
		String sql;
		ResultSet resultSet;
		try {
		Connection con = dataSource.getConnection();
		
		if(id==0) {
			sql="SELECT e.id, e.rest_id, e.first_name, e.last_name, e.position, e.wage, e.gender, e.birthdate, e.e_mail, e.phone from employee e";
			Statement stmt = con.createStatement();
			resultSet = stmt.executeQuery(sql);
		}else {
			sql="SELECT  e.id, e.rest_id, e.first_name, e.last_name, e.position, e.wage, e.gender, e.birthdate, e.e_mail, e.phone from employee e join emp_reserv er on e.id=er.emp_id where er.reserv_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
		}

		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		while(resultSet.next()) {
			Integer e_id = resultSet.getInt("id");
			Integer rest_id = resultSet.getInt("rest_id");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String gender = resultSet.getString("gender");
            LocalDate birthdate = LocalDate.parse(resultSet.getString("birthdate"), dateFormat);
            String position = resultSet.getString("position");
            String phone = resultSet.getString("phone");
            String e_mail = resultSet.getString("e_mail");
            Double wage = resultSet.getDouble("wage");
            Employee emp = new Employee(rest_id, first_name, last_name, gender, birthdate, position, phone, e_mail, wage);
			emps.put(e_id, emp);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emps;
	}
	

}
