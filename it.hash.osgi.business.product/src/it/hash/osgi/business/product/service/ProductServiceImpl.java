package it.hash.osgi.business.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.persistence.api.ProductPersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.utils.StringUtils;

public class ProductServiceImpl implements ProductService{
	private volatile ProductPersistence _productPersistence;
	private volatile UUIDService _uuid;

	@SuppressWarnings("unused")
	private volatile UUIDService _userSrv;
   
	@Override
	public Map<String, Object> getProduct(Map<String, Object> pars) {
		return _productPersistence.getProduct(pars);
	}

	@Override
	public Map<String, Object> createProduct(Product item) {
		Map<String, Object> response = new HashMap<String, Object>();
		String u = _uuid.createUUID("app/product");
		if (!StringUtils.isEmptyOrNull(u)) {
			item.setUuid(u);

			response = _productPersistence.createProduct(item);
			if ((Boolean) response.get("created") == false) 
				_uuid.removeUUID(u);

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
		// TODO Auto-generated method stub
		return null;
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
		return _productPersistence.retrieveProducts(keyword);
	}
	
	@Override
	public List<Product> retrieveProducts(String businessUuid, String keyword) {
		return _productPersistence.retrieveProducts(businessUuid, keyword);
	}

}
