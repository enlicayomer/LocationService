package com.detaysoft.getlocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
	private static String lat,lon;
	private static final String SELECT_LC01 = "select * from bn_lc01";
	private static final String SELECT_LC00 = "select 6378.1 * 2 * ASIN(SQRT(POWER(SIN(RADIANS("+":lat:"+"-bn_lc00.geolat) / 2),2) +COS(RADIANS(39.715523)) * COS(RADIANS(bn_lc00.geolat) )* POWER(SIN((RADIANS("+":lon:"+"-bn_lc00.geolon)) /2), 2) )) AS haversine from bn_lc00 order by haversine asc limit 1";
	private static Connection connection;
	
	private static Map<String, Integer> mapLoc = new HashMap<String, Integer>();

	/**
	 * 
	 * @param bssid
	 */
	public static void selectWpsLocation(List<DeviceInfoModel> bssid) {
		double kontrol=100;
		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			Log.error("select wps-veri tabanina baglanirken hata: " + e);
		}
		try {

			ResultSet rs01 = connection.createStatement()
					.executeQuery(SELECT_LC01);
			
			for (DeviceInfoModel device : bssid) {
				
				while (rs01.next()) {
					ResultSet rs00 = connection.createStatement()
							.executeQuery(SELECT_LC00.replaceAll(":lat:",rs01.getString("geolat")).replaceAll(":lon:",rs01.getString("geolon")));
					while(rs00.next()){
						
						System.out.println("haversine: "+rs00.getString("haversine"));
						mapLoc.put(rs01.getString("bssid"), value);
						if(kontrol>Double.parseDouble(rs00.getString("haversine"))){
						kontrol=Double.parseDouble(rs00.getString("haversine"));
						GetGpsLocation.selecetGpsLocation(Double.parseDouble(rs01.getString("geolat")), Double.parseDouble(rs01.getString("geolon")));
						
						}
					}
					System.out.println(rs01.getString("geolat"));
					System.out.println(rs01.getString("geolon"));
				
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
