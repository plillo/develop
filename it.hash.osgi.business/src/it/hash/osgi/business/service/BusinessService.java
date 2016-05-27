package it.hash.osgi.business.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;
import it.hash.osgi.geojson.Coordinates;

public interface BusinessService {
	Map<String, Object> getBusiness(Map<String, Object> pars);
	
	Map<String, Object> createBusiness(Business business);
	Map<String, Object> createBusiness(Map<String, Object> pars);
	Map<String, Object> updateBusiness(String uuid, Business business);
	Map<String, Object> updateBusiness(String uuid, Map<String, Object> pars);
	Map<String, Object> deleteBusiness(String uuid);
	Map<String, Object> updateBusinessLogo(String uuid, String type, byte[] encodeBase64);
	Map<String, Object> updateBusinessLogo(String uuid, String type, InputStream istream);

	Map<String, Object> updateFollowersToBusiness(Map<String, Object> pars);
	List<Business> retrieveBusinesses(String criterion, String search);
	List<Business> getBusinesses();
	Business getBusiness(String uuid);
	Business getBusiness(String uuid, boolean withLogo);
	Coordinates getPosition(String businessUuid);
	Map<String, Object> follow(String businessUuid, String userUuid);
	Map<String, Object> unfollow(String businessUuid, String userUuid);
	List<Business> retrieveFollowedByUser(String uuid);
	List<Business> retrieveFollowedByUser(String uuid, boolean withLogo);
	List<Business> retrieveOwnedByUser(String uuid);
	List<Business> retrieveOwnedByUser(String uuid, boolean withLogo);
	List<Business> retrieveNotFollowedByUser(String user, String search);
	List<Business> retrieveNotFollowedByUser(String user, String search, boolean withLogo);
}