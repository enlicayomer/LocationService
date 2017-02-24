package com.detaysoft.insertlocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.db.DbConnectionManager;
import com.detaysoft.model.DeviceInfoModel;
import com.detaysoft.model.GeoLocationModel;
import com.detaysoft.model.IpInfoModel;
import com.detaysoft.model.LocationInfoModel;

public class InsertLocationData {

	private static final Logger Log = LoggerFactory.getLogger(InsertLocationData.class);

	/**
	 * 
	 * insert data class
	 */

	private static final String INSERT_INTO_LC00 = "insert into bn_lc00(locnm,geolat,geolon,geoalt,geopoint,cpnm,cptp,cpid,active,crdat) values("
			+ "':locname:'" + "," + ":geolat:" + "," + ":geolon:" + "," + ":geoalt:" + ",point(" + ":geolat:" + ","
			+ ":geolon:" + ")," + "':cpnm:'" + "," + "':cptp:'" + "," + "':cpid:'" + "," + "':active:'" + ","
			+ "':crdat:'" + ")";
	private static final String INSERT_INTO_LC01 = "insert into bn_lc01(bssid,ssid,isconnected,geolat,geolon,geoalt,geopoint) values("
			+ "':bssid:'" + "," + "':ssid:'" + "," + "':isconnected:'" + "," + ":geolat:" + "," + ":geolon:" + ","
			+ ":geoalt:" + ",point(" + ":geolat:" + "," + ":geolon:" + "))";
	private static final String INSERT_INTO_LC02 = "insert into bn_lc02(locip,geolat,geolon,geoalt,geopoint) values("
			+ "':locip:'" + "," + ":geolat:" + "," + ":geolon:" + "," + ":geoalt:" + ",point(" + ":geolat:" + ","
			+ ":geolon:" + "))";

	static Connection connection;
	static int getId;

	public InsertLocationData() {

	}

	/**
	 * 
	 * insert lc00 metod
	 */
	synchronized public static void insrt_lc00() {
		try {
			connection = DbConnectionManager.getConnection();

		} catch (Exception e) {
			Log.error("insert bn_lc00 connect db: " + e);
		}
		try {
			connection.createStatement().executeUpdate(INSERT_INTO_LC00
					.replace(":locname:", LocationInfoModel.getLocname())
					.replaceAll(":geolat:", GeoLocationModel.getLatitude())
					.replaceAll(":geolon:", GeoLocationModel.getLongitude())
					.replaceAll(":geoalt:", GeoLocationModel.getAltitude())
					.replace(":cpnm:", LocationInfoModel.getCpnm()).replace(":cptp:", LocationInfoModel.getCptp())
					.replace(":cpid:", LocationInfoModel.getCpid()).replace(":active:", LocationInfoModel.getActive()).replace(":crdat:", LocationInfoModel.getCrdat()));
		} catch (Exception e) {
			Log.error("execute insert bn_lc00: " + e);
		}

	}

	/**
	 * 
	 * insert bn_lc01 metod
	 */
	public static void insrt_lc01(List<DeviceInfoModel> deviceInfoModels) {
		try {
			connection = DbConnectionManager.getConnection();

		} catch (Exception e) {
			Log.error("insert bn_lc01 connect db: " + e);
		}
		try {
			for (DeviceInfoModel model : deviceInfoModels) {
			connection.createStatement()
					.executeUpdate(INSERT_INTO_LC01.replace(":bssid:", model.getDeviceBssid())
							.replace(":ssid:", model.getDeviceSsid())
							.replace(":isconnected:", model.getIsConnected())
							.replaceAll(":geolat:", GeoLocationModel.getLatitude())
							.replaceAll(":geolon:", GeoLocationModel.getLongitude())
							.replaceAll(":geoalt:", GeoLocationModel.getAltitude()));

			}
		} catch (Exception e) {

			Log.error("execute insert bn_lc01: " + e);
		}
	}

	/**
	 * 
	 * insert bn_lc02 metod
	 */
	public static void insrt_lc02() {
		try {
			connection = DbConnectionManager.getConnection();

		} catch (Exception e) {
			Log.error("insert bn_lc02 connect db: " + e);
		}


		try {

			connection.createStatement()
					.executeUpdate(INSERT_INTO_LC02.replace(":locip:", IpInfoModel.getLocIp())
							.replaceAll(":geolat:", GeoLocationModel.getLatitude())
							.replaceAll(":geolon:", GeoLocationModel.getLongitude())
							.replaceAll(":geoalt:", GeoLocationModel.getAltitude()));

		} catch (Exception e) {
			Log.error("execute insert bn_lc02: " + e);

		}

	}



}
