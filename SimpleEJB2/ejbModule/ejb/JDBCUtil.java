package ejb;

import java.sql.*;

public class JDBCUtil {
	
//    String className="com.mysql.jdbc.Driver";
    String URL ="jdbc:mysql://localhost:3306/rest_chain_vava?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", user="manager", password="123456";
    
    Connection connection;
    public JDBCUtil() {
////        this.className = className;
//        this.URL = URL;
//        this.user = user;
//        this.password = password;
        this.connection = null;
    }
    public Connection getConnection() {
//        //Load the driver class
//        try {
//            Class.forName(className);
//        } catch (ClassNotFoundException ex) {
//            System.out.println("Unable to load the class. Terminating the program");
//            System.exit(-1);
//        }
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
    
    public void executeQuery(String query)
    
    {
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
                for(int i = 1; i <= columnsNumber; i++)
                {
                    System.out.printf("%-25s", (resultSet.getObject(i) != null)?resultSet.getObject(i).toString(): null );
                }
                System.out.printf("\n");
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
    }
}