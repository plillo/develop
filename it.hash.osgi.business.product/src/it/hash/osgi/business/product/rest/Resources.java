package it.hash.osgi.business.product.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import eu.medsea.mimeutil.detector.MimeDetector;
import io.swagger.annotations.Api;
import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.service.ProductService;

@Api
@Path("businesses/1.0/")
public class Resources {
	private volatile ProductService _productService;
	
	// PUT businesses/1.0/product
	@PUT
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


	// PUT businesses/1.0/business/{buuid}/product/{puuid}/picture
	@PUT
	@Path("business/{buuid}/product/{puuid}/picture")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @io.swagger.annotations.ApiOperation(value = "create", notes = "...")
	public Response create(
			@PathParam("buuid") PathSegment buuid,
			@PathParam("puuid") PathSegment puuid,
			@FormDataParam("picture") InputStream inputStream,
			@FormDataParam("picture") FormDataBodyPart body,
			@FormDataParam("picture") FormDataContentDisposition fileDetail) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		try {
			String businessUuid = buuid.getPath();
			String productUuid = puuid.getPath();
			
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
				response = _productService.addPicture(businessUuid, productUuid, awsBucketReference);
			}
		} catch (IOException e) {
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
}
