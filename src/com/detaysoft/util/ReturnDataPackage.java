package com.detaysoft.util;

import com.detaysoft.model.ReturnLocationInfoModel;
import com.google.gson.JsonObject;

public class ReturnDataPackage  {
	
	
	static JsonObject jsonObjects = new JsonObject();
	public static String returnJson()
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("locname", ReturnLocationInfoModel.getLocName());
		jsonObject.addProperty("cpnm", ReturnLocationInfoModel.getCorpName());
		jsonObject.addProperty("cptp", ReturnLocationInfoModel.getCptp());
		jsonObject.addProperty("cpid", ReturnLocationInfoModel.getCpid());
		jsonObject.addProperty("active",ReturnLocationInfoModel.getActive());
		jsonObject.addProperty("crdat", ReturnLocationInfoModel.getCrdat());
		jsonObject.addProperty("lat", ReturnLocationInfoModel.getLatitude());
		jsonObject.addProperty("lon", ReturnLocationInfoModel.getLongitude());
		jsonObject.addProperty("bssid", ReturnLocationInfoModel.getBssid());
		jsonObjects.add("locinfo", jsonObject);
		
		String base64 = Base64.encodeObject(jsonObjects.toString());
		
		return base64;
	}
}
