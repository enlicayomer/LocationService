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
import com.detaysoft.model.ManuelSetModel;

public class InsertLocationData {

	private static final Logger Log = LoggerFactory.getLogger(InsertLocationData.class);

	private static final String SELECT_OFTAG = "SELECT * FROM oftag ORDER BY idtag DESC LIMIT 1";
	private static final String INSERT_INTO_OFTAG = "INSERT INTO oftag(tag,corp) VALUES(?,?)";
	private static final String INSERT_INTO_OFGPS = "INSERT INTO ofgpsinfo(lat,lon,loc,tag) VALUES(" + ":lat:" + ","
			+ ":lon:" + ",point(" + ":lat:" + "," + ":lon:" + ")," + ":id:" + ")";
	private static final String INSERT_INTO_OFWPS = "INSERT INTO ofwpsinfo(bssid,ssid,isconnected,tag) VALUES(?,?,?,?)";
	private static final String INSERT_INTO_OFIP = "INSERT INTO ofipinfo(pip,tag) VALUES(?,?)";

	static Connection connection;
	static int getId;

	public InsertLocationData() {

	}

	synchronized public static void insertTagAndCorp() {
		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			Log.error("insert wps-veri tabanina baglanirken hata: " + e);
		}

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_OFTAG);
			preparedStatement.setString(1, ManuelSetModel.getTag());
			preparedStatement.setString(2, ManuelSetModel.getCorporation());
			preparedStatement.executeUpdate();
			ResultSet rs = connection.createStatement().executeQuery(SELECT_OFTAG);
			while (rs.next())
				getId = rs.getInt("idtag");

		} catch (Exception e) {
			Log.error("insert tag and corp error: " + e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {

				Log.error("wps insert islemi-baglanti kapatilirken hata: " + e);

			}

		}
	}

	synchronized public static void insertGps() {
		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			Log.error("insert wps-veri tabanina baglanirken hata: " + e);
		}

		try {

			connection.createStatement().executeUpdate(INSERT_INTO_OFGPS.replaceAll(":lat:", String.valueOf(GeoLocationModel.getLatitude()))
					.replaceAll(":lon:", String.valueOf(GeoLocationModel.getLongitude())).replace(":id:", String.valueOf(getId)));

		} catch (Exception e) {

			Log.error("wps insert islemi basarisiz: " + e);

		} finally {
			try {
				connection.close();
			} catch (Exception e) {

				Log.error("gps insert islemi-baglanti kapatilirken hata: " + e);

			}

		}

	}

	synchronized public static void insertWps(List<DeviceInfoModel> deviceInfoModels) {
		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			Log.error("select wps-veri tabanina baglanirken hata: " + e);
		}
		try {
			for(DeviceInfoModel model : deviceInfoModels){
				System.out.println(model.getDeviceBssid()+" "+model.getDeviceSsid()+" "+model.getIsConnected()+" "+model.getSignalRate());
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_OFWPS);
			preparedStatement.setString(1, model.getDeviceBssid());
			preparedStatement.setString(2, model.getDeviceSsid());
			preparedStatement.setString(3, model.getIsConnected());
			preparedStatement.setInt(4, getId);

			preparedStatement.executeUpdate();
			}
		} catch (Exception e) {

			Log.error("wps insert islemi basarisiz: " + e);

		} finally {
			try {
				connection.close();
			} catch (Exception e) {

				Log.error("wps insert islemi-baglanti kapatilirken hata: " + e);

			}

		}
	}

	synchronized public static void insertIp() {
		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			Log.error("insert wps-veri tabanina baglanirken hata: " + e);
		}
		

		try {

			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_OFIP);
			preparedStatement.setString(1, IpInfoModel.getPublicIp());
			preparedStatement.setInt(2, getId);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			Log.error("ip insert islemi basarisiz: " + e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {

				Log.error("ip insert islemi-baglanti kapatilirken hata: " + e);

			}

		}
	}

}
