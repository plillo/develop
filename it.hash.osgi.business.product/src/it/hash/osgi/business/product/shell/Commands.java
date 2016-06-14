package it.hash.osgi.business.product.shell;

import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.service.ProductService;

@Component(
	immediate=true, 
	service = Commands.class, 
	property = {
		CommandProcessor.COMMAND_SCOPE+"=product",
		CommandProcessor.COMMAND_FUNCTION+"=create",
		CommandProcessor.COMMAND_FUNCTION+"=delete",
		CommandProcessor.COMMAND_FUNCTION+"=list"
	}
)
public class Commands {
	// References
	private ProductService _productService;
	
	@Reference(service=ProductService.class)
	public void setProductService(ProductService service){
		_productService = service;
		doLog("ProductService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetProductService(ProductService service){
		doLog("ProductService: "+(service==null?"NULL":"released"));
		_productService = null;
	}
	// === end references

	public void create(String businessUuid, String code, String barcode, String description, String longDescription) {
		Product item = new Product();
		
		item.setBusiness(businessUuid);
		item.setCode(code);
		item.setBarcode(barcode);
		item.set_locDescription(description);
		item.set_locLongDescription(longDescription);
		
		Map<String, Object> response = _productService.createProduct(item);
		
		System.out.println(response.toString());
	}
	
	public void delete(String uuid) {
		Map<String, Object> list = _productService.deleteProduct(uuid);
		
		System.out.println(list.get("deleted"));
	}
	
	public void list(String keyword) {
		List<Product> list = _productService.retrieveProducts(keyword);
		
		System.out.println("N."+list.size()+" matching products");
	}
	
	public void listInBusiness(String businessUuid, String keyword) {
		List<Product> list = _productService.retrieveProducts(businessUuid, keyword);
		
		System.out.println("N."+list.size()+" matching products");
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
