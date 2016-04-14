package it.hash.osgi.business.category.shell;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.user.attribute.Attribute;

public class Commands {
	private volatile CategoryService _category;
	public void createCollectionByCsv(String url, String fileName){
	//	 url="file:\\C|\\Users\\Antonella\\git\\osgi-platform\\a-node\\src\\it\\hash\\osgi\\business\\category\\service";
	//	 fileName="categories.csv";
		System.out.println(" Collection created: "+_category.createCollectionByCsv(url,fileName));
		 
		
	}
	public void addCategory(String name, String code, String description, String longDescription) {
		Category category = new Category();
		category.setName(name);
		category.setCode(code);
		category.set_locDescription(description);
		category.set_locLongDescription(longDescription);
		_category.createCategory(category);
	}
	
	public void deleteCategory(String uuid) {
		Map<String, Object> list = _category.deleteCategory(uuid);
		
		System.out.println(list.get("deleted"));
	}
	
	public void retrieveCategories() {
		List<Category> list = _category.retrieveCategories("","");
		
		System.out.println(list.toString());
	}

	public void createAttribute(String ctgUuid, String name, String label) {
		Attribute attribute = new Attribute();
		attribute.setName(name);
		attribute.setLabel(label);

		_category.createAttribute(ctgUuid, attribute);
	}
}
