package objects;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer rest_id;
	private String first_name;
	private String last_name;
	private String gender; //"M" for male and "F" for female
	private LocalDate birthdate;
	private String phone;
	private String e_mail;
	private String position;
	private Double wage;
	
	public Employee(Integer r_id, String f_n, String l_n, String g, LocalDate birth, String p, String e_m, String pos, Double w) {
		this.rest_id=r_id;
		this.first_name=f_n;
		this.last_name=l_n;
		this.gender=g;
		this.birthdate=birth;
		this.phone=p;
		this.e_mail=e_m;
		this.position=pos;
		this.wage=w;
	}
	
	public Double getWage() {
		return wage;
	}
	public void setWage(Double wage) {
		this.wage = wage;
	}

	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public Integer getRest_id() {
		return rest_id;
	}
	public void setRest_id(Integer rest_id) {
		this.rest_id = rest_id;
	}
	
	
}
