package ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import objects.Supplier;

@Stateless(name = "SupplierSessionEJB")
public class SupplierSessionBean implements SupplierRemote{

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
    private DataSource dataSource;

	@Override
	public Dictionary<Integer, Supplier> searchSupplier(Dictionary<String, String> args) {
		Dictionary<Integer, Supplier>result = new Hashtable <Integer, Supplier>();
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select s.id, s.title, s.phone, s.e_mail from supplier s";
			if(args.get("title")!=null && args.get("title")!="") {
				sql+= " where s.title like \"%"+args.get("title")+"%\"";
			}
			System.out.println(sql);
			sql+=" order by s.id";
			
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				Integer id = resultSet.getInt("s.id");
                String title = resultSet.getString("s.title");
                String phone = resultSet.getString("s.phone");
                String e_mail = resultSet.getString("s.e_mail");
                Supplier supp = new Supplier(title, phone, e_mail);
				result.put(id, supp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	@Override
	public int addSupplier(Dictionary<String, String> args) {
		Integer id=-1;
		try {
			Connection con = dataSource.getConnection();
			String sql = "INSERT INTO supplier (title, phone, e_mail) Values (?, ?, ?)";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
	        preparedStatement.setString(1, args.get("title"));
	        preparedStatement.setString(2, args.get("phone"));
	        preparedStatement.setString(3, args.get("e_mail"));
	        System.out.print(preparedStatement);
	        preparedStatement.executeUpdate();
	        sql="SELECT MAX(id) FROM supplier";
	        Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			
			while(resultSet.next()) {
				id = resultSet.getInt("MAX(id)");
				System.out.print("here"+id);
			}
			
	        
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		return id;
	}

	@Override
	public void deleteSupplier(int id) {
		try {
			Connection con = dataSource.getConnection();
			String sql="select id from product where supp_id="+String.valueOf(id);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				sql="Delete from meal_product where prod_id=?";
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setInt(1, resultSet.getInt("id"));
				preparedStatement.executeUpdate();
				
				sql="Delete from product where id=?";
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setInt(1, resultSet.getInt("id"));
				preparedStatement.executeUpdate();
			}
//			try {
//				Context ctx;
//				ctx = new InitialContext();
//				ProductRemote ProductRemote = (ProductRemote) ctx.lookup("ejb:/SimpleEJB2//ProductSessionEJB!ejb.ProductRemote");
//				while(resultSet.next()) {
//					ProductRemote.deleteProduct(resultSet.getInt("id"));
//				}
//			} catch (NamingException e) {				
//				e.printStackTrace();
//			}
			
			
			sql="Delete from supplier where id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Supplier getSupplierById(int supp_id) {
		
		try {
			Connection con = dataSource.getConnection();
			String sql="select s.id, s.title, s.phone, s.e_mail from supplier s";
			sql+= " where s.id=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, supp_id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
                String title = resultSet.getString("title");
                String phone = resultSet.getString("phone");
                String e_mail = resultSet.getString("e_mail");
                Supplier supp = new Supplier(title, phone, e_mail);
				return supp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(result);
		return null;
	}
	
}
