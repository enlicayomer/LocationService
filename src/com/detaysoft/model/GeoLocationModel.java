package com.detaysoft.model;

public class GeoLocationModel {

	/*
	 * enlem bilgisi
	 */
	private static String latitude;
	/*
	 * boylam bilgisi
	 */
	private static String longitude;
	
	/*
	 * yükseklik bilgisi
	 */
	private static String altitude="0.00000";
	
	public static String getAltitude() {
		return altitude;
	}
	public static void setAltitude(String altitude) {
		GeoLocationModel.altitude = altitude;
	}
	public static String getLatitude() {
		return latitude;
	}
	public static void setLatitude(String latitude) {
		GeoLocationModel.latitude = latitude;
	}
	public static String getLongitude() {
		return longitude;
	}
	public static void setLongitude(String longitude) {
		GeoLocationModel.longitude = longitude;
	}

	
}
