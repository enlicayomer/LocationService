package com.detaysoft.ws;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.detaysoft.util.CastingData;
import com.sun.jersey.core.util.Base64;

@Path("/restapi/v1/locationService")
public class GetLocationService {

	private static final Logger Log = LoggerFactory.getLogger(CastingData.class);

	@GET
	@Path("/get/{getdata}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getLocation(@PathParam("getdata") String lat) {
		
			CastingData castingData = new CastingData();
			String locationData = new String(Base64.decode(lat));
			String returnJson = castingData.processLocation(locationData);
		return Response.ok(returnJson).build();
			

	}

	@POST
	@Path("/set/{setdata}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response setLocation(@PathParam("setdata") String lat) {
		
			CastingData castingData = new CastingData();
			String locationData = new String(Base64.decode(lat));
			String returnJson = castingData.processLocation(locationData);
			
				return Response.ok(201).build();
			
	}

}
