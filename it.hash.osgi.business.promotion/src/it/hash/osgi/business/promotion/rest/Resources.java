package it.hash.osgi.business.promotion.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.swagger.annotations.Api;
import it.hash.osgi.aws.s3.service.S3Service;
import it.hash.osgi.business.Business;
import it.hash.osgi.business.promotion.Promotion;
import it.hash.osgi.business.promotion.PromotionFactory;
import it.hash.osgi.business.promotion.service.PromotionService;
import it.hash.osgi.business.promotion.service.Status;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.utils.StringUtils;

@Component(service = Resources.class)
@Api
@Path("businesses/1.0/")
public class Resources {

	// References
	private volatile PromotionService _promotionService;
	private volatile S3Service _S3Service;
	private volatile UuidService _uuidService;
	private volatile BusinessService _businessService;

	@Reference(service = PromotionService.class)

	public void setPromotionService(PromotionService service) {
		_promotionService = service;
		System.out.println("Referenced PromotionService in the Rest: " + (service == null ? "NULL" : "ok"));
	}

	public void unsetPromotionService(PromotionService service) {
		_promotionService = null;
	}

	@Reference(service = S3Service.class)
	public void setS3service(S3Service Service) {
		_S3Service = Service;
	}

	public void unsetS3service(S3Service Service) {
		_S3Service = null;
	}

	@Reference(service = UuidService.class)
	public void setUuidService(UuidService Service) {
		_uuidService = Service;
	}

	public void unsetUuidservice(UuidService Service) {
		_uuidService = null;
	}

	@Reference(service = BusinessService.class)
	public void setBusinessService(BusinessService Service) {
		_businessService = Service;
	}

	public void unsetBusinessservice(BusinessService Service) {
		_businessService = null;
	}
	// === end references

