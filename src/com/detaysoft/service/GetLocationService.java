package com.detaysoft.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/location")
public class GetLocationService {

	   @GET
	   @Path("get/{lat},{lon}")
	   @Produces(MediaType.TEXT_PLAIN)
	   public Response sayPlainTextHello(@PathParam("lat") String lat,@PathParam("lat") String lon) {
	     return Response.ok(lat+" "+lon).build();
	   }                                                                                                                    
}
