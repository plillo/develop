package it.hash.osgi.business.product.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.product.Product;

public interface ProductService {
	Product getProduct(Product item);
	List<Product> getProduct(String search);
	Map<String, Object> getProduct(Map<String, Object> pars);
	Map<String, Object> createProduct(Product item);
	Map<String, Object> updateProduct(Product item);
	Map<String, Object> deleteProduct(String uuid);
	
	Map<String, Object> createProduct(Map<String, Object> pars);
	Map<String, Object> updateProduct(Map<String, Object> pars);

	List<Product> retrieveProducts(String businessUuid, String keyword);
	List<Product> retrieveProducts(String keyword);
	Map<String, Object> addPicture(String productUuid, String string);
	List<Category> retrieveProductCategories(String productUuid);
	List<String> retrieveProductPictures(String retrieveProductPictures);
}
