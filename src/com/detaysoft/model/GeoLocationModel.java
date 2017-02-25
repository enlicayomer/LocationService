package com.detaysoft.model;

public class GeoLocationModel {

	// enlem bilgisi
	private String latitude;

	// boylam bilgisi
	private String longitude;

	// yükseklik bilgisi
	private String altitude;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		if (latitude.equals(""))
			latitude = "0.00000";
		
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

}
