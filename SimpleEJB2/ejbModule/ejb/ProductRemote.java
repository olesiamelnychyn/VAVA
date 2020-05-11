package ejb;

import javax.ejb.Remote;
import java.util.Dictionary;

import objects.Meal;
import objects.Product;
import objects.Reservation;

@Remote
public interface ProductRemote {
	
	Dictionary<Integer, Product> searchProduct(Dictionary<String, String> args);
	int addProduct(Dictionary <String, String>  args);
	void deleteProduct(int id);
	void updateProduct(Dictionary <String, String>  args);
	double getMaxPrice();
	Dictionary<Integer, Meal> getMealProduct(Integer id);
	Dictionary<Integer, Reservation> getReservProduct(Integer id);

}
