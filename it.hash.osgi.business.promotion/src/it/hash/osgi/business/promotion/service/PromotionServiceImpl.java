package it.hash.osgi.business.promotion.service;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.service.ProductService;
import it.hash.osgi.business.promotion.Promotion;
import it.hash.osgi.business.promotion.PromotionFactory;
import it.hash.osgi.business.promotion.typeOffer;
import it.hash.osgi.business.promotion.persistence.api.PromotionServicePersistence;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.user.service.api.UserService;
import it.hash.osgi.utils.StringUtils;

@Component()
public class PromotionServiceImpl implements PromotionService {
	private volatile PromotionServicePersistence _promotionPersistenceService;
	private volatile UuidService _uuid;
	private volatile BusinessService _businessService;
	private volatile ProductService _productService;
	private volatile CategoryService _categoryService;

	@SuppressWarnings("unused")
	private volatile UserService _userSrv;
	
	private volatile EventAdmin _eventAdminService;

	@Reference(service = PromotionServicePersistence.class)
	public void setPromotionServicePersistence(PromotionServicePersistence service) {

		_promotionPersistenceService = service;
	}

	public void unsetPromotionServicePersistence(PromotionServicePersistence service) {
		_promotionPersistenceService = null;
	}

	@Reference(service = UuidService.class)
	public void setUuidService(UuidService service) {

		_uuid = service;
	}

	public void unsetUuidService(UuidService service) {
		_uuid = null;
	}

	@Reference(service = BusinessService.class)
	public void setBusinessService(BusinessService service) {

		_businessService = service;
	}

	public void unsetBusinessService(BusinessService service) {
		_businessService = null;
	}

	@Reference(service = ProductService.class)
	public void setProductService(ProductService service) {

		_productService = service;
	}

	public void unsetProductService(ProductService service) {
		_productService = null;
	}

	@Reference(service = CategoryService.class)
	public void setCategoryService(CategoryService service) {

		_categoryService = service;
	}

	public void unsetCategoryService(CategoryService service) {
		_categoryService = null;
	}
	
