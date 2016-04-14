package it.hash.osgi.security.service;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("security")
public class Resources {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String satHello() {
		return "Hello";
	}
	
	@GET
	@Path("/unsecure")
	@PermitAll
	public String getUnsecure() {
		return "This is the result of a request to the unsecured method";
	}

	@GET
	@Path("/secure")
	@RolesAllowed("secure")
	public String getSecure() {
		return "This is the result of a request to the secured method";
	}

	@GET
	@Path("/root")
	@RolesAllowed("root")
	public String getRoot() {
		return "This is the result of a request to the secured method for a ROOT API";
	}
}
