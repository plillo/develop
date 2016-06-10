package it.hash.osgi.business.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.geojson.Coordinates;
import it.hash.osgi.geojson.Point;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.service.api.UserService;
import it.hash.osgi.utils.StringUtils;

public class BusinessServiceImpl implements BusinessService {
	private volatile BusinessServicePersistence _businessPersistenceService;
	private volatile UUIDService _uuid;

	@SuppressWarnings("unused")
	private volatile UserService _userSrv;
   
	@SuppressWarnings("unused")
	private volatile EventAdmin _eventAdminService;
	
	@Override
	public Map<String, Object> getBusiness(Map<String, Object> pars) {
		return _businessPersistenceService.getBusiness(pars);
	}

	@Override
	public Map<String, Object> createBusiness(Business business) {
		Map<String, Object> response = new HashMap<String, Object>();
		String u = _uuid.createUUID("app/business");
		if (!StringUtils.isEmptyOrNull(u)) {
			business.setUuid(u);

			response = _businessPersistenceService.addBusiness(business);
			if ((Boolean) response.get("created") == false) 
				_uuid.removeUUID(u);

		} else {
			response.put("created", false);
			response.put("returnCode", 630);
		}
		return response;
	}

	@Override
	public Map<String, Object> createBusiness(Map<String, Object> pars) {
		String u = _uuid.createUUID("app/business");
		Map<String, Object> response = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(u)) {
			pars.put("uuid", u);
			response = _businessPersistenceService.addBusiness(pars);

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
	public Map<String, Object> deleteBusiness(String uuid) {
		Map<String, Object> response = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(uuid)) {
			response = _uuid.removeUUID(uuid);

			return _businessPersistenceService.deleteBusiness(uuid);
		} else {
			response.put("created", false);
			response.put("errorUUIDService", true);
			response.put("returnCode", 630);
			return response;
		}
	}

	@Override
	public Map<String, Object> updateBusiness(String uuid, Business business) {
		return _businessPersistenceService.updateBusiness(uuid, business);
	}
	
	@Override
	public Map<String, Object> updateBusiness(String uuid, Map<String, Object> pars) {
		return _businessPersistenceService.updateBusiness(uuid, pars);
	}
	
	@Override
	public Map<String, Object> updateBusinessLogo(String uuid, String type, byte[] encodeBase64) {
		return _businessPersistenceService.updateBusinessLogo(uuid, type, encodeBase64);
	}
	
	@Override
	public Map<String, Object> updateBusinessLogo(String uuid, String type, InputStream istream) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			return updateBusinessLogo(uuid, type, IOUtils.toByteArray(istream));
		} catch (IOException e) {
			response.put("update", "ERROR");
			response.put("returnCode", 610);
		}
		return response;
	}

	@Override
	public List<Business> retrieveBusinesses(String criterion, String search) {
		if(_uuid.isUUID(search)){
			List<Business> list = new ArrayList<Business>();
			list.add(_businessPersistenceService.getBusinessByUuid(search));
			return list;
		}
		else
			return _businessPersistenceService.retrieveBusinesses(criterion, search);
	}

	@Override
	public List<Business> getBusinesses() {
		return _businessPersistenceService.getBusinesses();
	}
	
	@Override
	public Business getBusiness(String uuid, boolean withLogo){
		return _businessPersistenceService.getBusinessByUuid(uuid, withLogo);
	}
	
	@Override
	public Business getBusiness(String uuid){
		return _businessPersistenceService.getBusinessByUuid(uuid);
	}
	
	@Override
	public Map<String, Object> updateFollowersToBusiness(Map<String, Object> pars) {
		return _businessPersistenceService.updateBusiness("", pars);
	}

	@Override
	public Map<String, Object> follow(String businessUuid, String userUuid) {
		return _businessPersistenceService.follow(businessUuid, userUuid);
	}

	@Override
	public Map<String, Object> unfollow(String businessUuid, String userUuid) {
		return _businessPersistenceService.unFollow(businessUuid, userUuid);
	}

	@Override
	public List<Business> retrieveFollowedByUser(String uuid) {
		return _businessPersistenceService.retrieveFollowedByUser(uuid);
	}
	
	@Override
	public List<Business> retrieveFollowedByUser(String uuid, boolean withLogo) {
		return _businessPersistenceService.retrieveFollowedByUser(uuid, withLogo);
	}

	@Override
	public List<Business> retrieveOwnedByUser(String uuid) {
		return _businessPersistenceService.retrieveOwnedByUser(uuid);
	}
	
	@Override
	public List<Business> retrieveOwnedByUser(String uuid, boolean withLogo) {
		return _businessPersistenceService.retrieveOwnedByUser(uuid, withLogo);
	}

	@Override
	public List<Business> retrieveNotFollowedByUser(String uuid, String search) {
		return _businessPersistenceService.retrieveNotFollowedByUser(uuid, search);
	}
	
	@Override
	public List<Business> retrieveNotFollowedByUser(String uuid, String search, boolean withLogo) {
		return _businessPersistenceService.retrieveNotFollowedByUser(uuid, search, withLogo);
	}

	@Override
	public Coordinates getPosition(String businessUuid) {
		Business business = _businessPersistenceService.getBusinessByUuid(businessUuid);
		
		Point position = business.getPosition();
		
		if(position==null)
			return null;
		
		return business.getPosition().getCoordinates();
	}
}
