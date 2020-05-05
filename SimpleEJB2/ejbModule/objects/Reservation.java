package objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reservation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private Restaurant restaurant;
	private int rest_id;
	private ArrayList <Employee> staff = new ArrayList<Employee>();
	private ArrayList <Meal> menu = new ArrayList<Meal>();
	private LocalDateTime date_start;
	private LocalDateTime date_end;
	private int visitors;
	
	public void addEmployee(Employee e) {
		staff.add(e);
	}
	
	public void addMeal(Meal m) {
		menu.add(m);
	}
	
	public void removeEmployee(Employee e) {
		staff.remove(e);
	}
	
	public void removeMeal(Meal m) {
		menu.remove(m);
	}
	
	public ArrayList <Employee> getEmployee(Employee e) {
		return staff;
	}
	
	public ArrayList <Meal> getMeal(Meal m) {
		return menu;
	}
	
//	public Restaurant getRestaurant() {
//		return restaurant;
//	}
//
//	public void setRestaurant(Restaurant restaurant) {
//		this.restaurant = restaurant;
//	}

	public LocalDateTime getDate_start() {
		return date_start;
	}

	public void setDate_start(LocalDateTime date_start) {
		this.date_start = date_start;
	}

	public LocalDateTime getDate_end() {
		return date_end;
	}

	public void setDate_end(LocalDateTime date_end) {
		this.date_end = date_end;
	}

	public int getVisitors() {
		return visitors;
	}

	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	
	public Reservation(int rest, LocalDateTime start, LocalDateTime end, int vis ) {
		this.rest_id=rest;
		this.date_start=start;
		this.date_end=end;
		this.visitors=vis;
	}

	public int getRest_id() {
		return rest_id;
	}

	public void setRest_id(int rest_id) {
		this.rest_id = rest_id;
	}
}
