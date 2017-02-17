package com.detaysoft.model;

public class DeviceInfoModel {

	/*
	 * cihaz bssid
	 */
	private String deviceBssid;

	/*
	 * cihaz ssid
	 */
	private String deviceSsid;

	/*
	 * bagli cihaz
	 */
	private String isConnected;

	/*
	 * cihaz sinyali
	 */
	private String signalRate;

	public String getDeviceBssid() {
		return deviceBssid;
	}

	public void setDeviceBssid(String deviceBssid) {
		this.deviceBssid = deviceBssid;
	}

	public String getDeviceSsid() {
		return deviceSsid;
	}

	public void setDeviceSsid(String deviceSsid) {
		this.deviceSsid = deviceSsid;
	}

	public String getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(String isConnected) {
		this.isConnected = isConnected;
	}

	public String getSignalRate() {
		return signalRate;
	}

	public void setSignalRate(String signalRate) {
		this.signalRate = signalRate;
	}
	
	

}
