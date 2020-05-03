package objects;

import java.io.Serializable;
import java.time.LocalTime;

//import javafx.scene.image.Image;

public class Meal implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private Double price;
	private LocalTime prep_time;
//	private Image image;
	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalTime getPrep_time() {
		return prep_time;
	}

	public void setPrep_time(LocalTime prep_time) {
		this.prep_time = prep_time;
	}
	
	public Meal(String tit, Double pr, LocalTime prep) {
		this.title=tit;
		this.price=pr;
		this.prep_time=prep;
	}

//	public Image getImage() {
//		return image;
//	}
//
//	public void setImage(Image image) {
//		this.image = image;
//	}
}
