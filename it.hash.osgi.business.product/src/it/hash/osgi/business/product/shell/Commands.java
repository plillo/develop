package it.hash.osgi.business.product.shell;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.service.ProductService;


public class Commands {
	private volatile ProductService _product;

	public void create(String businessUuid, String code, String barcode, String description, String longDescription) {
		Product item = new Product();
		
		item.setBusiness(businessUuid);
		item.setCode(code);
		item.setBarcode(barcode);
		item.set_locDescription(description);
		item.set_locLongDescription(longDescription);
		
		Map<String, Object> response = _product.createProduct(item);
		
		System.out.println(response.toString());
	}
	
	public void delete(String uuid) {
		Map<String, Object> list = _product.deleteProduct(uuid);
		
		System.out.println(list.get("deleted"));
	}
	
	public void list(String keyword) {
		List<Product> list = _product.retrieveProducts(keyword);
		
		System.out.println("N."+list.size()+" matching products");
	}
	
	public void listInBusiness(String businessUuid, String keyword) {
		List<Product> list = _product.retrieveProducts(businessUuid, keyword);
		
		System.out.println("N."+list.size()+" matching products");
	}
	
}
