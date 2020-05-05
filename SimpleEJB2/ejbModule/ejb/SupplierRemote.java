package ejb;

import java.util.Dictionary;
import javax.ejb.Remote;

import objects.Supplier;

@Remote
public interface SupplierRemote {
	
	Dictionary<Integer, Supplier> searchSupplier(Dictionary<String, String> args);
	int addSupplier(Dictionary <String, String>  args);
	void deleteSupplier(int id);

}
