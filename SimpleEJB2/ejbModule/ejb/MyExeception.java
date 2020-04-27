package ejb;

public class MyExeception extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyExeception(String message) {
        super(message);
    }
}