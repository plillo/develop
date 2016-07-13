package it.hash.osgi.business.category.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.swagger.annotations.Api;
import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;

@Api
@Path("businesses/1.0/categories")
@Component(service = Resources.class)
public class Resources {
	// References
	private CategoryService _categoryService;
	
	@Reference(service=CategoryService.class)
	public void setCategoryService(CategoryService service){
		_categoryService = service;
		doLog("CategoryService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetCategoryService(CategoryService service){
		doLog("CategoryService: "+(service==null?"NULL":"released"));
		_categoryService = null;
	}
 
	@GET
	@Path("by_searchKeyword/{keyword}")  
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategory(@PathParam("keyword") PathSegment s) {
		String search = s.getPath();
		String criterion = s.getMatrixParameters().getFirst("criterion");
		List<Category> items = _categoryService.retrieveCategories(criterion, search);
		
		if (items == null) {
			return Response.serverError().build();
		}
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(items).build();
	}

    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}