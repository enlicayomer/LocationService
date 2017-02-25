package com.detaysoft.util;

import java.util.ArrayList;
import java.util.List;

import com.detaysoft.getlocation.LocationDatabaseProvider;
import com.detaysoft.model.DeviceInfoModel;
import com.detaysoft.model.GeoLocationModel;
import com.detaysoft.model.IpInfoModel;
import com.detaysoft.model.ReturnLocationInfoModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jersey.core.util.Base64;

public class CastingData extends LocationModuleAbstract {

	private LocationDatabaseProvider databaseProvider = null;

	List<DeviceInfoModel> deviceList;
	double lat, lon;
	String pip;

	public CastingData() {
		databaseProvider = new LocationDatabaseProvider();
	}

	/**
	 * 3. aþama
	 * 
	 * Yapýcý metoda gelen json paketindeki verilere göre kontrol yapýlýr. "get"
	 * ise veri tabanýndan sorgulama yapýlarak sonuç dönderilir. "set" ise veri
	 * tabanýna kayýt yapýlýr.
	 * 
	 * gelen paket "get" ve veri tabanýnda kayýtlý deðilse locName deðerine "0"
	 * yüklenerek sonuç dönderilir.
	 * 
	 * gelen paket "set" ve "locname", "corpname" alanlarý boþ ise boþ hatasý
	 * Log'lanýr.
	 * 
	 * @param jsonData
	 */
	public String processLocation(String jsonString) {
		JsonObject infoObject = getProperty(new String(jsonString));
		String type = getString(infoObject.get("type"));

		GeoLocationModel geoLocationModel = new GeoLocationModel();
		IpInfoModel ipInfoModel = new IpInfoModel();
		ReturnLocationInfoModel returnInfoModel = new ReturnLocationInfoModel();

		if (type.equals("get")) {

			if (getGPSInformation(infoObject, geoLocationModel)) {

				databaseProvider.selecetGpsLocation(Double.parseDouble(geoLocationModel.getLatitude()),
						Double.parseDouble(geoLocationModel.getLongitude()), returnInfoModel);

			} else if (getDeviceInformations(infoObject)) {

				databaseProvider.selectWpsLocation(deviceList, returnInfoModel);

			} else if (getIpInformation(infoObject, ipInfoModel)) {
				databaseProvider.selectIpLocation(ipInfoModel.getLocIp(), returnInfoModel);

			} else {
				returnInfoModel.setLocName("0");
			}
		} else if (type.equals("set")) {
			ReturnLocationInfoModel infoModel = locInfo(infoObject);

			if (infoModel != null) {
				if (getGPSInformation(infoObject, geoLocationModel)) {
					databaseProvider.insertLC00(infoModel, geoLocationModel);
				}
				if (getDeviceInformations(infoObject)) {
					databaseProvider.insertLC01(deviceList, geoLocationModel);
				}
				if (getIpInformation(infoObject, ipInfoModel)) {
					databaseProvider.insertLC02(geoLocationModel, ipInfoModel);
				}
			}
		}

		return returnData(returnInfoModel);
	}

	private boolean getGPSInformation(JsonObject infoObject, GeoLocationModel geoLocationModel) {
		boolean isGPSInformation = false;
		JsonObject geolocation = infoObject.getAsJsonObject("geolocation");

		String latitude = getString(geolocation.get("latitude"));

		String longitude = getString(geolocation.get("longitude"));
		String altitude = getString(geolocation.get("altitude"));
		geoLocationModel.setLatitude(latitude);
		geoLocationModel.setLongitude(longitude);
		geoLocationModel.setAltitude(altitude);
		if (!latitude.isEmpty() && !longitude.isEmpty())
			isGPSInformation = true;
		if(altitude.isEmpty())
			geoLocationModel.setAltitude("0.000");

		return isGPSInformation;
	}

	private boolean getDeviceInformations(JsonObject infoObject) {
		boolean isDeviceInformation = false;
		// cihaz bilgileri
		JsonArray locationInfoObject = infoObject.getAsJsonArray("deviceinfolist");
		List<DeviceInfoModel> deviceInfoList = new ArrayList<DeviceInfoModel>();

		// bagli cihaz
		for (JsonElement deviceInfoObject : locationInfoObject) {

			DeviceInfoModel deviceInfoModel = new DeviceInfoModel();

			String deviceBssid = getString(deviceInfoObject.getAsJsonObject().get("bssid"));

			deviceInfoModel.setDeviceBssid(deviceBssid);

			String deviceSsid = getString(deviceInfoObject.getAsJsonObject().get("ssid"));
			deviceInfoModel.setDeviceSsid(deviceSsid);

			String deviceIsConnected = getString(deviceInfoObject.getAsJsonObject().get("isconnected"));
			deviceInfoModel.setIsConnected(deviceIsConnected);

			deviceInfoList.add(deviceInfoModel);

			if (deviceBssid.isEmpty())
				deviceInfoList.remove(deviceInfoList.size() - 1);

		}

		deviceList = deviceInfoList;

		if (!deviceInfoList.isEmpty())
			isDeviceInformation = true;

		return isDeviceInformation;
	}

	public boolean getIpInformation(JsonObject infoObject, IpInfoModel ipInfoModel) {
		boolean isIpInformation = false;
		JsonObject ipInfoModelJsonObject = infoObject.getAsJsonObject("ipinfo");

		// Local ip adresi
		String locIp = getString(ipInfoModelJsonObject.get("locip"));
		ipInfoModel.setLocIp(locIp);
		if (!locIp.isEmpty())
			isIpInformation = true;

		return isIpInformation;
	}

	public ReturnLocationInfoModel locInfo(JsonObject infoObject) {
		try {
			JsonObject locInfo = infoObject.getAsJsonObject("locinfo");
			ReturnLocationInfoModel infoModel = new ReturnLocationInfoModel();

			String locname = getString(locInfo.get("locname"));
			String cpnm = getString(locInfo.get("cpnm"));
			String cptp = getString(locInfo.get("cptp"));
			String cpid = getString(locInfo.get("cpid"));
			String active = getString(locInfo.get("active"));
			String crdat = getString(locInfo.get("crdat"));

			infoModel.setLocName(locname);
			infoModel.setCorpName(cpnm);
			infoModel.setCptp(cptp);
			infoModel.setCpid(cpid);
			infoModel.setActive(active);
			infoModel.setCrdat(crdat);

			return infoModel;
		} catch (Exception e) {
			return null;
		}
	}

	private String returnData(ReturnLocationInfoModel infoModel) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("locname", infoModel.getLocName());
		jsonObject.addProperty("cpnm", infoModel.getCorpName());
		jsonObject.addProperty("cptp", infoModel.getCptp());
		jsonObject.addProperty("cpid", infoModel.getCpid());
		jsonObject.addProperty("active", infoModel.getActive());
		jsonObject.addProperty("crdat", infoModel.getCrdat());
		jsonObject.addProperty("lat", infoModel.getLatitude());
		jsonObject.addProperty("lon", infoModel.getLongitude());
		jsonObject.addProperty("bssid", infoModel.getBssid());

		String base64 = new String(Base64.encode((jsonObject.toString())));

		return base64;
	}
}
