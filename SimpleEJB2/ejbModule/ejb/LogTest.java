package ejb;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogTest {

	
	private static final LogManager logManager = LogManager.getLogManager();
	public static final Logger LOGGER = Logger.getLogger("confLogger");
	static{
        try {
            logManager.readConfiguration(new FileInputStream("./logging.properties"));
            System.out.print("yes");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error in loading configuration",exception);
            System.out.print("no");
        }
    }
	public static void main() {
		
		howToLogException();
	}

	private static void howToLogException() {
		try {
			long l = 1/0;
			System.out.print(l);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Vypocet nedopadol dobre", e);
			
		}
	}

}
