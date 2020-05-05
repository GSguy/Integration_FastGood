package acs.data;

import javax.persistence.Embeddable;

@Embeddable
public class Location {

	private Double lat;
	private Double lng;
	  
	public Location() {
		
	}
	
	public Location( Double lat,Double lng) {
		this.setLat(lat);
		this.setLng(lng);	
	}
	
	public Double getLat() {
		return lat;
	}
	public void setLat( Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}	
}