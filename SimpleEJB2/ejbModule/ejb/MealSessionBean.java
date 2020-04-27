package ejb;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import javax.ejb.Stateless;

@Stateless(name = "MealSessionEJB")
public class MealSessionBean implements MealRemote{

	@Override
	public List searchMeal(Dictionary args) {
		System.out.print("here");
		List la = new ArrayList();
		la.add("here");
		return la;
	}

	@Override
	public List addMeal(Dictionary args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List deleteMeal(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List updateMeal(Dictionary args) {
		// TODO Auto-generated method stub
		return null;
	}

}
