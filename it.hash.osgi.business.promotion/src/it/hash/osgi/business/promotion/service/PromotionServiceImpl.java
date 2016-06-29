package it.hash.osgi.business.promotion.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.promotion.Promotion;
import it.hash.osgi.business.promotion.persistence.api.PromotionServicePersistence;
import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.user.service.api.UserService;
import it.hash.osgi.utils.StringUtils;

@Component()
public class PromotionServiceImpl implements PromotionService {
	private volatile PromotionServicePersistence _promotionPersistenceService;
	private volatile UuidService _uuid;

	@SuppressWarnings("unused")
	private volatile UserService _userSrv;
	// TODO non iniettate !!!
	@SuppressWarnings("unused")
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

	@Override
	public Map<String, Object> createPromotion(Promotion promotion) {
		Map<String, Object> response = new HashMap<String, Object>();
		String uuid = _uuid.createUUID("app/business.promotion");
		if (!StringUtils.isEmptyOrNull(uuid)) {
			promotion.setUuid(uuid);

			response = _promotionPersistenceService.addPromotion(promotion);
			
			if ((Boolean) response.get("created") == false)
				_uuid.removeUUID(uuid);

		} else {
			response.put("status", Status.ERROR_GENERATING_UUID.getCode());
			response.put("message", Status.ERROR_GENERATING_UUID.getMessage());
			
		}

		return response;
	}

	@Override
	public Map<String, Object> createPromotion(Map<String, Object> pars) {
		String u = _uuid.createUUID("app/business.promotion");
		Map<String, Object> response = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(u)) {
			pars.put("uuid", u);
			response = _promotionPersistenceService.addPromotion(pars);

			if ((Boolean) response.get("created") == false)
				_uuid.removeUUID(u);

			// TODO INTEGRITA' REFERENZIALE

		} else {

			response.put("created", false);
			response.put("returnCode", 630);

		}
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
			else {
				response.put("deleted", false);
				response.put("errorUUIDService", true);
				response.put("returnCode", 630);

			}
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
		// TODO Auto-generated method stub
		return this._promotionPersistenceService.getPromotionByUuid(uuid);
	}

	@Override
	public Promotion getPromotion(String uuid, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> retrievePromotion(String search) {
		// TODO Auto-generated method stub
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

	/*
	 * @Override public Promotion getPromotionByBusinessFiscalCode(String
	 * BusinessFiscalCode) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public Promotion getPromotionByBusinessPartitaIva(String
	 * partitaIva) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public Promotion getPromotionByBusinessName(String Name) { //
	 * TODO Auto-generated method stub return null; }
	 * 
	 * @Override public Promotion getPromotionByBusinessName(String Name,
	 * boolean withLogo) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public Promotion getPromotionByBusinessId(String businessId) {
	 * // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public Promotion getPromotionByBusinessId(String businessId,
	 * boolean withLogo) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public Promotion getPromotionByBusinessUuid(String uuid,
	 * boolean withLogo) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public List<Promotion> retrievePromotion(String search) { //
	 * TODO Auto-generated method stub return null; }
	 * 
	 * /*@Override public Business getBusiness(String uuid, boolean withLogo){
	 * return _businessPersistenceService.getBusinessByUuid(uuid, withLogo); }
	 * 
	 * @Override public Business getBusiness(String uuid){ return
	 * _businessPersistenceService.getBusinessByUuid(uuid); }
	 * 
	 * @Override public Map<String, Object>
	 * updateFollowersToBusiness(Map<String, Object> pars) { return
	 * _businessPersistenceService.updateBusiness("", pars); }
	 * 
	 * @Override public Map<String, Object> follow(String businessUuid, String
	 * userUuid) { return _businessPersistenceService.follow(businessUuid,
	 * userUuid); }
	 * 
	 * @Override public Map<String, Object> unfollow(String businessUuid, String
	 * userUuid) { return _businessPersistenceService.unFollow(businessUuid,
	 * userUuid); }
	 * 
	 * @Override public List<Business> retrieveFollowedByUser(String uuid) {
	 * return _businessPersistenceService.retrieveFollowedByUser(uuid); }
	 * 
	 * @Override public List<Business> retrieveFollowedByUser(String uuid,
	 * boolean withLogo) { return
	 * _businessPersistenceService.retrieveFollowedByUser(uuid, withLogo); }
	 * 
	 * @Override public List<Business> retrieveOwnedByUser(String uuid) { return
	 * _businessPersistenceService.retrieveOwnedByUser(uuid); }
	 * 
	 * @Override public List<Business> retrieveOwnedByUser(String uuid, boolean
	 * withLogo) { return _businessPersistenceService.retrieveOwnedByUser(uuid,
	 * withLogo); }
	 * 
	 * @Override public List<Business> retrieveNotFollowedByUser(String uuid,
	 * String search) { return
	 * _businessPersistenceService.retrieveNotFollowedByUser(uuid, search); }
	 * 
	 * @Override public List<Business> retrieveNotFollowedByUser(String uuid,
	 * String search, boolean withLogo) { return
	 * _businessPersistenceService.retrieveNotFollowedByUser(uuid, search,
	 * withLogo); }
	 */
}
