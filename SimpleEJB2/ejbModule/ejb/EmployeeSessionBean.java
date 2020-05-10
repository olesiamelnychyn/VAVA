package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import objects.Employee;
import objects.Reservation;
import objects.Restaurant;
import objects.Zip;

@Stateless(name = "EmployeeSessionEJB")
public class EmployeeSessionBean implements EmployeeRemote{

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public Dictionary<Integer, Employee> searchEmployee(Dictionary<String, String> args) {
		Dictionary<Integer, Employee>result = new Hashtable <Integer, Employee>();
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select e.id, e.rest_id, e.first_name, e.last_name, e.position, e.wage, e.gender, e.birthdate, e.e_mail, e.phone from employee e";
			sql+=" where e.wage between "+args.get("wage_from")+" and "+args.get("wage_to");
			if(args.get("name")!="") {
				sql+=" and(e.first_name like \"%"+args.get("name")+"%\" or e.last_name like \"%"+args.get("name")+"%\")";
			}
			if(args.get("position")!="") {
				sql+=" and e.position = \""+args.get("position")+"\"";
			}
			sql+=" order by e.id";
			
				
			
			System.out.println(sql);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			while(resultSet.next()) {
				Integer id = resultSet.getInt("id");
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
				result.put(id, emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	@Override
	public int addEmployee(Dictionary<String, String> args) {
		Integer id=-1;
		try {
			Connection con = dataSource.getConnection();
			String sql = "INSERT INTO employee (rest_id, first_name, last_name, gender, birthdate, phone, e_mail, position, wage) VALUES (?,?,?,?,?,?,?,?,?)";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, args.get("rest_id"));
	        preparedStatement.setString(2, args.get("first_name"));
	        preparedStatement.setString(3, args.get("last_name"));
	        preparedStatement.setString(4, args.get("gender"));
	        preparedStatement.setString(5, args.get("birthdate"));
	        preparedStatement.setString(6, args.get("phone"));
	        preparedStatement.setString(7, args.get("e_mail"));
	        preparedStatement.setString(8, args.get("position"));
	        preparedStatement.setString(9, args.get("wage"));
	        preparedStatement.executeUpdate();
	        
	        sql="SELECT MAX(id) FROM employee";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			
			while(resultSet.next()) {
				id = resultSet.getInt("MAX(id)");
			}
			
	        
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		return id;
	}

	@Override
	public void deleteEmployee(int id) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Delete from emp_reserv where emp_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			
			sql="Delete from employee where id=?";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateEmployee(Dictionary<String, String> args) {
		try {
			Connection con = dataSource.getConnection();
			
			if(args!=null){
		        Enumeration<String> e = args.keys();
		        while(e.hasMoreElements()) {
		            String k = e.nextElement();
		            if(!k.equals("id")) {
		            	String sql="Update employee set "+k+"="+args.get(k)+" where id = ?";
		    			PreparedStatement preparedStatement = con.prepareStatement(sql);
		    			preparedStatement.setInt(1, Integer.valueOf(args.get("id")));
		            	preparedStatement.executeUpdate();
		            }
		        }
		    }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public int getMaxWage() {
		int wage = 0;
		try {
			Connection con = dataSource.getConnection();
			String sql="SELECT MAX(wage) FROM employee";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			
			while(resultSet.next()) {
				wage = resultSet.getInt("MAX(wage)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return wage;
		
	}

	@Override
	public ArrayList<String> getPositions() {
		ArrayList<String>positions = new ArrayList<String>();
		try {
			Connection con = dataSource.getConnection();
			String sql="select distinct position from employee";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			resultSet.next();
			
			while(resultSet.next()) {
				positions.add(resultSet.getString("position"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return positions;
	}

	@Override
	public Dictionary<Integer, Restaurant> getRest(Integer id) {
		Dictionary<Integer, Restaurant> rests = new Hashtable <Integer, Restaurant>();
		try {
			Connection con = dataSource.getConnection();
			
			String sql;
			ResultSet resultSet;
			
			if(id!=-1) {
				sql="SELECT r.id, z.code, z.state, r.capacity FROM restaurant r join zip z on r.zip=z.code where r.id="+id;
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
				sql="SELECT r.id, z.code, z.state, r.capacity FROM restaurant r join zip z on r.zip=z.code where r.id!="+id;
			}
			else {
				sql="SELECT r.id, z.code, z.state, r.capacity FROM restaurant r join zip z on r.zip=z.code order by r.id desc";
			}
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
	public Dictionary<Integer, Reservation> getEmpReserv(Integer id) {
		Dictionary<Integer, Reservation> resev = new Hashtable <Integer, Reservation>();
		String sql;
		ResultSet resultSet;
		try {
			Connection con = dataSource.getConnection();
			
			
			sql="SELECT  r.id, r.date_start, r.date_end, r.visitors, r.rest_id from reservation r join emp_reserv er on r.id=er.reserv_id where er.emp_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
			while(resultSet.next()) {
				Integer res_id = resultSet.getInt("r.id");
	            Integer rest_id = resultSet.getInt("r.rest_id");
	            Integer visitors = resultSet.getInt("r.visitors");
	            LocalDateTime date_start = LocalDateTime.parse(resultSet.getString("r.date_start"), dateFormat);
	            LocalDateTime date_end = LocalDateTime.parse(resultSet.getString("r.date_end"), dateFormat);
				
				Reservation res = new Reservation(rest_id, date_start, date_end, visitors);
				resev.put(res_id, res);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resev;
	}

	
}
