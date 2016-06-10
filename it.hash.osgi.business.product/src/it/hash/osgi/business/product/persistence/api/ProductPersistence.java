package it.hash.osgi.business.product.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.product.Product;


public interface ProductPersistence {
	// CREATE
	Map<String, Object> createProduct(Map<String, Object> item);
	Map<String, Object> createProduct(Product item);
	             
	// READ
	Map<String, Object> getProduct(Product item);
	Map<String, Object> getProduct(Map<String, Object> item);
	
	Product getProductByUuid(String itemUuid);
	
	List<Product> retrieveProducts(String uuid, String keyword);
	List<Product> retrieveProducts(String keyword);
	
	// UPDATE
	Map<String, Object> updateProduct(Product item);
	Map<String, Object> updateProduct(Map<String, Object> item);
	
	// DELETE
	Map<String, Object> deleteProduct(String uuid);
	
	// ADD PICTURE
	Map<String, Object> addPicture(String productUuid, String pictureUuid);
	
	// IMPLEMENTATION
	String getImplementation();

}
