package ejb;

import java.util.Dictionary;
import javax.ejb.Remote;

import objects.Restaurant;

@Remote
public interface RestaurantRemote {
	
	Dictionary<Integer, Restaurant> searchRestaurant(Dictionary<String, String> args);
	int addRestaurant(Dictionary <String, String>  args);
	void deleteRestaurant(int id);
	void updateRestaurant(Dictionary <String, String>  args);

}
