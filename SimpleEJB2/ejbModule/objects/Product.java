package objects;

import java.io.Serializable;

public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private Double price;
	private int supp_id;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getSupplier() {
		return supp_id;
	}
	public void setSupplier(Integer supplier) {
		this.supp_id = supplier;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Product(String title, Double price, int supp_id) {
		this.title=title;
		this.price=price;
		this.supp_id=supp_id;
	}
}
