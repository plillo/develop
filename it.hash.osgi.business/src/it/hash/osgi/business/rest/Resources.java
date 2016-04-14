package it.hash.osgi.business.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.geojson.Coordinates;
import it.hash.osgi.geojson.Point;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;
import it.hash.osgi.user.service.api.UserService;

@Path("businesses/1.0/businesses")
public class Resources {

	private volatile BusinessService _businessService;
	private volatile AttributeService _attributeService;
	private volatile UserService _userService;

	// GET businesses/1.0/businesses/{Uuid}
	@Path("/{Uuid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBusiness(@PathParam("Uuid") String uuid) {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(_businessService.getBusiness(uuid))
				.build();
	}
	
	// GET businesses/1.0/businesses/{Uuid}/position
	@Path("/{uuid}/position")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPosition(@PathParam("uuid") String businessUuid) {
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getPosition(businessUuid)).build();
	}
	
	// GET businesses/1.0/businesses/by_selfOwned/positions
	@Path("/by_selfOwned/positions")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOwnedPositions() {
		// Retrieve
		List<Business> businesses = _businessService.retrieveOwnedByUser(_userService.getUUID());

		if (businesses == null)
			return Response.serverError().build();

		// Build list of coordinates
		List<Map<String, Object>> positions_list = new ArrayList<Map<String, Object>>();
		for(Business business: businesses) {
			if(business.getPosition()!=null) {
				Map<String, Object> position = new TreeMap<String, Object>();
				position.put("uuid",business.getUuid());
				position.put("description",business.get__Description());
				position.put("coordinates",business.getPosition().getCoordinates());
				positions_list.add(position);
			}
		}
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(positions_list).build();
	}
	
	// GET businesses/1.0/businesses/by_selfFollowed/positions
	@Path("/by_selfFollowed/positions")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFollowedPositions() {
		// Retrieve
		List<Business> businesses = _businessService.retrieveFollowedByUser(_userService.getUUID());

		if (businesses == null)
			return Response.serverError().build();

		// Build list of coordinates
		List<Map<String, Object>> positions_list = new ArrayList<Map<String, Object>>();
		for(Business business: businesses) {
			if(business.getPosition()!=null) {
				Map<String, Object> position = new TreeMap<String, Object>();
				position.put("uuid",business.getUuid());
				position.put("description",business.get__Description());
				position.put("coordinates",business.getPosition().getCoordinates());
				positions_list.add(position);
			}
		}
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(positions_list).build();
	}
	
	// GET businesses/1.0/businesses/by_searchKeyword/{keyword};criterion=xyz
	@GET
	@Path("/by_searchKeyword/{keyword}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBusiness(@PathParam("keyword") PathSegment keyword) {

		Map<String, Object> response = new TreeMap<String, Object>();
		List<Business> businesses = new ArrayList<Business>();
		String search = keyword.getPath();
		String criterion = keyword.getMatrixParameters().getFirst("criterion");

		businesses = _businessService.retrieveBusinesses(criterion, search);

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

	
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	@GET
	@Path("/by_searchKeyword/{keyword}/attributes")
	@Produces(MediaType.APPLICATION_JSON)
	// if search == uuidBusiness  ritorna tutti gli attributi di quel Business (valorizzati e non) 
	// in base alle categorie a cui appartiene 
	public Response getAttribute(@PathParam("keyword") PathSegment s) {
		// TODO: da rivedere
		Map<String, Object> response = new TreeMap<String, Object>();
		List<Business> businesses = new ArrayList<Business>();
		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Attribute> attributesUser=new ArrayList<Attribute>();
		String search = s.getPath();
		String criterion = s.getMatrixParameters().getFirst("criterion");

		businesses = _businessService.retrieveBusinesses(criterion, search);
		if (!businesses.isEmpty()) {
			List<String> list = businesses.get(0).getCategories();

			attributes = _attributeService.getAttributesByCategories(list);
			attributes = mergeList(attributes, attributesUser);
            response.put("businesses", businesses.get(0));
			response.put("attributes", attributes);
		} else
			response.put("Error", 400);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// POST businesses/1.0/businesses/{Uuid}
	@Path("/{uuid}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("uuid") String uuid, Business business) {
		Map<String, Object> response = new TreeMap<String, Object>();

		response = _businessService.updateBusiness(uuid, business);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// PUT businesses/1.0/businesses
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON})
	public Response create(Business business) {
		// SET Business's owner
		business.setOwner(_userService.getUUID());
		
		Map<String, Object> response = _businessService.createBusiness(business);
		
		System.out.println("Add " + business.get_id() + "returnCode " + response.get("returnCode"));
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// DELETE business/1.0/businesses/{Uuid}
	@Path("/{uuid}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String uuid) {
		Map<String, Object> response = _businessService.deleteBusiness(uuid);
		System.out.println("Delete  " + response.get("business") + "returnCode " + response.get("returnCode"));

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// POST businesses/1.0/businesses/{Uuid}/map
	@Path("/{Uuid}/map")
	@POST
	@Consumes ({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setMapPosition(@PathParam("Uuid") String uuid, Coordinates coordinate) {
		Business business = new Business();
		business.setPosition(new Point(coordinate));
		Map<String, Object> response = _businessService.updateBusiness(uuid, business);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(response)
				.build();
	}
	
	@POST
	@Consumes ({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/{uuid}/selfUnfollow")
	public Response unFollow(@PathParam("uuid") String businessUuid) {
		Map<String, Object> response = _businessService.unfollow(businessUuid, _userService.getUUID());
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/{uuid}/selfFollow")
	public Response selfFollow(@PathParam("uuid") String businessUuid) {
		Map<String, Object> response = _businessService.follow(businessUuid, _userService.getUUID());
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	@GET
	@Path("/by_selfFollowed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFollowedBusinesses() {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveFollowedByUser(_userService.getUUID());

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// GET businesses/1.0/businesses/by_selfOwned
	@GET
	@Path("/by_selfOwned")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOwnedBusinesses() {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveOwnedByUser(_userService.getUUID());

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@GET 
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/by_notSelfFollowed/by_searchKeyword/{keyword}")
	public Response getNotFollowedBusiness(@PathParam("keyword") String keyword) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveNotFollowedByUser(_userService.getUUID(), keyword);

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private Map<String, Object> extractToForm(MultivaluedMap<String, String> form) {
		Map<String, Object> response = new TreeMap<String, Object>();

		if (form.containsKey("uuid")) {
			String attribute;
			Business business = new Business();
			Map<String, Object> others = new TreeMap<String, Object>();

			for (Entry<String, List<String>> entry : form.entrySet()) {
				attribute = entry.getKey();
				if (attribute.equals("_id")) {
					business.set_id(entry.getValue().toString());
				} else {
					switch (attribute) {
					case "uuid":
						business.setUuid((String) entry.getValue().get(0));
						break;
					case "name":
						business.setName((String) entry.getValue().get(0));
						break;
					case "pIva":
						business.setPIva((String) entry.getValue().get(0));
						break;
					case "fiscalCode":
						business.setFiscalCode((String) entry.getValue().get(0));
						break;
					case "address":
						business.setAddress((String) entry.getValue().get(0));
						break;
					case "city":
						business.setCity((String) entry.getValue().get(0));
						break;
					case "cap":
						business.setCap((String) entry.getValue().get(0));
						break;
					case "nation":
						business.setNation((String) entry.getValue().get(0));
						break;
					case "_description":
						business.set__Description((String) entry.getValue().get(0));
						break;
					case "__longDescription":
						business.set__longDescription((String) entry.getValue().get(0));
						break;
					case "email":
						business.setEmail((String) entry.getValue().get(0));
						break;
					case "mobile":
						business.setMobile((String) entry.getValue().get(0));
						break;
					case "published":
						business.setPublished((String) entry.getValue().get(0));
						break;
					case "trusted_email":
						business.setTrusted_email((String) entry.getValue().get(0));
						break;
					case "trusted_mobile":
						business.setTrusted_mobile((String) entry.getValue().get(0));
						break;
					case "cauthor":
						business.setCauthor((String) entry.getValue().get(0));
						break;
					case "cdate":
						business.setCdate((String) entry.getValue().get(0));
						break;
					case "mauthor":
						business.setMauthor((String) entry.getValue().get(0));
						break;
					case "mdate":
						business.setMdate((String) entry.getValue().get(0));
						break;
					case "lauthor":
						business.setLauthor((String) entry.getValue().get(0));
						break;
					case "ldate":
						business.setLdate((String) entry.getValue().get(0));
						break;
					case "categories":
						String cat = (String) entry.getValue().get(0);
						String[] categories = cat.split(",");
						for (String element : categories) {
							business.addCategory(element);
						}
						break;
					case "others":
						business.setOthers((Map<String, Object>) entry.getValue());
						break;
					default:
						if (business.getOthers() == null)
							business.setOthers(new HashMap<String, Object>());
						if (!business.getOthers().containsKey(attribute))
							others.put(attribute, entry.getValue());

					}
				}
			}
			response.put("business", business);
		}
		return response;
	}
	
	public static List<Attribute> mergeList(List<Attribute> attributes, List<Attribute> attributesUser) {
		for (Attribute elem : attributes) {
			if (attributesUser.contains(elem)) {
				attributes.remove(elem);
				attributes.add(elem);
			}
		}
		return attributes;
	}
}
