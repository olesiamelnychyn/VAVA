package ejb;

import javax.annotation.Resource;
import javax.sql.DataSource;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//
//
//import org.hibernate.Session;
//import org.hibernate.query.Query;



public class Main {

	@Resource(lookup = "java:jboss/datasources/mysqlDS")
	private static DataSource dataSource;
//	@Resource(lookup = "java:jboss/datasources/mysqlDS")
//    private DataSource dataSource;
	
	public static void main(String[] args) {
//		List la = new ArrayList();
////		
//		JDBCUtil util = JDBCUtil.getInstance();
//		util.getConnection();
////		
//		la = util.executeQuery("select * from meal");
//		System.out.print(la);
//		List <List<String>> result = new ArrayList <List<String>>();
//		try {
//			Connection con = dataSource.getConnection();
//			Statement stmt = con.createStatement();
//			ResultSet resultSet = stmt.executeQuery("select * from meal limit 10");
//			
//			if(resultSet.next()) {
//				List<String> item = new ArrayList <String>();
//				
////				System.out.println(resultSet.getObject(1));
//				item.add(String.valueOf(resultSet.getObject(1)));
//
////				System.out.println(resultSet.getObject(2));
//				item.add(String.valueOf(resultSet.getObject(2)));
//				
//				result.add(item);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		JDBCUtil util=new JDBCUtil();
//		util.getConnection();
//		Connection connection = null;
		
		
//        }
//		util.executeQuery("Select * from meal ");
		
	
//	}
		
		
//		try (Session session = HibernateUtil.getSession()){
//			session.beginTransaction();
//			Zip zip = new Zip();
//			zip.setCode("1234567890");
//			zip.setState("UA");
//			
//			session.save(zip);
//			
//			Zip zip2 = new Zip();
//			zip2.setCode("1234567980");
//			zip2.setState("UR");
//			session.save(zip2);
//			
//			session.getTransaction().commit();
//		} catch (Throwable cause) {
//			cause.printStackTrace();
//		}
//		
//		List <Zip> list = null;
//		
//		try (Session session = HibernateUtil.getSession()){
//			session.beginTransaction();
//			Query query = session.createQuery("From zip");
//			list = (List <Zip> ) query.list();
//			
//			session.getTransaction().commit();
//		}catch (Throwable cause) {
//			cause.printStackTrace();
//		}
//		if (list!=null && !list.isEmpty()) {
//			for (Zip zip : list) {
//				System.out.println(zip.getCode()+" "+zip.getState());
//			}
//		}
//	}
}
}
