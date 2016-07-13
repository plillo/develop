package it.hash.osgi.business.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import eu.medsea.mimeutil.detector.MimeDetector;
import io.swagger.annotations.Api;
import it.hash.osgi.business.Business;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.geojson.Coordinates;
import it.hash.osgi.geojson.Point;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;
import it.hash.osgi.user.service.api.UserService;

@Api
@Path("businesses/1.0/businesses")
@Component(service = Resources.class)
public class Resources {
	
	// References
	private BusinessService _businessService;
	private AttributeService _attributeService;
	private UserService _userService;
	
	@Reference(service=BusinessService.class)
	public void setBusinessService(BusinessService service){
		_businessService = service;
		doLog("BusinessService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetBusinessService(BusinessService service){
		doLog("BusinessService: "+(service==null?"NULL":"released"));
		_businessService = null;
	}
	
	@Reference(service=AttributeService.class)
	public void setAttributeService(AttributeService service){
		_attributeService = service;
		doLog("AttributeService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetAttributeService(AttributeService service){
		doLog("AttributeService: "+(service==null?"NULL":"released"));
		_attributeService = null;
	}
	
	@Reference(service=UserService.class)
	public void setUserService(UserService service){
		_userService = service;
		doLog("UserService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUserService(UserService service){
		doLog("UserService: "+(service==null?"NULL":"released"));
		_userService = null;
	}
	// === end references
	
	// API
	// ===

	// GET businesses/1.0/businesses/{Uuid}
	@Path("/{Uuid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getBusiness", notes = "...")
	public Response getBusiness(@PathParam("Uuid") String uuid) {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.entity(Business.toMap(_businessService.getBusiness(uuid, false)))
				.build();
	}
	
	// GET businesses/1.0/businesses/{Uuid}/position
	@Path("/{uuid}/position")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getPosition", notes = "...")
	public Response getPosition(@PathParam("uuid") String businessUuid) {
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getPosition(businessUuid)).build();
	}
	
	@GET
	@Path("/{uuid}/logo")
	@Produces(MediaType.WILDCARD)
    @io.swagger.annotations.ApiOperation(value = "getLogo", notes = "get business logo")
	public Response getLogo(@PathParam("uuid") String uuid) throws Exception {
		Business business = _businessService.getBusiness(uuid, true);
	
		if(business.getLogo()!=null)
			return Response.ok(new ByteArrayInputStream(Base64.decodeBase64(business.getLogo()))).type(business.getLogoType()).build();
		else
			return Response.ok().header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("/{uuid}/rules")
	@Produces(MediaType.WILDCARD)
    @io.swagger.annotations.ApiOperation(value = "getLogo", notes = "get business logo")
	public Response getSubscriptionRules(@PathParam("uuid") String uuid) throws Exception {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		Map<String, Object> retrieve = _businessService.retrieveSubscriptionRules(uuid, _userService.getUUID());

		if((boolean)retrieve.get("matched")){
			response.put("matched", true);
			response.put("rules", retrieve.get("rules"));
			response.put("name", retrieve.get("name"));
		}
		else
			response.put("matched", false);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	@PUT
	@Path("/{uuid}/rules/{rule}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getLogo", notes = "get business logo")
	public Response setSubscriptionRule(@PathParam("uuid") String uuid, @PathParam("rule") String rule, Map<String,Object> map) throws Exception {
		Map<String, Object> response = new TreeMap<String, Object>();
		String action = (String)map.get("action");
		Boolean set = null;
		if("set".equalsIgnoreCase(action))
			set = true;
		else if("unset".equalsIgnoreCase(action))
			set = false;

		if(set==null)
			response.put("executed", false);
		else {
			response.put("executed", true);
			Map<String, Object> serviceResponse = _businessService.setSubscriptionRule(uuid, _userService.getUUID(), rule, set);
			boolean setted = (boolean)serviceResponse.get("setted");
			response.put("setted", setted);
			if(setted) {
				response.put("rule", (String)serviceResponse.get("rule"));
				response.put("status", (boolean)serviceResponse.get("status"));
			}
		}
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
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
    @io.swagger.annotations.ApiOperation(value = "getFollowedPositions", notes = "...")
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
    @io.swagger.annotations.ApiOperation(value = "getBusiness by keword", notes = "...")
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
    @io.swagger.annotations.ApiOperation(value = "getAttribute", notes = "...")
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
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @io.swagger.annotations.ApiOperation(value = "update", notes = "...")
	public Response update(
			@Context HttpServletRequest request,
			@PathParam("uuid") String uuid, 
			@FormDataParam("name") String name,
			@FormDataParam("address") String address,
			@FormDataParam("_description") String _description,
			@FormDataParam("city") String city,
			@FormDataParam("cap") String cap,
			@FormDataParam("pIva") String pIva,
			@FormDataParam("fiscalCode") String fiscalCode,
			@FormDataParam("logo") InputStream inputStream,
			@FormDataParam("logo") FormDataBodyPart body,
			@FormDataParam("logo") FormDataContentDisposition fileDetail) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		Business business = new Business();
		business.setUuid(uuid);
		business.setName(name);
		business.setAddress(address);
		business.set__Description(_description);
		business.setCity(city);
		business.setCap(cap);
		business.setPIva(pIva);
		business.setFiscalCode(fiscalCode);
		try {
			if(inputStream!=null){
				// Cloning the inputStream
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) > -1 ) {
				    baos.write(buffer, 0, len);
				}
				baos.flush();

				InputStream is1 = new ByteArrayInputStream(baos.toByteArray()); 
				InputStream is2 = new ByteArrayInputStream(baos.toByteArray()); 
				
				// Set Logo
				business.setLogo(Base64.encodeBase64(IOUtils.toByteArray(is1)));
				
				// Get MimeType
				String mimeType = "application/octet-stream";
				MimeDetector md = MimeUtil.getMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
				if(md==null)
					MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
				@SuppressWarnings("unchecked")
				Collection<MimeType> collection = MimeUtil.getMimeTypes(IOUtils.toByteArray(is2));
				Iterator<MimeType> iterator = collection.iterator();
				while(iterator.hasNext()) {
					MimeType mt = iterator.next();
					mimeType =  mt.getMediaType() + "/" + mt.getSubType();
					break;
				}

				// Set LogoType
				business.setLogoType(mimeType);
			}
		} catch (IOException e) {
		}

		response = _businessService.updateBusiness(uuid, business);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// PUT businesses/1.0/businesses
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @io.swagger.annotations.ApiOperation(value = "create", notes = "...")
	public Response create(
			@Context HttpServletRequest request,
			@FormDataParam("name") String name,
			@FormDataParam("address") String address,
			@FormDataParam("description") String description,
			@FormDataParam("city") String city,
			@FormDataParam("cap") String cap,
			@FormDataParam("pIva") String pIva,
			@FormDataParam("fiscalCode") String fiscalCode,
			@FormDataParam("categories") String categories,
			@FormDataParam("logo") InputStream inputStream,
			@FormDataParam("logo") FormDataBodyPart body,
			@FormDataParam("logo") FormDataContentDisposition fileDetail) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		Business business = new Business();
		business.setOwner(_userService.getUUID());
		business.setName(name);
		business.setAddress(address);
		business.set__Description(description);
		business.setCity(city);
		business.setCap(cap);
		business.setPIva(pIva);
		business.setFiscalCode(fiscalCode);
		
		for(String category:categories.split(",")) 
			business.addCategory(category);
		
		try {
			if(inputStream!=null){
				// Cloning the inputStream
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) > -1 ) {
				    baos.write(buffer, 0, len);
				}
				baos.flush();

				InputStream is1 = new ByteArrayInputStream(baos.toByteArray()); 
				InputStream is2 = new ByteArrayInputStream(baos.toByteArray()); 
				
				// Set Logo
				business.setLogo(Base64.encodeBase64(IOUtils.toByteArray(is1)));
				
				// Get MimeType
				String mimeType = "application/octet-stream";
				MimeDetector md = MimeUtil.getMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
				if(md==null)
					MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
				@SuppressWarnings("unchecked")
				Collection<MimeType> collection = MimeUtil.getMimeTypes(IOUtils.toByteArray(is2));
				Iterator<MimeType> iterator = collection.iterator();
				while(iterator.hasNext()) {
					MimeType mt = iterator.next();
					mimeType =  mt.getMediaType() + "/" + mt.getSubType();
					break;
				}

				// Set LogoType
				business.setLogoType(mimeType);
			}
		} catch (IOException e) {
		}

		response = _businessService.createBusiness(business);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// DELETE business/1.0/businesses/{Uuid}
	@Path("/{uuid}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "delete", notes = "...")
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
    @io.swagger.annotations.ApiOperation(value = "setMapPosition", notes = "...")
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
    @io.swagger.annotations.ApiOperation(value = "unFollow", notes = "...")
	public Response unFollow(@PathParam("uuid") String businessUuid) {
		Map<String, Object> response = _businessService.unfollow(businessUuid, _userService.getUUID());
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/{uuid}/selfFollow")
    @io.swagger.annotations.ApiOperation(value = "selfFollow", notes = "...")
	public Response selfFollow(@PathParam("uuid") String businessUuid) {
		Map<String, Object> response = _businessService.follow(businessUuid, _userService.getUUID());
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	@GET
	@Path("/by_selfFollowed")
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getFollowedBusinesses", notes = "...")
	public Response getFollowedBusinesses() {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveFollowedByUser(_userService.getUUID(), false);

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// GET businesses/1.0/businesses/by_selfOwned
	@GET
	@Path("/by_selfOwned")
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getOwnedBusinesses", notes = "...")
	public Response getOwnedBusinesses() {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveOwnedByUser(_userService.getUUID(), false);

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@GET 
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/by_notSelfFollowed/by_searchKeyword/{keyword}")
    @io.swagger.annotations.ApiOperation(value = "getNotFollowedBusiness", notes = "...")
	public Response getNotFollowedBusiness(@PathParam("keyword") String keyword) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveNotFollowedByUser(_userService.getUUID(), keyword, false);

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
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
