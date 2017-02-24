package com.detaysoft.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.model.LocationInfoModel;
import com.detaysoft.model.ReturnLocationInfoModel;
import com.detaysoft.util.Base64ToJson;
import com.detaysoft.util.CastingData;
import com.detaysoft.util.ReturnDataPackage;

@Path("/setloc")
public class SetLocationService {

	private static final Logger Log = LoggerFactory.getLogger(CastingData.class);

	@GET
	@Path("{setdata}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getLocation(@PathParam("setdata") String lat) {
		try{
		Base64ToJson.toJson(lat);
		}catch(Exception e)
		{
			Log.error("setloc service error: "+e);
		}
		return Response.status(201).build();
	}

}
