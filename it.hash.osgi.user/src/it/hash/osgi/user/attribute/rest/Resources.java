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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.swagger.annotations.Api;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

@Api
@Path("attributes/1.0")
@Component(service = Resources.class)
public class Resources {
	// References
	private AttributeService _attributeService;
	
	@Reference(service=AttributeService.class)
	public void setAttributeService(AttributeService service){
		_attributeService = service;
		doLog("AttributeService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetAttributeService(AttributeService service){
		doLog("AttributeService: "+(service==null?"NULL":"released"));
		_attributeService = null;
	}
	// === end references
	
	// API
	// ===
	
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
   
	// POST attributes/1.0
	@POST
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

    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
