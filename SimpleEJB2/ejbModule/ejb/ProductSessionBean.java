package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import objects.Meal;
import objects.Product;
import objects.Reservation;
import objects.Supplier;

@Stateless(name = "ProductSessionEJB")
public class ProductSessionBean implements ProductRemote {

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;
	
	@Override
	public Dictionary<Integer, Product> searchProduct(Dictionary<String, String> args) {
		Dictionary<Integer, Product>result = new Hashtable <Integer, Product>();
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select p.id, p.title, p.price, p.supp_id from product p";
			if(args!=null && args.get("supp_id")!="") {
				sql+=" join supplier s on s.id=p.supp_id where p.supp_id="+args.get("supp_id")+
						" and p.price between "+args.get("price_from")+" and "+args.get("price_to");
			} else {
				sql+=" where p.price between "+args.get("price_from")+" and "+args.get("price_to");
			}
			if(args!=null && args.get("title")!="") {
				sql+= " and p.title like \"%"+args.get("title")+"%\"";
			}
			sql+=" order by p.id";
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				Integer id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Double price = resultSet.getDouble("price");
                Integer supp_id = resultSet.getInt("supp_id");
				sql="select s.id, s.title, s.phone, s.e_mail from supplier s where s.id=" + supp_id;
				stmt = con.createStatement();
				ResultSet resultSet1 = stmt.executeQuery(sql);
				while(resultSet1.next()) {
				    String title_s = resultSet1.getString("title");
				    String phone_s = resultSet1.getString("phone");
				    String e_mail_s = resultSet1.getString("e_mail");
				    Supplier supp = new Supplier(title_s, phone_s, e_mail_s);
				    Product prod = new Product(title, price, supp);
					result.put(id, prod);
				}
//					Context ctx;
//					ctx = new InitialContext();
//					SupplierRemote SupplierRemote = (SupplierRemote) ctx.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote");
//					Supplier supp = SupplierRemote.getSupplierById(supp_id);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int addProduct(Dictionary<String, String> args) {
		Integer id=-1;
		try {
			Connection con = dataSource.getConnection();
			String sql = "INSERT INTO product (title, price, supp_id) VALUES ("+args.get("title")+","+args.get("price")+","+ args.get("supp_id")+")";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.executeUpdate();
	        
	        sql="SELECT MAX(id) FROM product";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			
			while(resultSet.next()) {
				id = resultSet.getInt("MAX(id)");
			}
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		return id;
	}

	@Override
	public void deleteProduct(int id) {
		try {
			Connection con = dataSource.getConnection();
			String sql="Delete from meal_product where prod_id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			
			sql="Delete from product where id=?";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateProduct(Dictionary<String, String> args) {
		try {
			Connection con = dataSource.getConnection();
			
			if(args!=null){
				if(args.get("id").equals("0")) {
					System.out.println(args.get("id"));
					addProduct(args);
					return;
				}
		        Enumeration<String> e = args.keys();
		        while(e.hasMoreElements()) {
		            String k = e.nextElement();
		            if(!k.equals("id")) {
		            	String sql="Update product set "+k+"="+args.get(k)+" where id = ?";
		    			PreparedStatement preparedStatement = con.prepareStatement(sql);
		    			preparedStatement.setInt(1, Integer.valueOf(args.get("id")));
		            	preparedStatement.executeUpdate();
		            }
		        }
		    }
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Dictionary<Integer, Meal> getMealProduct(Integer id) {
		Dictionary<Integer, Meal> meals = new Hashtable <Integer, Meal>();
		String sql;
		ResultSet resultSet;
		try {
			Connection con = dataSource.getConnection();
			if(id!=0) {
				sql="select m.id, m.title, m.price, m.prep_time from meal m join meal_product mp on mp.meal_id=m.id where mp.prod_id=?";
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setInt(1, id);
				resultSet = preparedStatement.executeQuery();
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("H:mm:ss");
				while(resultSet.next()) {
					Integer m_id = resultSet.getInt("id");
	                String title = resultSet.getString("title");
	                Double price = resultSet.getDouble("price");
	                LocalTime prep_time = LocalTime.parse(resultSet.getString("prep_time"), dateFormat);
		            Meal meal = new Meal(title, price, prep_time);
		            meals.put(m_id, meal);           
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return meals;
	}

	@Override
	public double getMaxPrice() {
		double price = 0;
		try {
			Connection con = dataSource.getConnection();
			String sql="SELECT MAX(price) FROM product";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			
			while(resultSet.next()) {
				price = resultSet.getInt("MAX(price)");
				System.out.print("here "+price);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return price;
	}

	@Override
	public Dictionary<Integer, Reservation> getReservProduct(Integer id) {
		Dictionary<Integer, Meal> meals = getMealProduct(id);
		Dictionary<Integer, Reservation> rese = new Hashtable <Integer, Reservation>();
		try {
			Context ctx;
			ctx = new InitialContext();
			MealRemote MealRemote = (MealRemote) ctx.lookup("ejb:/SimpleEJB2//MealSessionEJB!ejb.MealRemote");
			Enumeration<Integer> enam = meals.keys();
            while(enam.hasMoreElements()) {
				Dictionary<Integer, Reservation> reserv = MealRemote.getReservMeal(enam.nextElement());
				Enumeration<Integer> enam2 = reserv.keys();
				while(enam2.hasMoreElements()) {
					int k = enam2.nextElement();
					rese.put(k, reserv.get(k));
				}
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rese;
	}
	
	
}
