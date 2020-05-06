package ejb;

import java.sql.SQLException;
import java.util.Dictionary;
import javax.ejb.Remote;
import objects.Meal;
import objects.Product;
import objects.Reservation;
import objects.Restaurant;

@Remote
public interface MealRemote {
	
	Dictionary<Integer, Meal> searchMeal(Dictionary<String, String> args);
	int addMeal(Dictionary <String, String>  args);
	void deleteMeal(int id);
	void updateMeal(Dictionary <String, String>  args);
	Dictionary<Integer, Restaurant> getRestMeal(Integer id) throws SQLException;
	Dictionary<Integer, Reservation> getReservMeal(Integer id);
	Dictionary<Integer, Product> getProdMeal(Integer id);

}
