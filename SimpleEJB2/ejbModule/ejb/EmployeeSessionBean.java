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

import objects.Employee;

@Stateless(name = "EmployeeSessionEJB")
public class EmployeeSessionBean implements EmployeeRemote{

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public Dictionary<Integer, Employee> searchEmployee(Dictionary<String, String> args) {
		// TODO Auto-generated method stub
		return null;
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
			String sql="Update employee set ?=? where id = ?";
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
