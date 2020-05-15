package ejb;

import java.util.Dictionary;
import java.util.ArrayList;

import javax.ejb.Remote;
import objects.Employee;
import objects.Reservation;
import objects.Restaurant;


@Remote
public interface EmployeeRemote {

	
	Dictionary<Integer, Employee> searchEmployee(Dictionary<String, String> args);
	int addEmployee(Dictionary <String, String>  args);
	void deleteEmployee(int id);
	void updateEmployee(Dictionary <String, String>  args);
	int getMaxWage();
	ArrayList<String> getPositions();
	public Dictionary<Integer, Restaurant> getRest(Integer id);
	public Dictionary<Integer, Reservation> getEmpReserv(Integer id);
	public byte[] getImage(int id);
	public void setImage(int id, byte[] img);
	
}
