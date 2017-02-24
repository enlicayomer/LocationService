package com.detaysoft.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.util.LocationModuleAbstract;
import com.detaysoft.getlocation.GetGpsLocation;
import com.detaysoft.getlocation.GetIpLocation;
import com.detaysoft.getlocation.GetWpsLocation;
import com.detaysoft.insertlocation.InsertLocationData;
import com.detaysoft.model.DeviceInfoModel;
import com.detaysoft.model.GeoLocationModel;
import com.detaysoft.model.IpInfoModel;
import com.detaysoft.model.LocationInfoModel;
import com.detaysoft.model.ReturnLocationInfoModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CastingData extends LocationModuleAbstract {

	private static final Logger Log = LoggerFactory.getLogger(CastingData.class);

	String jsonData;
	JsonObject infoObject;
	List<DeviceInfoModel> deviceList;
	double lat, lon;
	String pip;

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
	public CastingData(String jsonData) {

		this.jsonData = jsonData;
		parseData();

		if (type().equals("get")) {

			if (getGPSInformation()) {
				GetGpsLocation.selecetGpsLocation(Double.parseDouble(GeoLocationModel.getLatitude()),
						Double.parseDouble(GeoLocationModel.getLongitude()));
			} else if (getDeviceInformations()) {
				GetWpsLocation.selectWpsLocation(deviceList);
			} else if (getIpInformation()) {
				GetIpLocation.selectIpLocation(IpInfoModel.getLocIp());
			} else {
				ReturnLocationInfoModel.setLocName("0");
			}
		} else if (type().equals("set")) {
			if (locInfo()) {
				if (getGPSInformation()) {
					InsertLocationData.insrt_lc00();
				}
				if (getDeviceInformations()) {
					InsertLocationData.insrt_lc01(deviceList);
				}
				if (getIpInformation()) {
					InsertLocationData.insrt_lc02();
				}
			} else {
				Log.error("beklenmeyen bir hata.");
			}
		}

	}

	private void parseData() {

		Log.info("gelen base64 sifresi decode: " + jsonData);
		infoObject = getProperty(new String(jsonData));
	}

	public String type() {
		return getString(infoObject.get("type"));
	}

	private boolean getGPSInformation() {
		boolean isGPSInformation = false;
		JsonObject geolocation = infoObject.getAsJsonObject("geolocation");

		String latitude = getString(geolocation.get("latitude"));

		String longitude = getString(geolocation.get("longitude"));

		GeoLocationModel.setLatitude(latitude);
		GeoLocationModel.setLongitude(longitude);
		if (!latitude.isEmpty() && !longitude.isEmpty())
			isGPSInformation = true;
		return isGPSInformation;
	}

	private boolean getDeviceInformations() {
		boolean isDeviceInformation = false;
		// cihaz bilgileri
		JsonArray locationInfoObject = infoObject.getAsJsonArray("deviceinfolist");
		List<DeviceInfoModel> deviceInfoList = new ArrayList<DeviceInfoModel>();

		// bagli cihaz
		String isConnectedDeviceId = "";
		for (JsonElement deviceInfoObject : locationInfoObject) {

			DeviceInfoModel deviceInfoModel = new DeviceInfoModel();

			String deviceBssid = getString(deviceInfoObject.getAsJsonObject().get("bssid"));

			deviceInfoModel.setDeviceBssid(deviceBssid);

			String deviceSsid = getString(deviceInfoObject.getAsJsonObject().get("ssid"));
			deviceInfoModel.setDeviceSsid(deviceSsid);

			String deviceIsConnected = getString(deviceInfoObject.getAsJsonObject().get("isconnected"));
			deviceInfoModel.setIsConnected(deviceIsConnected);
			if (deviceIsConnected.equals("X"))
				isConnectedDeviceId = deviceBssid;

			deviceInfoList.add(deviceInfoModel);

			if (deviceBssid.isEmpty())
				deviceInfoList.remove(deviceInfoList.size() - 1);

		}

		deviceList = deviceInfoList;

		if (!deviceInfoList.isEmpty())
			isDeviceInformation = true;

		return isDeviceInformation;
	}

	public boolean getIpInformation() {
		boolean isIpInformation = false;
		JsonObject ipInfoModelJsonObject = infoObject.getAsJsonObject("ipinfo");

		// Local ip adresi
		String locIp = getString(ipInfoModelJsonObject.get("locip"));
		IpInfoModel.setLocIp(locIp);
		if (!locIp.isEmpty())
			isIpInformation = true;
		return isIpInformation;
	}

	
	/**
	 * 
	 * locInfo metod
	 * @return true
	 */
	public boolean locInfo() {
		
		JsonObject locInfo = infoObject.getAsJsonObject("locinfo");

		String locname = getString(locInfo.get("locname"));
		String cpnm = getString(locInfo.get("cpnm"));
		String cptp = getString(locInfo.get("cptp"));
		String cpid = getString(locInfo.get("cpid"));
		String active = getString(locInfo.get("active"));
		String crdat = getString(locInfo.get("crdat"));

		LocationInfoModel.setLocname(locname);
		LocationInfoModel.setCpnm(cpnm);
		LocationInfoModel.setCptp(cptp);
		LocationInfoModel.setCpid(cpid);
		LocationInfoModel.setActive(active);
		LocationInfoModel.setCrdat(crdat);

		

		return true;
	}

}
