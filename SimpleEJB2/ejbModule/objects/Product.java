package objects;

import java.io.Serializable;

public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private Double price;
	private Supplier supp_id;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Supplier getSupp_id() {
		return supp_id;
	}
	public void setSupp_id(Supplier supplier) {
		this.supp_id = supplier;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Product(String title, Double price, Supplier supp_) {
		this.title=title;
		this.price=price;
		this.supp_id=supp_;
	}
	@Override
	public String toString() {
		return this.title + ": "+this.price + " " + this.supp_id.toString();
	}
}
