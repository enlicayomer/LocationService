package com.detaysoft.model;

public class ManuelSetModel {
	
	/*
	 * Wps için manuel girilecek olan tag
	 */
	private static String tag;
	
	/*
	 * gps için manuel girilecek olan tag
	 */
	private static String corporation;

	public static String getTag() {
		return tag;
	}

	public static void setTag(String tag) {
		ManuelSetModel.tag = tag;
	}

	public static String getCorporation() {
		return corporation;
	}

	public static void setCorporation(String corporation) {
		ManuelSetModel.corporation = corporation;
	}

	
	
	

}
