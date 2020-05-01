package ejb;

import java.util.List;
import java.util.Dictionary;


import javax.ejb.Remote;

@Remote
public interface MealRemote {
	
	List<List<String>> searchMeal(Dictionary<String, String> args);
	int addMeal(Dictionary <String, String>  args);
	void deleteMeal(int id);
	void updateMeal(Dictionary <String, String>  args);
	

}
