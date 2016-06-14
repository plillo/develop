package it.hash.osgi.business.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.persistence.api.ProductPersistence;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.utils.StringUtils;

@Component(immediate=true)
public class ProductServiceImpl implements ProductService{
	// References
	// ==========
	private CategoryService _categoryService;
	private ProductPersistence _productPersistenceService;
	private UuidService _uuidService;
   
	@Reference(service=CategoryService.class)
	public void setCategoryService(CategoryService service){
		_categoryService = service;
		doLog("CategoryService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetCategoryService(CategoryService service){
		doLog("CategoryService: "+(service==null?"NULL":"released"));
		_categoryService = null;
	}
	
	@Reference(service=ProductPersistence.class)
	public void setProductPersistence(ProductPersistence service){
		_productPersistenceService = service;
		doLog("ProductPersistenceService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetProductPersistence(ProductPersistence service){
		doLog("ProductPersistenceService: "+(service==null?"NULL":"released"));
		_productPersistenceService = null;
	}
	
	@Reference(service=UuidService.class)
	public void setUuidService(UuidService service){
		_uuidService = service;
		doLog("JWTService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUuidService(UuidService service){
		doLog("JWTService: "+(service==null?"NULL":"released"));
		_uuidService = null;
	}
	
	@Override
	public Map<String, Object> getProduct(Map<String, Object> pars) {
		return _productPersistenceService.getProduct(pars);
	}

	
	
	// CREATE PRODUCT
	@Override
	public Map<String, Object> createProduct(Product item) {
		Map<String, Object> response = new HashMap<String, Object>();
		String u = _uuidService.createUUID("app/product");
		if (!StringUtils.isEmptyOrNull(u)) {
			item.setUuid(u);

			response = _productPersistenceService.createProduct(item);
			if ((Boolean) response.get("created") == false) 
				_uuidService.removeUUID(u);

		} else {
			response.put("created", false);
			response.put("returnCode", 630);
		}
		return response;
	}
	

	@Override
	public Product getProduct(Product item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProduct(String search) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<String, Object> updateProduct(Product item) {
		return _productPersistenceService.updateProduct(item);
	}

	@Override
	public Map<String, Object> deleteProduct(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> createProduct(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateProduct(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> retrieveProducts(String keyword) {
		return _productPersistenceService.retrieveProducts(keyword);
	}
	
	@Override
	public List<Product> retrieveProducts(String businessUuid, String keyword) {
		return _productPersistenceService.retrieveProducts(businessUuid, keyword);
	}

	@Override
	public Map<String, Object> addPicture(String productUuid, String pictureUuid) {
		return _productPersistenceService.addPicture(productUuid, pictureUuid);
	}

	@Override
	public List<Category> retrieveProductCategories(String productUuid) {
		Product item = _productPersistenceService.getProductByUuid(productUuid);
		if(item!=null && item.getCategories()!=null)
			return _categoryService.getCategoryByUuid(item.getCategories());

		return null;
	}

	@Override
	public List<String> retrieveProductPictures(String productUuid) {
		Product item = _productPersistenceService.getProductByUuid(productUuid);
		if(item!=null && item.getPictures()!=null)
			return item.getPictures();

		// return empty list
		return new ArrayList<>();
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }

}
