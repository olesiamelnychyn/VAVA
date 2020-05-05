package objects;

import java.io.Serializable;

public class StatisticData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String attribute;
	public Double data1;
	public Double data2;
	
	public StatisticData(String att, Double al, Double pro) {
		attribute=att;
		data1=al;
		data2=pro;
	}
}
