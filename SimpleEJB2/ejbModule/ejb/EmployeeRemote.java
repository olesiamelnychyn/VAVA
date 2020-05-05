package ejb;

import java.util.Dictionary;
import java.util.ArrayList;

import javax.ejb.Remote;
import objects.Employee;


@Remote
public interface EmployeeRemote {

	
	Dictionary<Integer, Employee> searchEmployee(Dictionary<String, String> args);
	int addEmployee(Dictionary <String, String>  args);
	void deleteEmployee(int id);
	void updateEmployee(Dictionary <String, String>  args);
	int getMaxWage();
	ArrayList<String> getPositions();

}
