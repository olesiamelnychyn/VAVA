package ejb;
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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List la = new ArrayList();
//		
		JDBCUtil util = JDBCUtil.getInstance();
		util.getConnection();
//		
		la = util.executeQuery("select * from meal");
		System.out.print(la);
		
//		JDBCUtil util=new JDBCUtil();
//		util.getConnection();
//		Connection connection = null;
//        PreparedStatement statement = null;
//        FileInputStream inputStream = null;
// 
//        try {
//            File image = new File("../img/caesar.jpg");
//            inputStream = new FileInputStream(image);
// 
//            connection = util.getConnection();
//            statement = connection.prepareStatement("insert into meal(image) " + "values(?)");
//            statement.setBinaryStream(1, (InputStream) inputStream, (int)(image.length()));
// 
//            statement.executeUpdate();
// 
//        } catch (FileNotFoundException e) {
//            System.out.println("FileNotFoundException: - " + e);
//        } catch (SQLException e) {
//            System.out.println("SQLException: - " + e);
//        } finally {
//        	 try {
//                 connection.close();
//                 statement.close();
//             } catch (SQLException e) {
//                 System.out.println("SQLException Finally: - " + e);
//             }
//         }
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
