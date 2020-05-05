package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import objects.Employee;
import objects.Meal;
import objects.Reservation;
import objects.Restaurant;
import objects.Zip;

@Stateless(name = "MealSessionEJB")
public class MealSessionBean implements MealRemote{

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public Dictionary<Integer, Meal> searchMeal(Dictionary <String, String>  args) {
		Dictionary<Integer, Meal>result = new Hashtable <Integer, Meal>();
		
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
			sql+=" order by m.id";
			//System.out.println(sql);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
			while(resultSet.next()) {
				Integer id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Double price = resultSet.getDouble("price");
                LocalTime prep_time = LocalTime.parse(resultSet.getString("prep_time"), dateFormat);
//				Dictionary <Integer, Meal> item = new Hashtable <Integer, Meal> ();
				Meal meal = new Meal(title, price, prep_time);
				result.put(id, meal);
//				//System.out.println(item);
//				result.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(result);
		return result;
	}

	@Override
	public int addMeal(Dictionary <String, String> args) {
		Integer id=-1;
		try {
			Connection con = dataSource.getConnection();
			String sql = "INSERT INTO meal (title, price, prep_time) Values ("+ args.get("title")+", ?, ?)";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
//	        preparedStatement.setString(1,);
	        preparedStatement.setString(1, args.get("price"));
	        preparedStatement.setString(2, args.get("prep_time"));
	        System.out.print(preparedStatement);
	        preparedStatement.executeUpdate();
//	        String image=args.get("image");
//	        if(image != null) {
//	        	//System.out.println("Will be implemented latter");
//	        }
	        sql="SELECT MAX(id) FROM meal";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			resultSet.next();
			
			while(resultSet.next()) {
				id = resultSet.getInt("id");
				System.out.print("heer"+id);
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
			
			if(args!=null){
				
				if(args.get("id").equals("0")) {
					System.out.println(args.get("id"));
					addMeal(args);
					return;
				}
		        Enumeration<String> e = args.keys();
		        while(e.hasMoreElements()) {
		            String k = e.nextElement();
		            if(k!="meal_reserv" && k!="meal_prod" && k!="meal_rest" && !k.equals("id")) {
		            	String sql="Update meal set "+k+"="+args.get(k)+" where id=?";
//		            	System.out.println(sql);
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
	public Dictionary<Integer, Restaurant> getRestMeal(Integer id){
		Dictionary<Integer, Restaurant> rests = new Hashtable <Integer, Restaurant>();
		try {
		Connection con = dataSource.getConnection();
		
		String sql;
		ResultSet resultSet;
		
		if(id==0) {
			sql="SELECT r.id, z.code, z.state, r.capacity FROM restaurant r join zip z on r.zip=z.code";
			Statement stmt = con.createStatement();
			resultSet = stmt.executeQuery(sql);
		}else {
			sql="SELECT r.id, z.code, z.state, r.capacity FROM restaurant r join meal_rest mr on mr.rest_id=r.id join zip z on r.zip=z.code where mr.meal_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
		}
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
	public Dictionary<Integer, Reservation> getReservMeal(Integer id) {
		Dictionary<Integer, Reservation> reserv = new Hashtable <Integer, Reservation>();
		String sql;
		ResultSet resultSet;
		try {
		Connection con = dataSource.getConnection();
		
		if(id==0) {
			sql="SELECT r.id,r.rest_id, r.date_start, r.date_end, r.visitors FROM reservation r";
			Statement stmt = con.createStatement();
			resultSet = stmt.executeQuery(sql);
		}else {
			sql="SELECT r.id, r.rest_id, r.date_start, r.date_end, r.visitors  FROM reservation r join meal_reserv mr on mr.reserv_id=r.id where mr.meal_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
		}
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
		while(resultSet.next()) {
			Integer r_id = resultSet.getInt("r.id");
            Integer rest_id = resultSet.getInt("rest_id");
            Integer visitors = resultSet.getInt("visitors");
            LocalDateTime date_start = LocalDateTime.parse(resultSet.getString("date_start"), dateFormat);
            LocalDateTime date_end = LocalDateTime.parse(resultSet.getString("date_end"), dateFormat);
			
			Reservation res = new Reservation(rest_id, date_start, date_end, visitors);
			reserv.put(r_id, res);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reserv;
	}

	@Override
	public Dictionary<Integer, Employee> getEmpMeal(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