	@Reference(service=EventAdmin.class)
	public void setEventAdmin(EventAdmin service){
		_eventAdminService = service;
		doLog("eventAdminService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetEventAdmin(EventAdmin service){
		_eventAdminService =null;
		doLog("eventAdminService: "+(service==null?"NULL":"released"));
	}
	

	
	
	@Override
	public Map<String, Object> createPromotion(String businessUuid, Promotion promotion) {
		Map<String, Object> response = new HashMap<String, Object>();
		String uuid = _uuid.createUUID("app/business.promotion");
		if (!StringUtils.isEmptyOrNull(uuid)) {
			promotion.setUuid(uuid);

			try {
				response = _promotionPersistenceService.addPromotion(promotion);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!(Boolean) response.get("status").equals(Status.CREATED.getCode()))
				_uuid.removeUUID(uuid);

		} else {
			response.put("status", Status.ERROR_GENERATING_UUID.getCode());
			response.put("message", Status.ERROR_GENERATING_UUID.getMessage());

		}

		return response;
	}

	private Map<String, Object> retrieveDetailsBusiness(String businessUuid) {
		Map<String, Object> pars = new HashMap<String, Object>();
		Business business = _businessService.getBusiness(businessUuid);
		if (business != null) {
			if (!StringUtils.isEON(business.getUuid()))
				pars.put("businessUuid", business.getUuid());
			if (!StringUtils.isEON(business.getName()))
				pars.put("businessName", business.getName());
			if (!StringUtils.isEON(business.getPIva()))
				pars.put("businessPIva", business.getPIva());
			if (!StringUtils.isEON(business.getFiscalCode()))
				pars.put("businessFiscalCode", business.getFiscalCode());
			if (!StringUtils.isEON(business.getAddress()))
				pars.put("businessAddress", business.getAddress());
			if (!StringUtils.isEON(business.getCity()))
				pars.put("businessCity", business.getCity());
			if (!StringUtils.isEON(business.getCap()))
				pars.put("businessCap", business.getCap());
			if (!StringUtils.isEON(business.getNation()))
				pars.put("businessNation", business.getNation());

			return pars;
		}
		return null;
	}

	private List<Product> retrieveDetailsProducts(Map<String, Object> map) {

		List<Product> listProduct = new ArrayList<Product>();
		JSONObject obj = new JSONObject(map);
		if (obj != null) {
			JSONArray items = obj.optJSONArray("products");

			if (items != null) {
				for (int i = 0; i < items.length(); i++) {
					String current = items.optString(i);
					Product p = this._productService.getProduct(current);
					listProduct.add(p);
				}
			}
		}

		return listProduct;
	}

	private List<Category> retrieveDetailsCategories(Map<String, Object> map) {

		List<Category> listCategory = new ArrayList<Category>();
		JSONObject obj = new JSONObject(map);
		if (obj != null) {
			JSONArray items = obj.optJSONArray("categories");
			if (items != null) {
				for (int i = 0; i < items.length(); i++) {
					String current = items.optString(i);
					Category p = this._categoryService.getCategoryByUuid(current);
					listCategory.add(p);
				}
			}
		}

		return listCategory;

	}

	@Override
	public Map<String, Object> createPromotion(String businessUuid, Map<String, Object> pars) {
		Map<String, Object> response = new HashMap<String, Object>();

		Map<String, Object> business = this.retrieveDetailsBusiness(businessUuid);
		if (business == null) {
			pars.put("status", Status.ERROR_UNMATCHED_BUSINESS.getCode());
			pars.put("message", Status.ERROR_UNMATCHED_BUSINESS.getMessage());
			return pars;
		}

		pars.putAll(business);
		pars.put("active", true);
		if (pars.get("type").equals(typeOffer.LastMinute.getMessage())) {

			List<Product> products = this.retrieveDetailsProducts(pars);
			if (!products.isEmpty()) {
				pars.put("products", products);
			} else
				pars.put("products", null);

			List<Category> categories = this.retrieveDetailsCategories(pars);
			if (!categories.isEmpty())
				pars.put("categories", categories);
			else
				pars.put("categories", null);
		}

		String u = _uuid.createUUID("app/business.promotion");
		if (!StringUtils.isEmptyOrNull(u)) {
			pars.put("uuid", u);

			response = _promotionPersistenceService.addPromotion(pars);

			if (!(Boolean) response.get("status").equals(Status.CREATED.getCode()))
				_uuid.removeUUID(u);

			// TODO INTEGRITA' REFERENZIALE

		} else {

			response.put("status", Status.ERROR_GENERATING_UUID.getCode());
			response.put("message", Status.ERROR_GENERATING_UUID.getMessage());
			return response;

		}

		// TODO processare prodotti

		return response;
	}

	@Override
	public Map<String, Object> getPromotion(Map<String, Object> pars) {
		return _promotionPersistenceService.getPromotion(pars);
	}

	@Override
	public Map<String, Object> deletePromotion(String uuid) {
		Map<String, Object> response = null;

		if (!StringUtils.isEmptyOrNull(uuid))
			response = _uuid.removeUUID(uuid);

		if (response != null) {
			if ((Boolean) response.get("deleted"))
				return _promotionPersistenceService.deletePromotion(uuid);

		}
		return response;
	}

	@Override
	public Map<String, Object> updatePromotion(Promotion promotion) {
		return _promotionPersistenceService.updatePromotion(promotion);
	}

	@Override
	public Map<String, Object> updatePromotion(String uuid, Map<String, Object> pars) {
		return _promotionPersistenceService.updatePromotion(uuid, pars);
	}

	/*
	 * @Override public List<Promotion> retrievePromotions(String criterion,
	 * String search) { if(_uuid.isUUID(search)){ List<Promotion> list = new
	 * ArrayList<Promotion>();
	 * list.add(_promotionPersistenceService.getPromotionByBusinessUuid(search))
	 * ; return list; } else return
	 * _promotionPersistenceService.retrievePromotions(criterion, search); }
	 */
	@Override
	public List<Promotion> getPromotions() {
		return _promotionPersistenceService.getPromotions();
	}

	@Override
	public Map<String, Object> updatePromotionLogo(String uuid, String type, byte[] encodeBase64) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updatePromotionLogo(String uuid, String type, InputStream istream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> retrievepromotions(String criterion, String search) {

		return _promotionPersistenceService.retrievePromotions(criterion, search);
	}

	@Override
	public Promotion getPromotion(String uuid) {

		return this._promotionPersistenceService.getPromotionByUuid(uuid);
	}

	@Override
	public Promotion getPromotion(String uuid, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> retrievePromotion(String search) {

		return _promotionPersistenceService.retrievePromotions(search);

	}

	@Override
	public List<Promotion> getPromotionsByBusinessUuid(String businessUuid) {

		return _promotionPersistenceService.getPromotionByBusinessUuid(businessUuid);
	}

	@Override
	public Map<String, Object> updateActive(String uuid, Boolean activate) {
		return _promotionPersistenceService.updateActivate(uuid, activate);
	}

	@Override
	public Map<String, Object> addPicture(String promotionUuid, String pictureUuid) {
		// TODO Auto-generated method stub
		return null;
	}
	   private void doLog(String message) {
	        System.out.println("## [" + this.getClass() + "] " + message);
	    }
}
