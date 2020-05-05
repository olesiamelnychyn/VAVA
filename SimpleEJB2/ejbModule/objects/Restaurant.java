package objects;

import java.io.Serializable;

public class Restaurant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Zip zip;
	private Integer capacity;
	
	public Zip getZip() {
		return zip;
	}
	public void setZip(Zip zip) {
		this.zip = zip;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	public Restaurant(Zip zip2, Integer r_cap) {
		zip=zip2;
		capacity=r_cap;
	}
	
}
