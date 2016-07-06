/**
 * Persistence Api
 * @author Montinari Antonella
 */
package it.hash.osgi.business.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;


/**
 * Provides interfaces for the management of the persistence of a business
 * @author Montinari Antonella
 */
public interface BusinessPersistenceService {
	// CREATE
	Map<String, Object> addBusiness(Map<String, Object> business);
	Map<String, Object> addBusiness(Business business);
	             
	// READ
	Map<String, Object> getBusiness(Business business);
	Map<String, Object> getBusiness(Business business, boolean withLogo);
	Map<String, Object> getBusiness(Map<String, Object> business);
	Map<String, Object> getBusiness(Map<String, Object> business, boolean withLogo);
	
	Business getBusinessByFiscalCode(String fiscalCode);
	Business getBusinessByFiscalCode(String fiscalCode, boolean withLogo);
	Business getBusinessByPartitaIva(String partitaIva);
	Business getBusinessByPartitaIva(String partitaIva, boolean withLogo);
	Business getBusinessByName(String Name);
	Business getBusinessByName(String Name, boolean withLogo);
	Business getBusinessById(String businessId);
	Business getBusinessById(String businessId, boolean withLogo);
	Business getBusinessByUuid(String uuid);
	Business getBusinessByUuid(String uuid, boolean withLogo);
	
	List<Business> getBusinesses();
	List<Business> getBusinessDetails(Business business);
	List<Business> retrieveBusinesses(String criterion, String search);
	
	List<Business> retrieveFollowedByUser(String uuid);
	List<Business> retrieveFollowedByUser(String uuid, boolean withLogo);
	List<Business> retrieveOwnedByUser(String uuid);
	List<Business> retrieveOwnedByUser(String uuid, boolean withLogo);
	List<Business> retrieveNotFollowedByUser(String userUuid, String search);
	List<Business> retrieveNotFollowedByUser(String userUuid, String search, boolean withLogo);
	
	Map<String, Object> retrieveSubscriptionRules(String businessUuid, String userUuid);
	Map<String, Object> setSubscriptionRule(String businessUuid, String userUuid, String rule, Boolean set);

	// UPDATE
	Map<String, Object> updateBusiness(String uuid, Business business);
	Map<String, Object> updateBusiness(String uuid, Map<String, Object> business);
	Map<String, Object> follow(String businessUuid, String userUuid);
	Map<String, Object> unFollow(String businessUuid, String userUuid);
	Map<String, Object> updateBusinessLogo(String uuid, String type, byte[] encodeBase64);

	// DELETE
	Map<String, Object> deleteBusiness(String uuid);
	Map<String, Object> deleteBusinessById(String id);
	
	// IMPLEMENTATION
	String getImplementation();
}
