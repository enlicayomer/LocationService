package com.detaysoft.getlocation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.db.DbConnectionManager;
import com.detaysoft.model.DeviceInfoModel;
import com.detaysoft.model.GeoLocationModel;
import com.detaysoft.model.IpInfoModel;
import com.detaysoft.model.ReturnLocationInfoModel;

public class LocationDatabaseProvider {

	private static final Logger Log = LoggerFactory.getLogger(LocationDatabaseProvider.class);

	private static final String SELECT_GPSINFO = ".......................";

	private static final String SELECT_BNLC02 = "select * from bn_lc02 where locip=" + "':locip:'";

	private static final String SELECT_LC01 = "select * from bn_lc01 where bssid=" + "':bssid:'" + "";

	private static final String INSERT_INTO_LC00 = "insert into bn_lc00(locnm,geolat,geolon,geoalt,geopoint,cpnm,cptp,cpid,active,crdat) values("
			+ "':locname:',:geolat:,:geolon:,:geoalt:,point(:geolat:,"
			+ ":geolon:),':cpnm:',':cptp:',':cpid:',':active:'," + "':crdat:')";

	private static final String INSERT_INTO_LC01 = "insert into bn_lc01(bssid,ssid,isconnected,geolat,geolon,geoalt,geopoint) values("
			+ "':bssid:',':ssid:',':isconnected:',:geolat:,:geolon:," + ":geoalt:,point(:geolat:,:geolon:))";

	private static final String INSERT_INTO_LC02 = "insert into bn_lc02(locip,geolat,geolon,geoalt,geopoint) values("
			+ "':locip:',:geolat:,:geolon:,:geoalt:,point(:geolat:," + ":geolon:))";

	private static Map<String, Integer> mapLoc = new HashMap<String, Integer>();

	public synchronized String selecetGpsLocation(double lat, double lon, ReturnLocationInfoModel infoModel) {
		Connection connection = null;
		try {
			connection = DbConnectionManager.getConnection();

			ResultSet resultSet = connection.createStatement().executeQuery(
					SELECT_GPSINFO.replaceAll(":lat:", String.valueOf(lat)).replaceAll(":lon:", String.valueOf(lon)));

			while (resultSet.next()) {
				infoModel.setCorpName(resultSet.getString("cpnm"));
				infoModel.setLocName(resultSet.getString("locnm"));
				infoModel.setLatitude(resultSet.getString("geolat"));
				infoModel.setLongitude(resultSet.getString("geolon"));
				infoModel.setCptp(resultSet.getString("cptp"));
				infoModel.setCpid(resultSet.getString("cpid"));
				infoModel.setActive(resultSet.getString("active"));
				infoModel.setCrdat(resultSet.getString("crdat"));
			}
		} catch (SQLException e) {
			Log.error("select gps location hata olustu: " + e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}

		return infoModel.getLocName();
	}

	public synchronized void selectIpLocation(String pip, ReturnLocationInfoModel infoModel) {
		Connection connection = null;
		try {
			connection = DbConnectionManager.getConnection();

			ResultSet resultSet = connection.createStatement().executeQuery(SELECT_BNLC02.replace(":locip:", pip));
			while (resultSet.next()) {
				selecetGpsLocation(resultSet.getDouble("geolat"), resultSet.getDouble("geolon"), infoModel);

			}
		} catch (SQLException e) {
			Log.error("select ip info error: " + e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}

	}

	public synchronized void selectWpsLocation(List<DeviceInfoModel> bssid, ReturnLocationInfoModel infoModel) {
		int count = 0;

		Connection connection = null;
		try {
			connection = DbConnectionManager.getConnection();

			for (DeviceInfoModel device : bssid) {
				ResultSet resultSet = connection.createStatement()
						.executeQuery(SELECT_LC01.replace(":bssid:", device.getDeviceBssid()));
				while (resultSet.next()) {
					selecetGpsLocation(Double.parseDouble(resultSet.getString("geolat")),
							Double.parseDouble(resultSet.getString("geolon")), infoModel);
					if (!mapLoc.containsKey(infoModel.getLocName()))
						mapLoc.put(infoModel.getLocName(), 0);
					else
						for (Map.Entry<String, Integer> entry : mapLoc.entrySet()) {
							System.out.println("key1: " + entry.getKey() + " " + "value1: " + entry.getValue()
									+ " loc: " + infoModel.getLocName());

							if (entry.getKey().equals(infoModel.getLocName())) {
								mapLoc.put(entry.getKey(), (Integer) entry.getValue() + 1);
								System.out.println("key2: " + entry.getKey() + " " + "value2: " + entry.getValue());
							}
						}

				}

			}

			for (Map.Entry<String, Integer> entry : mapLoc.entrySet()) {
				if (entry.getValue() >= count) {
					count = entry.getValue();
					infoModel.setLocName(entry.getKey());
				}

			}

		} catch (Exception e) {
			Log.error("select wps info error: " + e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}

	/**
	 * 
	 * 
	 * @param infoModel
	 * @param geoLocationModel
	 */
	public synchronized void insertLC00(ReturnLocationInfoModel infoModel, GeoLocationModel geoLocationModel) {
		Connection connection = null;
		try {
			connection = DbConnectionManager.getConnection();

			connection.createStatement().executeUpdate(INSERT_INTO_LC00.replace(":locname:", infoModel.getLocName())
					.replaceAll(":geolat:", geoLocationModel.getLatitude())
					.replaceAll(":geolon:", geoLocationModel.getLongitude())
					.replaceAll(":geoalt:", geoLocationModel.getAltitude()).replace(":cpnm:", infoModel.getCorpName())
					.replace(":cptp:", infoModel.getCptp()).replace(":cpid:", infoModel.getCpid())
					.replace(":active:", infoModel.getActive()).replace(":crdat:", infoModel.getCrdat()));
		} catch (Exception e) {
			Log.error("execute insert bn_lc00: " + e);
		}

	}

	public synchronized void insertLC01(List<DeviceInfoModel> deviceInfoModels, GeoLocationModel geoLocationModel) {
		Connection connection = null;
		try {
			connection = DbConnectionManager.getConnection();

			for (DeviceInfoModel model : deviceInfoModels) {
				connection.createStatement().executeUpdate(INSERT_INTO_LC01.replace(":bssid:", model.getDeviceBssid())
						.replace(":ssid:", model.getDeviceSsid()).replace(":isconnected:", model.getIsConnected())
						.replaceAll(":geolat:", geoLocationModel.getLatitude())
						.replaceAll(":geolon:", geoLocationModel.getLongitude())
						.replaceAll(":geoalt:", geoLocationModel.getAltitude()));

			}
		} catch (Exception e) {

			Log.error("execute insert bn_lc01: " + e);
		}
	}

	public synchronized void insertLC02(GeoLocationModel geoLocationModel, IpInfoModel ipInfoModel) {
		Connection connection = null;
		try {
			connection = DbConnectionManager.getConnection();

			connection.createStatement()
					.executeUpdate(INSERT_INTO_LC02.replace(":locip:", ipInfoModel.getLocIp())
							.replaceAll(":geolat:", geoLocationModel.getLatitude())
							.replaceAll(":geolon:", geoLocationModel.getLongitude())
							.replaceAll(":geoalt:", geoLocationModel.getAltitude()));

		} catch (Exception e) {
			Log.error("execute insert bn_lc02: " + e);

		}
	}

}
