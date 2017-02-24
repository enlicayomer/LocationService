package com.detaysoft.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.detaysoft.util.Base64ToJson;
import com.detaysoft.util.CastingData;
import com.detaysoft.util.ReturnDataPackage;

@Path("/getloc")
public class GetLocationService {
	
	private static final Logger Log = LoggerFactory.getLogger(CastingData.class);

	@GET
	@Path("{getdata}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getLocation(@PathParam("getdata") String lat) {
		try{
		Base64ToJson.toJson(lat);
		}catch(Exception e)
		{
			Log.error("getloc service error: "+e);
		}
		return Response.ok(ReturnDataPackage.returnJson()).build();
	}

}
