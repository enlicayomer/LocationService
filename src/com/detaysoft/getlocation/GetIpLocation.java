package com.detaysoft.getlocation;

import java.sql.Connection;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.db.DbConnectionManager;




public class GetIpLocation {

	private static final Logger Log = LoggerFactory.getLogger(GetIpLocation.class);
	private static final String SELECT_BNLC02 = "select * from bn_lc02 where locip="+"':locip:'";
	private static Connection connection;

	public GetIpLocation() {

	}

	/**
	 * 
	 * @param pip
	 */
	public static void selectIpLocation(String pip) {

		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			Log.error("select wps-veri tabanina baglanirken hata: " + e);
		}
		try {
			ResultSet rs02 = connection.createStatement().executeQuery(SELECT_BNLC02.replace(":locip:", pip));

			while (rs02.next()) {
				GetGpsLocation.selecetGpsLocation(rs02.getDouble("geolat"), rs02.getDouble("geolon"));

			}
		} catch (Exception e) {
			Log.error("select ip info error: " + e);
		} finally {
			try {
				connection.close();

			} catch (Exception e) {
				Log.error("select ip-baglanti kapatilirken hata: " + e);
			}
		}

	}
}
