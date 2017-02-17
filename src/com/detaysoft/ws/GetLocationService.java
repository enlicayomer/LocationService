package com.detaysoft.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.detaysoft.model.LocationInfoModel;
import com.detaysoft.model.ReturnLocationInfoModel;
import com.detaysoft.util.Base64ToJson;

@Path("/getloc")
public class GetLocationService {

	   @GET
	   @Path("{base64}")
	   @Produces(MediaType.TEXT_PLAIN)
	   public Response getLocation(@PathParam("base64") String lat) {
		   
		   Base64ToJson.toJson(lat);
		   String locName=ReturnLocationInfoModel.getLocName();
		   System.out.println("locName: "+locName);
	     return Response.ok(locName).build();
	   }                    
	   
	   @GET
	   @Path("{lat}/{lon}/{corp}")
	   @Produces(MediaType.APPLICATION_JSON)
	   public Response sayPlainTextHello(@PathParam("lat") String lat,@PathParam("lon") String lon,@PathParam("corp") String corp) {
		  
		   
	     return Response.ok(lat+" "+lon+" "+corp).build();
	   }              
}
