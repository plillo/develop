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
public interface BusinessServicePersistence {
	// CREATE
	Map<String, Object> addBusiness(Map<String, Object> business);
	Map<String, Object> addBusiness(Business business);
	             
	// READ
	Map<String, Object> getBusiness(Business business);
	Map<String, Object> getBusiness(Map<String, Object> business);
	
	Business getBusinessByFiscalCode(String fiscalCode);
	Business getBusinessByPartitaIva(String partitaIva);
	Business getBusinessByName(String Name);
	Business getBusinessById(String businessId);
	Business getBusinessByUuid(String uuid);
	
	List<Business> getBusinesses();
	List<Business> getBusinessDetails(Business business);
	List<Business> retrieveBusinesses(String criterion, String search);
	
	List<Business> retrieveFollowedByUser(String uuid);
	List<Business> retrieveOwnedByUser(String uuid);
	List<Business> retrieveNotFollowedByUser(String userUuid, String search);
	
	// UPDATE
	Map<String, Object> updateBusiness(String uuid, Business business);
	Map<String, Object> updateBusiness(String uuid, Map<String, Object> business);
	Map<String, Object> follow(String businessUuid, String userUuid);
	Map<String, Object> unFollow(String businessUuid, String userUuid);
	Map<String, Object> updateBusinessLogo(String uuid, String type, byte[] encodeBase64);

	// DELETE
	Map<String, Object> deleteBusiness(String uuid);
	
	// IMPLEMENTATION
	String getImplementation();
}
