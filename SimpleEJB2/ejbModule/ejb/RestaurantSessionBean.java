package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import objects.Employee;
import objects.Meal;
import objects.Restaurant;
import objects.StatisticData;
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
			if(args!=null && args.get("zip")!="") {
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
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(result);
		return result;
	}

	@Override
	public int addRestaurant(Dictionary<String, String> args) {
		Integer id=-1;
		if(args!=null) {
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
				
				while(resultSet.next()) {
					id = resultSet.getInt("MAX(id)");
				}
				System.out.println("Id new: "+id);
				if(args.get("staff")!=null && args.get("staff")!="") {
		        	String [] staff = args.get("staff").split(",");
		        	for (int i =0; i<staff.length; i++) {
		        		if(staff[i]!=null) {
		        			sql="Update employee set rest_id=? where id=?";
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
		        		sql="INSERT INTO meal_rest (rest_id, meal_id) VALUES(?,?)";
		        		preparedStatement = con.prepareStatement(sql);
		        		preparedStatement.setInt(1,id); 
		        		preparedStatement.setInt(2, Integer.valueOf(menu[i]));
		        		preparedStatement.executeUpdate();
		        		}
		        	}
		        }
		        con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
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
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateRestaurant(Dictionary<String, String> args) {
		try {
			System.out.println(args.get("menu")+" "+ args.get("id"));
			if(args.get("id").equals("0")) {
				addRestaurant(args);
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
		            	sql="Update restaurant set "+k+"="+args.get(k)+" where id = ?";
		            	preparedStatement = con.prepareStatement(sql);
		    			preparedStatement.setInt(1, Integer.valueOf(args.get("id")));
		            	preparedStatement.executeUpdate();
		            	
		            }
		        } 
		        if(args.get("staff")!=null && args.get("staff")!="") {		
					sql="Update employee set rest_id=null where rest_id=?";
					preparedStatement = con.prepareStatement(sql);
					preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
					preparedStatement.executeUpdate();
		     
		        	String [] staff = args.get("staff").split(",");
		        	for (int i =0; i<staff.length; i++) {
		        		if(staff[i]!=null) {
		        			sql="Update employee set rest_id=? where id=?";
				        	preparedStatement = con.prepareStatement(sql);
				        	preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
				        	preparedStatement.setInt(2, Integer.valueOf(staff[i]));
				        	preparedStatement.executeUpdate();
		        		}
		        	}
		        	
		        }
					
		        
		        if(args.get("menu")!=null && args.get("menu")!="") {
		        	
		        	sql="delete from meal_rest where rest_id = ?";
		        	preparedStatement = con.prepareStatement(sql);
		        	preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
		        	preparedStatement.executeUpdate();
		        	String [] menu = args.get("menu").split(",");
		        	for (int i =0; i<menu.length; i++) {
		        		if(menu[i] !=null) {
		        		sql="INSERT INTO meal_rest (rest_id, meal_id) VALUES(?,?)";
		        		preparedStatement = con.prepareStatement(sql);
		        		preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
		        		preparedStatement.setInt(2, Integer.valueOf(menu[i]));
		        		preparedStatement.executeUpdate();
		        		}
		        	}
		        }
			}
			con.close();
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
			con.close();
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
			
			while(resultSet.next()) {
				capacity = resultSet.getInt("MAX(capacity)");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return capacity;
	}
	
	@Override
	public Dictionary<Integer, Meal> getMealRest(Integer id) {
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
				sql="SELECT  m.id, m.title,  m.prep_time, m.price FROM meal m join meal_rest mr on mr.meal_id=m.id where mr.rest_id=?";
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
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meals;
	}

	@Override
	public Dictionary<Integer, Employee> getEmpRest(Integer id) {
		Dictionary<Integer, Employee> emps = new Hashtable <Integer, Employee>();
		String sql;
		ResultSet resultSet;
		try {
			Connection con = dataSource.getConnection();
			
			if(id==0) {
				sql="SELECT e.id, e.rest_id, e.first_name, e.last_name, e.position, e.wage, e.gender, e.birthdate, e.e_mail, e.phone from employee e";
				PreparedStatement preparedStatement = con.prepareStatement(sql);
	//			preparedStatement.setInt(1, id);
				resultSet = preparedStatement.executeQuery();
			}else {
				sql="SELECT  e.id, e.rest_id, e.first_name, e.last_name, e.position, e.wage, e.gender, e.birthdate, e.e_mail, e.phone from employee e where e.rest_id=?";
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
	            Employee emp = new Employee(rest_id, first_name, last_name, gender, birthdate, phone, e_mail, position, wage);
				emps.put(e_id, emp);
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emps;
	}

	@Override
	public void addCheque(Dictionary<String, String> args) {
		if(args!=null){
			if(args.get("id").equals("0")) {
				return;
			}
			try {
				Connection con;
				con = dataSource.getConnection();
			
				PreparedStatement preparedStatement;
				String sql;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(args.get("menu")!=null && args.get("menu")!="") {
		        	String [] menu = args.get("menu").split(",");
		        	for (int i =0; i<menu.length; i++) {
		        		if(menu[i] !=null) {
		        			Date date = new Date();  
		        		sql="INSERT INTO cheque (rest_id, meal_id, amount, date) VALUES(?,?,1,\""+formatter.format(date)+"\")";
		        		preparedStatement = con.prepareStatement(sql);
		        		preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
		        		preparedStatement.setInt(2, Integer.valueOf(menu[i]));
		        		preparedStatement.executeUpdate();
		        		}
		        	}
		        }
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public List<StatisticData> statRest() {
		List <StatisticData> stat = new ArrayList <StatisticData> ();
		try {
			List <String> rests = new ArrayList <String> ();
			String sql="select r.id, r.capacity, zip.state from restaurant r join zip on r.zip=zip.code";
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				String rest= resultSet.getString("r.id")+" "+ resultSet.getString("r.capacity")+" "+resultSet.getString("zip.state");
				rests.add(rest);
		
			}
		
			
			sql = "select c.rest_id, sum(m.price)*c.amount as debit, sum(p.price)*c.amount as credit from cheque  c join meal m on m.id=c.meal_id join meal_product mp on mp.meal_id=m.id join product p on p.id=mp.prod_id group by c.rest_id order by c.rest_id";
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
			   
				}
			StatisticData item = new StatisticData(rest, deb, pro);
			//System.out.println(rest+" "+deb+" "+pro);
			stat.add(item);
			
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stat;
	}

}
