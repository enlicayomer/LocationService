package com.detaysoft.util;

public class Base64ToJson {

	
	public static void toJson(String base64Data)
	{
		byte[] base64=Base64.decode(base64Data);
		
		CastingData castingData = new CastingData(new String(base64));
		
	}
	
}
