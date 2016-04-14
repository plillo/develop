package it.hash.osgi.user.attribute.rest;

import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

@Api
@Path("attributes/1.0")
public class Resources {
	private volatile AttributeService _attributeService;
	
	// GET attributes/1.0
	@GET
	@Path("/version")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getVersion() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity("1.0")
				.build();
	}

	// GET attributes/1.0
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAttributes() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(_attributeService.getAttributes())
				.build();
	}
   
	// PUT attributes/1.0
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response create(Attribute attribute){
		Map<String, Object> response = _attributeService.createAttribute(attribute);

		System.out.println("Add " + attribute.get_id() + "returnCode " + response.get("returnCode"));
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(response)
				.build();
	}

	// GET attributes/1.0/{Uuid}
	@Path("/{Uuid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAttribute(@PathParam("Uuid") String uuid) {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(_attributeService.getAttribute(uuid))
				.build();
	}
	
	// POST attributes/1.0/{Uuid}
	@Path("/{Uuid}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("Uuid") String uuid, Attribute attribute) {
		Map<String, Object> response = new TreeMap<String, Object>();

		response = _attributeService.updateAttribute(uuid, attribute);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
}
