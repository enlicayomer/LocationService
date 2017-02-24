package com.detaysoft.getlocation;

import java.sql.Connection;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.db.DbConnectionManager;
import com.detaysoft.model.GeoLocationModel;
import com.detaysoft.model.ReturnLocationInfoModel;

public class GetGpsLocation {

	private static final Logger Log = LoggerFactory.getLogger(GetIpLocation.class);

	private static final String SELECT_GPSINFO = "select locnm,cpnm,cptp,cpid,active,crdat,geolat,geolon,"
			+ "glength(LineStringFromWKB(linestring(geopoint,point(" + ":lat:" + "," + ":lon:" + "))))*100 "
			+ "as euclidean," + "6378.1*2*asin(sqrt(power(sin(radians(" + ":lat:" + "-geolat)/2),2)+"
			+ "cos(radians(" + ":lat:" + "))*cos(radians(geolat))*power(sin((radians(" + ":lon:"
			+ "-geolon))/2),2))) as haversine "
			+ "from bn_lc00  having euclidean <1 order by euclidean asc limit 1";

	private static Connection connection;

	public GetGpsLocation() {


	}

	
	/**
	 * 
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static String selecetGpsLocation(double lat, double lon) {
		
		try {
			connection = DbConnectionManager.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("select wps-veri tabanina baglanirken hata: " + e);
		}
		
		boolean isSelecet = false;
		try {
			ResultSet resultSet = connection.createStatement().executeQuery(
					SELECT_GPSINFO.replaceAll(":lat:", String.valueOf(lat)).replaceAll(":lon:", String.valueOf(lon)));

			while (resultSet.next()) {
				
				ReturnLocationInfoModel.setCorpName(resultSet.getString("cpnm"));
				ReturnLocationInfoModel.setLocName(resultSet.getString("locnm"));
				ReturnLocationInfoModel.setLatitude(resultSet.getString("geolat"));
				ReturnLocationInfoModel.setLongitude(resultSet.getString("geolon"));
				ReturnLocationInfoModel.setCptp(resultSet.getString("cptp"));
				ReturnLocationInfoModel.setCpid(resultSet.getString("cpid"));
				ReturnLocationInfoModel.setActive(resultSet.getString("active"));
				ReturnLocationInfoModel.setCrdat(resultSet.getString("crdat"));
			}
		} catch (Exception e) {
			Log.error("select gps location hata olustu: " + e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				Log.error("select gps-baglanti kapatilirken hata: " + e);
			}
		}
		return ReturnLocationInfoModel.getLocName();
	}

}
