package ejb;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="zip")
public class Zip {
	private String code;
	private String state;
	
	@Id
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "Zipcode " + this.code + ", state "+this.state;
	}
}
