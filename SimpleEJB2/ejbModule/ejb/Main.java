package ejb;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.query.Query;

public class Main {

	public static void main(String[] args) {
		JDBCUtil util=new JDBCUtil();
		util.getConnection();
		util.executeQuery("Select * from product limit 20");
	
	}
		
		
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
