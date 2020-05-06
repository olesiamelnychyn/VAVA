package ejb;

import java.util.ArrayList;
import java.util.Dictionary;
import javax.ejb.Remote;

import objects.Restaurant;
import objects.Zip;

@Remote
public interface RestaurantRemote {
	
	Dictionary<Integer, Restaurant> searchRestaurant(Dictionary<String, String> args);
	int addRestaurant(Dictionary <String, String>  args);
	void deleteRestaurant(int id);
	void updateRestaurant(Dictionary <String, String>  args);
	ArrayList<Zip> getZip();
	int getMaxCapacity();
}
