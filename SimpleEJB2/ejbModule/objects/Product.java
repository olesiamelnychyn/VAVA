package objects;

import java.io.Serializable;

public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private Double price;
	private Supplier supp;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Supplier getSupplier() {
		return supp;
	}
	public void setSupplier(Supplier supplier) {
		this.supp = supplier;
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
		this.supp=supp_;
	}
	@Override
	public String toString() {
		return this.title + ": "+this.price + " " + this.supp.toString();
	}
}
