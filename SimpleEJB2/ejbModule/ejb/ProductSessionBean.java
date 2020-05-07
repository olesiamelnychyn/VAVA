package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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
			if(args.get("supp_id")!="") {
				sql+=" join supplier s on s.id=p.supp_id where p.supp_id="+args.get("supp_id")+
						" and p.price between "+args.get("price_from")+" and "+args.get("price_to");
			} else {
				sql+=" where p.price between "+args.get("price_from")+" and "+args.get("price_to");
			}
			if(args.get("title")!="") {
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
				try {
					Context ctx;
					ctx = new InitialContext();
					SupplierRemote SupplierRemote = (SupplierRemote) ctx.lookup("ejb:/SimpleEJB2//SupplierSessionEJB!ejb.SupplierRemote");
					Supplier supp = SupplierRemote.getSupplierById(supp_id);
	                Product prod = new Product(title, price, supp);
					result.put(id, prod);
				} catch (NamingException e) {				
					e.printStackTrace();
				}
			}
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
			String sql = "INSERT INTO product (title, price, supp_id) VALUES (?,?,?)";
	        PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, args.get("title"));
	        preparedStatement.setString(2, args.get("price"));
	        preparedStatement.setString(3, args.get("supp_id"));
	        preparedStatement.executeUpdate();
	        
	        sql="SELECT MAX(id) FROM product";
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
				String sql="Update product set ?=? where id = ?";
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
				
		    }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Dictionary<Integer, Reservation> getMealProduct(Integer id) {
		// TODO Auto-generated method stub
		return null;
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return price;
	}
	
	
}
