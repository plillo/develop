package it.hash.osgi.business.category.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
 
public interface CategoryPersistence {
	// CREATE
	Map<String, Object> createCategory(Map<String, Object> category);
	Map<String, Object> createCategory(Category category);
	             
	// READ
	Map<String, Object> getCategory(Category category);
	Map<String, Object> getCategory(Map<String, Object> category);
	
	Category getCategoryByUUID(String categoryUuid);
	
	List<Category> retrieveCategories(String type,String criterion);
	
	// UPDATE
	Map<String, Object> updateCategory(Category category);
	Map<String, Object> updateCategory(Map<String, Object> category);
	
	// DELETE
	Map<String, Object> deleteCategory(String uuid);
	
	// IMPLEMENTATION
	String getImplementation();
}
