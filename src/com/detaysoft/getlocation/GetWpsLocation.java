package com.detaysoft.getlocation;

import java.sql.Connection;

import java.sql.ResultSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.db.DbConnectionManager;
import com.detaysoft.model.DeviceInfoModel;
import com.detaysoft.model.ReturnLocationInfoModel;

public class GetWpsLocation {

	private static final Logger Log = LoggerFactory.getLogger(GetWpsLocation.class);
	private static final String SELECT_LC01 = "select * from bn_lc01 where bssid=" + "':bssid:'" + "";
	private static Connection connection;

	private static Map<String, Integer> mapLoc = new HashMap<String, Integer>();

	/**
	 * 
	 * @param bssid
	 */
	public static void selectWpsLocation(List<DeviceInfoModel> bssid) {
		int count = 0;
		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			Log.error("select wps-veri tabanina baglanirken hata: " + e);
		}
		try {

			for (DeviceInfoModel device : bssid) {
				ResultSet rs01 = connection.createStatement()
						.executeQuery(SELECT_LC01.replace(":bssid:", device.getDeviceBssid()));
				while (rs01.next()) {

					GetGpsLocation.selecetGpsLocation(Double.parseDouble(rs01.getString("geolat")),
							Double.parseDouble(rs01.getString("geolon")));
					if (!mapLoc.containsKey(ReturnLocationInfoModel.getLocName()))
						mapLoc.put(ReturnLocationInfoModel.getLocName(), 0);
					else
						for (Map.Entry<String, Integer> entry : mapLoc.entrySet()) {
							System.out.println("key1: " + entry.getKey() + " " + "value1: " + entry.getValue()
									+ " loc: " + ReturnLocationInfoModel.getLocName());

							if (entry.getKey().equals(ReturnLocationInfoModel.getLocName())) {
								mapLoc.put(entry.getKey(), (Integer) entry.getValue() + 1);
								System.out.println("key2: " + entry.getKey() + " " + "value2: " + entry.getValue());
							}
						}

				}

			}

			for (Map.Entry<String, Integer> entry : mapLoc.entrySet()) {
				if (entry.getValue() >= count) {
					count = entry.getValue();
					ReturnLocationInfoModel.setLocName(entry.getKey());
				}

			}

		} catch (Exception e) {
			Log.error("select wps info error: " + e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				Log.error("select wps-baglanti kapatilirken hata: " + e);
			}
		}
	}

}
