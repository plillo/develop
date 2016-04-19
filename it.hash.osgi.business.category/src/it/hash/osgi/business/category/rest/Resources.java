package it.hash.osgi.business.category.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;

@Api
@Path("businesses/1.0/categories")
public class Resources {

	private volatile CategoryService _categoryService;
 
	// cerca in tutta la collezione la stringa search
	@GET
	@Path("by_searchKeyword/{keyword}")  
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategory(@PathParam("keyword") PathSegment s) {
		System.out.println(" ");
		
		List<Category> categories = new ArrayList<Category>();
		
		String search = s.getPath();
		String criterion = s.getMatrixParameters().getFirst("criterion");
		categories = _categoryService.retrieveCategories(criterion,search);
		
		if (categories == null) {
			return Response.serverError().build();
		}
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(categories).build();
	}


}