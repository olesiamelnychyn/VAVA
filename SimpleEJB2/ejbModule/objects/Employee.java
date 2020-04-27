package objects;

//import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="employee")
public class Employee {
//	private Restaurant restaurant;
//	private String first_name;
//	private String last_name;
//	private String gender; //"M" for male and "F" for female
//	private Date birthdate;
//	private String phone;
//	private String e_mail;
//	private Position position;
	private Double wage;
	
	
	
	public Double getWage() {
		return wage;
	}
	public void setWage(Double wage) {
		this.wage = wage;
	}
//	public Restaurant getRestaurant() {
//		return restaurant;
//	}
//	public void setRestaurant(Restaurant restaurant) {
//		this.restaurant = restaurant;
//	}
//	public Position getPosition() {
//		return position;
//	}
//	public void setPosition(Position position) {
//		this.position = position;
//	}
	
	
}
