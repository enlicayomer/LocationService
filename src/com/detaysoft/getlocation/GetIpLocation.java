package com.detaysoft.getlocation;

import java.sql.Connection;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.db.DbConnectionManager;
import com.detaysoft.model.IpInfoModel;
import com.detaysoft.model.ReturnLocationInfoModel;

public class GetIpLocation {

	private static final Logger Log = LoggerFactory.getLogger(GetIpLocation.class);
	private static final String SELECT_OFIP="select oftag.corp,oftag.tag,ofipinfo.pip from oftag,ofipinfo where oftag.idtag=ofipinfo.tag and ofipinfo.pip='"
							+ ":pip:" + "'";
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

			ResultSet rs = connection.createStatement().executeQuery(SELECT_OFIP.replace(":pip:", pip));

			while (rs.next()) {
				ReturnLocationInfoModel.setCorpName(rs.getString("corp"));
				ReturnLocationInfoModel.setLocName(rs.getString("tag"));
				ReturnLocationInfoModel.setPip(rs.getString("pip"));
				
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
