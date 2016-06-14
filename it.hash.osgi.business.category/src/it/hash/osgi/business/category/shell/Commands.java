package it.hash.osgi.business.category.shell;

import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.parser.ParserManager;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.user.attribute.Attribute;

@Component(
	immediate=true, 
	service = Commands.class, 
	property = {
		CommandProcessor.COMMAND_SCOPE+"=category",
		CommandProcessor.COMMAND_FUNCTION+"=create",
		CommandProcessor.COMMAND_FUNCTION+"=delete",
		CommandProcessor.COMMAND_FUNCTION+"=list",
		CommandProcessor.COMMAND_FUNCTION+"=retrieve",
		CommandProcessor.COMMAND_FUNCTION+"=createByUrl",
		CommandProcessor.COMMAND_FUNCTION+"=createAttribute"
	}
)
public class Commands {
	// References
	private CategoryService _categoryService;
	private ParserManager _parserManager;

	@Reference(service=CategoryService.class)
	public void setCategoryService(CategoryService service){
		_categoryService = service;
		doLog("CategoryService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetCategoryService(CategoryService service){
		doLog("CategoryService: "+(service==null?"NULL":"released"));
		_categoryService = null;
	}
	
	@Reference(service=ParserManager.class)
	public void setParserManager(ParserManager service){
		_parserManager = service;
		doLog("ParserManager: "+(service==null?"NULL":"got"));
	}
	
	public void unsetParserManager(ParserManager service){
		doLog("ParserManager: "+(service==null?"NULL":"released"));
		_parserManager = null;
	}
	// === end references
	
	public void createByUrl(String type, String url) {
		System.out.println(String.format("Creating categories by URL [%s]: %b", url, _parserManager.addCategoriesByUrl(type, url)));
	}
		
	public void create(String name, String code, String description, String longDescription) {
		Category category = new Category();
		category.setName(name);
		category.setCode(code);
		category.set_locDescription(description);
		category.set_locLongDescription(longDescription);
		
		_categoryService.createCategory(category);
	}
	
	public void delete(String uuid) {
		Map<String, Object> list = _categoryService.deleteCategory(uuid);
		
		System.out.println(list.get("deleted"));
	}
	
	public void retrieve() {
		List<Category> list = _categoryService.retrieveCategories("","");
		
		System.out.println(list.toString());
	}

	public void createAttribute(String ctgUuid, String name, String label) {
		Attribute attribute = new Attribute();
		attribute.setName(name);
		attribute.setLabel(label);

		_categoryService.createAttribute(ctgUuid, attribute);
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
