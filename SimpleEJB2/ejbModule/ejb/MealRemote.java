package ejb;

import java.util.List;
import java.util.Dictionary;


import javax.ejb.Remote;

@Remote
public interface MealRemote {
	
	List searchMeal(Dictionary args);
	List addMeal(Dictionary args);
	List deleteMeal(int id);
	List updateMeal(Dictionary args);
	

}
