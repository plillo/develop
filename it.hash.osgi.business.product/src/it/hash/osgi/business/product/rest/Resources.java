package it.hash.osgi.business.product.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import io.swagger.annotations.Api;
import it.hash.osgi.aws.s3.service.S3Service;
import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.service.ProductService;
import it.hash.osgi.resource.uuid.api.UUIDService;

@Api
@Path("businesses/1.0/")
public class Resources {
	private volatile ProductService _productService;
	private volatile S3Service _S3Service;
	private volatile UUIDService _uuidService;
	
	@PUT
	// PUT businesses/1.0/business/{uuid}/product
	@Path("business/{uuid}/product")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "create", notes = "...")
	public Response create(@PathParam("uuid") PathSegment uuid, Product item) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		item.setBusiness(uuid.getPath());
		response = _productService.createProduct(item);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// POST businesses/1.0/product
	@POST
	@Path("product")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "update", notes = "...")
	public Response update(Product item) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		response = _productService.updateProduct(item);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	// PUT businesses/1.0/product/{puuid}/picture
	@PUT
	@Path("product/{puuid}/picture")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @io.swagger.annotations.ApiOperation(value = "create", notes = "...")
	public Response putPicture(
			@PathParam("puuid") PathSegment puuid,
			@FormDataParam("picture") InputStream inputStream,
			@FormDataParam("picture") FormDataBodyPart body,
			@FormDataParam("picture") FormDataContentDisposition fileDetail) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		String productUuid = puuid.getPath();
		
		if(inputStream!=null){
			try {
				String pictureUuid = _uuidService.createUUID("app/bucket");
				String contentType = body.getMediaType().getType()+"/"+body.getMediaType().getSubtype();

				if(_S3Service.createBucket("reporetail", pictureUuid, contentType, inputStream))
					response = _productService.addPicture(productUuid, pictureUuid);
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
		}
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// GET businesses/1.0/product/by_searchKeyword/{keyword}
	@GET
	@Path("product/by_searchKeyword/{keyword}")
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getProduct by keyword", notes = "...")
	public Response getProduct(@PathParam("keyword") PathSegment keyword) {
		Map<String, Object> response = new TreeMap<String, Object>();
		List<Product> items = new ArrayList<Product>();
		String search = keyword.getPath();

		items = _productService.retrieveProducts(search);
		if (items == null)
			return Response.serverError().build();

		response.put("products", items);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// GET businesses/1.0/product/by_searchKeyword/{keyword}
	@GET
	@Path("product/{uuid}/pictures")
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getProduct by keyword", notes = "...")
	public Response getProductPictures(@PathParam("uuid") PathSegment uuid) {
		Map<String, Object> response = new TreeMap<String, Object>();
		String productUuid = uuid.getPath();

		List<String> items = _productService.retrieveProductPictures(productUuid);
		if (items == null)
			return Response.serverError().build();

		response.put("pictures", items);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	// GET businesses/1.0/business/{uuid}/product/by_searchKeyword/{keyword}
	@GET
	@Path("business/{uuid}/product/by_searchKeyword/{keyword}")
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getBusinessProduct by keyword", notes = "...")
	public Response getBusinessProduct(@PathParam("uuid") PathSegment uuid, @PathParam("keyword") PathSegment keyword) {
		String businessUuid = uuid.getPath();
		String search = keyword.getPath();

		List<Product> items = _productService.retrieveProducts(businessUuid, search.trim());
		if (items == null)
			return Response.serverError().build();

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(items).build();
	}
	
	// GET businesses/1.0/product/categories
	@GET
	@Path("product/{uuid}/categories")
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "getProductCategories", notes = "...")
	public Response getProductCategories(@PathParam("uuid") PathSegment uuid) {
		String productUuid = uuid.getPath();

		List<Category> items = _productService.retrieveProductCategories(productUuid);
		if (items == null)
			return Response.serverError().build();

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(items).build();
	}
}