	// API
	// ===
	@PUT
	// PUT businesses/1.0/business/{uuid}/promotion
	@Path("business/{uuid}/promotion")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "create", notes = "...")
	public Response create(@PathParam("uuid") PathSegment uuidBusiness, TreeMap<String, Object> map) {
		Map<String, Object> response = new TreeMap<String, Object>();

		response = _promotionService.createPromotion(uuidBusiness.getPath(), map);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// POST businesses/1.0/promotion/{uuid}/setActivate
	@POST
	@Path("promotion/{uuid}/setActive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "update", notes = "...")
	public Response setActive(@PathParam("uuid") PathSegment uuid) {
		Map<String, Object> response = new TreeMap<String, Object>();
		String uuidPromotion = uuid.getPath();
		Boolean active = true;
		response = _promotionService.updateActive(uuidPromotion, active);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// POST businesses/1.0/promotion/{uuid}/unsetActive
	@POST
	@Path("promotion/{uuid}/unsetActive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "update", notes = "...")
	public Response unsetActivate(@PathParam("uuid") PathSegment uuid) {
		Map<String, Object> response = new TreeMap<String, Object>();
		String uuidPromotion = uuid.getPath();
		Boolean active = false;
		response = _promotionService.updateActive(uuidPromotion, active);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// POST businesses/1.0/promotion
	@POST
	@Path("promotion")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "update", notes = "...")
	public Response update(Promotion item) {
		Map<String, Object> response = new TreeMap<String, Object>();

		response = _promotionService.updatePromotion(item);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// PUT businesses/1.0/promotion/{puuid}/picture
	@PUT
	@Path("promotion/{puuid}/picture")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@io.swagger.annotations.ApiOperation(value = "create", notes = "...")
	public Response putPicture(@PathParam("puuid") PathSegment puuid, @FormDataParam("picture") InputStream inputStream,
			@FormDataParam("picture") FormDataBodyPart body,
			@FormDataParam("picture") FormDataContentDisposition fileDetail) {
		Map<String, Object> response = new TreeMap<String, Object>();

		String promotionUuid = puuid.getPath();

		if (inputStream != null) {
			try {
				String pictureUuid = _uuidService.createUUID("app/bucket");
				String contentType = body.getMediaType().getType() + "/" + body.getMediaType().getSubtype();

				if (_S3Service.createBucket("reporetail", pictureUuid, contentType, inputStream))
				 response = _promotionService.addPicture(promotionUuid,	 pictureUuid);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// DELETE businesses/1.0/promotions/{uuid}
	@Path("promotions/{uuid}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "delete", notes = "...")
	public Response delete(@PathParam("uuid") PathSegment uuid) {
		String puuid = uuid.getPath();

		Map<String, Object> response = this._promotionService.deletePromotion(puuid);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// DELETE businesses/1.0/promotions
	@Path("promotions")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "delete", notes = "...")
	public Response delete(@QueryParam("listUuid") List<String> listUuid) {

		Map<String, Object> responseAll = new TreeMap<String, Object>();

		for (String uuid : listUuid) {
			Map<String, Object> response = this._promotionService.deletePromotion(uuid);
			// System.out.println("Delete " + response.get("promotion") +
			// "returnCode " + response.get("returnCode"));

			responseAll.put(uuid, response.get("status"));

		}
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(responseAll).build();
	}

	// GET businesses/1.0/promotion/by_searchKeyword/{keyword}
	@GET
	@Path("promotion/by_searchKeyword/{keyword}")
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "getPromotion by keyword", notes = "...")
	public Response getPromotion(@PathParam("keyword") PathSegment keyword) {
		Map<String, Object> response = new TreeMap<String, Object>();
		List<Promotion> items = new ArrayList<Promotion>();
		String search = keyword.getPath();

		items = _promotionService.retrievePromotion(search);
		if (items == null)
			return Response.serverError().build();

		response.put("promotions", items);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// GET businesses/1.0/promotion/by_searchKeyword/{keyword}
	@GET
	@Path("promotion/{uuid}/pictures")
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "getpromotion by keyword", notes = "...")
	public Response getpromotionPictures(@PathParam("uuid") PathSegment uuid) {
		Map<String, Object> response = new TreeMap<String, Object>();
		String promotiontUuid = uuid.getPath();

		List<String> items = null;
		// _promotionService.retrievePromotionPictures(promotiontUuid);
		if (items == null)
			return Response.serverError().build();

		response.put("pictures", items);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// GET businesses/1.0/business/{uuid}/promotion
	@GET
	@Path("business/{uuid}/promotion")
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "getBusinesspromotion by keyword", notes = "...")
	public Response getBusinesspromotions(@PathParam("uuid") PathSegment uuid) {

		String businessUuid = uuid.getPath();

		List<Promotion> items = _promotionService.getPromotionsByBusinessUuid(businessUuid);
		if (items == null)
			return Response.serverError().build();

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(items).build();
	}

	// GET businesses/1.0/business/{uuid}/promotion/by_searchKeyword/{keyword}
	@GET
	@Path("business/{uuid}/promotion/by_searchKeyword/{keyword}")
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.annotations.ApiOperation(value = "getBusinesspromotion by keyword", notes = "...")
	public Response getBusinesspromotions(@PathParam("uuid") PathSegment uuid,
			@PathParam("keyword") PathSegment keyword) {
		String businessUuid = uuid.getPath();
		String search = keyword.getPath();

		List<Promotion> items = _promotionService.retrievepromotions(businessUuid, search.trim());
		if (items == null)
			return Response.serverError().build();

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(items).build();
	}

	/*
	 * // GET businesses/1.0/promotion/categories
	 * 
	 * @GET
	 * 
	 * @Path("promotion/{uuid}/categories")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @io.swagger.annotations.ApiOperation(value = "getProductCategories",
	 * notes = "...") public Response getProductCategories(@PathParam("uuid")
	 * PathSegment uuid) { String productUuid = uuid.getPath();
	 * 
	 * List<Category> items =
	 * _productService.retrieveProductCategories(productUuid); if (items ==
	 * null) return Response.serverError().build();
	 * 
	 * return Response.ok().header("Access-Control-Allow-Origin",
	 * "*").entity(items).build(); }
	 */}
