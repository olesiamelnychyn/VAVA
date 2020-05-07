package objects;

import java.io.Serializable;

public class Supplier  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String phone;
	private String e_mail;
	
	public Supplier(String title, String phone, String e_mail) {
		this.title=title;
		this.phone=phone;
		this.e_mail=e_mail;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	@Override
	public String toString() {
		return this.title;
	}

}
