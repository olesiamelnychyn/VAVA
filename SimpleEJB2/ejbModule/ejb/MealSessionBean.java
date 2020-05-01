package ejb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

@Stateless(name = "MealSessionEJB")
public class MealSessionBean implements MealRemote{

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public List<List<String>> searchMeal(Dictionary <String, String>  args) {
		List <List<String>> result = new ArrayList <List<String>>();
		
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery("select * from meal limit 1");
			
			if(resultSet.next()) {
				List<String> item = new ArrayList <String>();
				
				System.out.println(resultSet.getObject(1));
				item.add(String.valueOf(resultSet.getObject(1)));

				System.out.println(resultSet.getObject(2));
				item.add(String.valueOf(resultSet.getObject(2)));
				
				result.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public int addMeal(Dictionary <String, String> args) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteMeal(int id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateMeal(Dictionary <String, String>  args) {
		// TODO Auto-generated method stub

	}

}
