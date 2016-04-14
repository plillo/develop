package it.hash.osgi.broker;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.hash.osgi.jwt.service.JWTService;

@Path("broker/1.0")
public class Resources {
	private volatile JWTService _jwtService;
	
	@GET
	public String version() {
		return "OSGi broker bundle by HASH - ver. 1.0";
	}

	@GET
    @Produces("text/plain;charset=UTF-8")
	@Path("monitor")
	public Response monitor(@QueryParam("content") String content) {
		System.out.println(content);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(new String("OK")).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("connection")
	public Response connection(@QueryParam("token") String jwt) {
		Map<String, Object> response = new TreeMap<String, Object>();

		List<String> roles = _jwtService.getRoles(jwt);
		
		if(roles!=null) {
			response.put("verified", true);
			response.put("roles", roles);
		}
		else
			response.put("verified", true); // false
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

}
