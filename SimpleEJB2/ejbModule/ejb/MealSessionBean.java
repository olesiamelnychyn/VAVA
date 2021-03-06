package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import objects.Meal;
import objects.Product;
import objects.Reservation;
import objects.Restaurant;
import objects.StatisticData;
import objects.Supplier;
import objects.Zip;

@Stateless(name = "MealSessionEJB")
public class MealSessionBean implements MealRemote{

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public Dictionary<Integer, Meal> searchMeal(Dictionary <String, String>  args) {
		Dictionary<Integer, Meal>result = new Hashtable <Integer, Meal>();
		System.out.print("here");
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
                Meal meal = new Meal(title, price, prep_time);
				result.put(id, meal);

			}
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Wrong input parameters for Meal search: "+args, e);
		}
		return result;
	}

	@Override
	public int addMeal(Dictionary <String, String> args) {
		Integer id=-1;
		Connection con;
		try {
			con = dataSource.getConnection();
			String sql = "INSERT INTO meal (title, price, prep_time) Values ("+ args.get("title")+", ?,"+args.get("prep_time")+")";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, args.get("price"));

	        System.out.print(preparedStatement);
	        preparedStatement.executeUpdate();

	        sql="SELECT MAX(id) FROM meal";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			
			while(resultSet.next()) {
				id = resultSet.getInt("MAX(id)");
				System.out.print("heer"+id);
			}
			if(args.get("ingr")!=null && args.get("ingr")!="") {
	        	String [] ingr = args.get("ingr").split(",");
	        	for (int i =0; i<ingr.length; i++) {
	        		if(ingr[i]!=null) {
	        			sql="INSERT INTO meal_product (meal_id, prod_id) VALUES(?,?)";
			        	preparedStatement = con.prepareStatement(sql);
			        	preparedStatement.setInt(1, id); 
			        	preparedStatement.setInt(2, Integer.valueOf(ingr[i]));
			        	preparedStatement.executeUpdate();
	        		}
	        	}
	        	
	        }
			
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Wrong input parameters for Meal, failed to insert "+ args, e);
			return -1;
		} 
		LogTest.LOGGER.log(Level.INFO, "Meal (id=="+id+") was succesfully inserted");
		return id;
	}

	@Override
	public void deleteMeal(int id) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Delete from meal_product where meal_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			sql="Delete from meal_reserv where meal_id=?";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			sql="Delete from meal where id=?";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to delete Meal (id=="+id+")", e);	
			return;
		}
		LogTest.LOGGER.log(Level.INFO, "Meal (id=="+id+") was succesfully deleted");

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
		            if(!k.equals("ingr") && !k.equals("id")) {
		            	String sql="Update meal set "+k+"="+args.get(k)+" where id=?";
//		            	System.out.println(sql);
		            	PreparedStatement preparedStatement = con.prepareStatement(sql);
		            	preparedStatement.setInt(1, Integer.valueOf(args.get("id")));
		            	preparedStatement.executeUpdate();
		            }
		        }
		        if(args.get("ingr")!=null && args.get("ingr")!="") {
		        	String sql="delete from meal_product where meal_id = ?";
		        	PreparedStatement preparedStatement = con.prepareStatement(sql);
		        	preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
		        	preparedStatement.executeUpdate();
		        	String [] ingr = args.get("ingr").split(",");
		        	for (int i =0; i<ingr.length; i++) {
		        		if(ingr[i]!=null) {
		        			sql="INSERT INTO meal_product (meal_id, prod_id) VALUES(?,?)";
				        	preparedStatement = con.prepareStatement(sql);
				        	preparedStatement.setInt(1, Integer.valueOf(args.get("id"))); 
				        	preparedStatement.setInt(2, Integer.valueOf(ingr[i]));
				        	preparedStatement.executeUpdate();
		        		}
		        	}
		        	
		        }
		    }
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to update Meal (id=="+args.get("id")+"), args:"+args, e);	
			return;
		}
		LogTest.LOGGER.log(Level.INFO, "Meal (id=="+args.get("id")+") was succesfully updated");
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
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get Restaurants for Meal "+id, e);
		}
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
				try{
					Integer r_id = resultSet.getInt("r.id");
					Integer rest_id = resultSet.getInt("rest_id");
					Integer visitors = resultSet.getInt("visitors");
					System.out.println(resultSet.getString("date_start"));
					LocalDateTime date_start= LocalDateTime.parse(resultSet.getString("date_start"), dateFormat);
					LocalDateTime date_end = LocalDateTime.parse(resultSet.getString("date_end"), dateFormat);         
					Reservation res = new Reservation(rest_id, date_start, date_end, visitors);
					reserv.put(r_id, res);
				}catch(java.time.format.DateTimeParseException | java.lang.NullPointerException ex) {
					continue;
				}
			}
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get Reservations for Meal "+id, e);
		}
		return reserv;
	}

	@Override
	public Dictionary<Integer, Product> getProdMeal(Integer id) {
		Dictionary<Integer, Product> prods = new Hashtable <Integer, Product>();
		String sql;
		ResultSet resultSet;
		try {
			Connection con = dataSource.getConnection();
			if(id==0) {
				sql="select p.id, p.title, p.price, p.supp_id from product p";
				Statement stmt = con.createStatement();
				resultSet = stmt.executeQuery(sql);
			}else {
				sql="select p.id, p.title, p.price, p.supp_id from product p join meal_product mp on mp.prod_id=p.id where mp.meal_id=?";
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setInt(1, id);
				resultSet = preparedStatement.executeQuery();
			}
			while(resultSet.next()) {
				Integer p_id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Double price = resultSet.getDouble("price");
                Integer supp_id = resultSet.getInt("supp_id");
                try {
					Context ctx;
					ctx = new InitialContext();
					SupplierRemote SupplierRemote = (SupplierRemote) ctx.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote");
					Supplier supp = SupplierRemote.getSupplierById(supp_id);
	                Product prod = new Product(title, price, supp);
					prods.put(p_id, prod);
                } catch (NamingException e) {				
					e.printStackTrace();
				}
			}
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get Products for Meal "+id, e);
		}
		return prods;
	}

	@Override
	public List<StatisticData> statMeal() {
List <StatisticData> stat = new ArrayList <StatisticData> ();
		
		try {
			List <String> meals = new ArrayList <String> ();
			List <Double> prices = new ArrayList <Double> ();
			String sql="select m.id , m.title, m.price from meal m";
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				String rest= resultSet.getString("m.id")+" "+ resultSet.getString("m.title");
				Double price = resultSet.getDouble("m.price");
				meals.add(rest);
				prices.add(price);
				
			}
		
			sql = "select sum(p.price) from meal_product mp join product p on mp.prod_id=p.id where mp.meal_id=?";
			con = dataSource.getConnection();
			PreparedStatement stmt1 = con.prepareStatement(sql);
			for (int i=0; i<meals.size(); i++) {
				stmt1.setString(1, meals.get(i).split(" ", 1)[0]);  
				resultSet = stmt1.executeQuery();
				if(resultSet.next()) {
					double deb=resultSet.getDouble("sum(p.price)");
				
				StatisticData item = new StatisticData(meals.get(i), deb*0.3, prices.get(i));
				stat.add(item);
				}
			}
			
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Failed to get statistics data for Meals ", e);
		}
		return stat;
	}

	@Override
	public byte[] getImage(int id) {
		System.out.print("here");
		byte[] imagebytes=null;
		try {
			String sql="select image from meal where id=?";
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				imagebytes = resultSet.getBytes("image");
			}
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Something is not correct with an image, failed to get image for Meal "+id, e);
		}
		return imagebytes;
	}

	@Override
	public void setImage(int id, byte[] img) {
		if(id!=0) {
		String sql="update meal set  image=?  where id=?";
		Connection con;
		try {
			con = dataSource.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setBytes(1, img);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
//			System.out.println("done");
			con.close();
		} catch (SQLException e) {
			LogTest.LOGGER.log(Level.SEVERE, "Something is not correct with an image, failed to insert image for Meal "+id, e);
			return;
		}
		LogTest.LOGGER.log(Level.INFO, "Image for Meal (id=="+id+") was succesfully inserted");
		}
	}	
}
		