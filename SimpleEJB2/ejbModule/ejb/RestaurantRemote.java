package ejb;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import javax.ejb.Remote;

import objects.Employee;
import objects.Meal;
import objects.Restaurant;
import objects.StatisticData;
import objects.Zip;

@Remote
public interface RestaurantRemote {
	
	Dictionary<Integer, Restaurant> searchRestaurant(Dictionary<String, String> args);
	int addRestaurant(Dictionary <String, String>  args);
	void deleteRestaurant(int id);
	void updateRestaurant(Dictionary <String, String>  args);
	ArrayList<Zip> getZip();
	int getMaxCapacity();
	public Dictionary<Integer, Meal> getMealRest(Integer id);
	public Dictionary<Integer, Employee> getEmpRest(Integer id);
	void addCheque(Dictionary <String, String>  args);
	public List<StatisticData> statRest();
}
