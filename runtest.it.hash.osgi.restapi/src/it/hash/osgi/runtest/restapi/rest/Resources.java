package it.hash.osgi.runtest.restapi.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@io.swagger.annotations.Api(description = "the estimates API")
@Path("/restapi")
public class Resources {
	// GET restapi/sayhello
	@GET
	@Path("/sayhello")
    @Produces(MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "sayHelloTextViaEntity", notes = "Note for sayHelloTextViaEntity:...\n")
	public Response sayHelloTextViaEntity() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity("Hello OSGi world!")
				.build();
	}
	
	@GET
	@Path("/sayhello2")
    @Produces(MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "sayHelloTextDirect", notes = "Note for sayHelloTextDirect:...\n")
	public String sayHelloTextDirect() {
		return "Hello OSGi world!";
	}
	
	@GET
	@Path("/sayjsonhello")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("anonymous")
    @io.swagger.annotations.ApiOperation(value = "sayjsonhello", notes = "Note for sayjsonhello:...\n")
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
    @io.swagger.annotations.ApiOperation(value = "sayxmlhello", notes = "Note for sayxmlhello:...\n")
	public Response sayxmlhello(@QueryParam("test") String test) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("test", test);
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(map)
				.build();
	}
}
