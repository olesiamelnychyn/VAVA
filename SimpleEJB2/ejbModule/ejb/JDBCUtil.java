package ejb;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtil {
	
	private static JDBCUtil util_instance;
	private String URL = "jdbc:mysql://localhost:3306/rest_chain_vava?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", user="manager", password="123456";
	Connection connection;
    String className="com.mysql.cj.jdbc.Driver";
	
	private JDBCUtil(){ 
		this.connection = null;
		
    }
    
	public static JDBCUtil  getInstance(){ 
        if (util_instance == null) 
        	util_instance  = new JDBCUtil (); 
  
        return util_instance; 
    } 
	
    
//    public JDBCUtil() {
//////        this.className = className;
////        this.URL = URL;
////        this.user = user;
////        this.password = password;
//        
//    }
    public Connection getConnection() {
//        //Load the driver class
        try {
            Class.forName(className);
        } catch (ClassNotFoundException ex) {
        	try {
  		    FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write("Unable to load the class. Terminating the program");
            myWriter.close();
        	} catch (IOException e) {
  		      System.out.println("An error occurred.");
  		      e.printStackTrace();
  		    }
            System.exit(-1);
        }
        	
        //get the connection
        try {
            connection = DriverManager.getConnection(URL, user, password);
//            return connection;
        } catch (SQLException ex) {
            System.out.println("Error getting connection: " + ex.getMessage());
            System.exit(-1);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }
		return connection;
        
    }
    
    public List<List <String>> executeQuery(String query)
    
    {
    	
    	List<List<String>> whole = new ArrayList <List<String>>();
        ResultSet resultSet = null;
        try
        {
            //executing query
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            //Get Number of columns
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnsNumber = metaData.getColumnCount();
            //Printing the results
            
            while(resultSet.next())
            {
            	List<String> inner = new ArrayList <String>();
            	for(int i = 1; i <= columnsNumber; i++)
                {
                    System.out.printf("%-25s", (resultSet.getObject(i) != null)?resultSet.getObject(i).toString(): null );
                    inner.add((resultSet.getObject(i) != null)?resultSet.getObject(i).toString(): null);
                }
                System.out.printf("\n");
                whole.add(inner);
            }
        }
        catch (SQLException ex)
        {
            System.out.println("Exception while executing statement. Terminating program... " + ex.getMessage());
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
            System.out.println("General exception while executing query. Terminating the program..." + ex.getMessage());
        }
        return whole;
    }
    
    
    
}