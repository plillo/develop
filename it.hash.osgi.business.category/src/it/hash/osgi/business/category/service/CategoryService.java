package it.hash.osgi.business.category.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.user.attribute.Attribute;

public interface CategoryService {
	Category getCategory(Category category);
	List<Category> getCategory(String search);
	Category getCategoryByUuid(String uuid);
	List<Category> getCategoryByUuid(List<String> uuids);
	Map<String, Object> createCategory(Category category);
	Map<String, Object> updateCategory(Category category);
	Map<String, Object> deleteCategory(String uuid);
	
	Map<String, Object> createCategory(Map<String, Object> pars);
	Map<String, Object> updateCategory(Map<String, Object> pars);

	List<Category> retrieveCategories(String type,String criterion);
	 
	List<Attribute> getAttributes(String ctgUuid);
	Map<String, Object> createAttribute(String ctgUuid, Attribute attribute);
	Map<String, Object> updateAttribute(String ctgUuid, Attribute attribute);
	Map<String, Object> deleteAttribute(String ctgUuid, String attrUuid);
	boolean createCollectionByCsv(String url,String nameFile) ;
	
}
