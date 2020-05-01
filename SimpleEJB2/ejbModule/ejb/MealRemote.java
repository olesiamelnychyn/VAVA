package ejb;

import java.util.List;
import java.util.Dictionary;


import javax.ejb.Remote;

import objects.Meal;

@Remote
public interface MealRemote {
	
	List<Dictionary<Integer, Meal>> searchMeal(Dictionary<String, String> args);
	int addMeal(Dictionary <String, String>  args);
	void deleteMeal(int id);
	void updateMeal(Dictionary <String, String>  args);
	

}
