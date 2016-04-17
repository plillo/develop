package it.hash.osgi.runtest.restapi.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;

@Api
@Path("restapi")
public class Resources {
	// GET restapi/sayhello
	@GET
	@Path("sayhello")
    @Produces(MediaType.TEXT_PLAIN)
	public Response sayHello() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity("Hello OSGi world!")
				.build();
	}
	
	@GET
	@Path("sayhello2")
    @Produces(MediaType.TEXT_PLAIN)
	public String sayHello2() {
		return "Hello OSGi world!";
	}
	
	@GET
	@Path("sayjsonhello")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sayjsonhello(@QueryParam("test") String test) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("test", test);
		Map<String, Object> map2 = new TreeMap<String, Object>();
		map2.put("field", test);
		map.put("map", map2);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(map);
		list.add(map2);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(list)
				.build();
	}
	
	@GET
	@Path("sayxmlhello")
	@Produces(MediaType.APPLICATION_XML)
	public Response getBusiness(@QueryParam("test") String test) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("test", test);
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(map)
				.build();
	}
}
