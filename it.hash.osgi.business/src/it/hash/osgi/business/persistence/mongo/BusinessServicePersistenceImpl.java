package it.hash.osgi.business.persistence.mongo;

import static it.hash.osgi.utils.StringUtils.isEmptyOrNull;
import static it.hash.osgi.utils.StringUtils.isNotEmptyOrNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.log.LogService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.utilsBusiness;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;

/**
 * Implements interface BusinessServicePersistence with MongoDB:
 * DBMS,open-source, document-oriented
 * 
 * @author Montinari Antonella
 */

public class BusinessServicePersistenceImpl implements BusinessServicePersistence {
	/** Name of the collection */
	private static final String COLLECTION = "businesses";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	// Mongo business collection
	private DBCollection businessCollection;

	public void start() {
		// Initialize business collection
		businessCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@Override
	public Map<String, Object> addBusiness(Map<String, Object> business) {

		Business business_obj = utilsBusiness.toBusiness(business);

		return addBusiness(business_obj);

	}

	private DBObject dbObjectBusiness(Business business) {
		Map<String, Object> map = utilsBusiness.createPars(business);
		
		DBObject db = new BasicDBObject(map);
		System.out.println(db.toString());

		return db;
	}

	@Override
	public Map<String, Object> addBusiness(Business business) {

		Map<String, Object> response = new TreeMap<String, Object>();

		// Match business
		Map<String, Object> result = getBusiness(business);
		// If new business
		if ((int) result.get("matched") == 0) {

			businessCollection.save(dbObjectBusiness(business));
			DBObject created = businessCollection.findOne(dbObjectBusiness(business));

			if (created != null) {
				Business created_business = utilsBusiness.toBusiness(created.toMap());
				response.put("business", created_business);
				response.put("created", true);
				response.put("returnCode", 200);
			} else {
				response.put("created", false);
				response.put("returnCode", 630);
			}
		} else {
			response.put("created", false);
			response.put("returnCode", 630);

		}
		return response;
	}

	@Override
	public Map<String, Object> getBusiness(Business business) {
		Map<String, Object> map = new TreeMap<String, Object>();

		if (!isEmptyOrNull(business.get_id()))
			map.put("_id", business.get_id());
		if (!isEmptyOrNull(business.getUuid()))
			map.put("uuid", business.getUuid());
		if (!isEmptyOrNull(business.getName()))
			map.put("name", business.getName());
		if (!isEmptyOrNull(business.getPIva()))
			map.put("pIva", business.getPIva());
		if (!isEmptyOrNull(business.getFiscalCode()))
			map.put("fiscalCode", business.getFiscalCode());

		return getBusiness(map);
	}
	
	@Override
	public List<Business> retrieveBusinesses(String criterion, String search) {

		List<DBObject> list = new ArrayList<DBObject>();
		List<Business> listB = null ;
		BasicDBObject regexQuery = null;

		regexQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("name", new BasicDBObject("$regex", search).append("$options", "$i")));
		obj.add(new BasicDBObject("_description", new BasicDBObject("$regex", search).append("$options", "$i")));
		regexQuery.put("$or", obj);

		System.out.println(regexQuery.toString());
		DBCursor cursor = businessCollection.find(regexQuery);
		list = cursor.toArray();
		if (!list.isEmpty()){
			listB= new ArrayList<Business>();
		Business b;
		for (DBObject elem : list) {
			b = utilsBusiness.toBusiness(elem.toMap());
			listB.add(b);
		}
		}
		return listB;
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {

		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found = null;
		Business found_business = null;
		Map<Business, TreeSet<String>> matchs = new TreeMap<Business, TreeSet<String>>();

		if (business.containsKey("uuid") && business.get("uuid") != null) {
			found = businessCollection.findOne(new BasicDBObject("uuid", business.get("uuid")));

			if (found != null) {
				found_business = utilsBusiness.toBusiness(found.toMap());

				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_business, list);
			}
		}

		if (business.containsKey("_id") && business.get("_id") != null) {
			found = businessCollection.findOne(new BasicDBObject("_id", business.get("_id")));

			if (found != null) {
				found_business = utilsBusiness.toBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("_id");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("fiscalCode") && business.get("fiscalCode") != null) {
			found = businessCollection.findOne(new BasicDBObject("fiscalCode", business.get("fiscalCode")));
			if (found != null) {
				found_business = utilsBusiness.toBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("fiscalCode");
				matchs.put(found_business, list);
			}
		}

		if (business.containsKey("name") && business.get("name") != null) {

			found = businessCollection.findOne(new BasicDBObject("name", business.get("name")));

			if (found != null) {
				found_business = utilsBusiness.toBusiness(found.toMap());

				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("name");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("pIva") && business.get("pIva") != null) {
			found = businessCollection.findOne(new BasicDBObject("pIva", business.get("pIva")));

			if (found != null) {
				found_business = utilsBusiness.toBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("partitaIva");
				matchs.put(found_business, list);
			}
		}

		// Set response: number of matched businesses
		response.put("matched", matchs.size());

		// Set response details
		switch (matchs.size()) {
		case 0:
			response.put("found", false);
			response.put("returnCode", 650);
			break;
		case 1:
			Business key = (Business) matchs.keySet().toArray()[0];
			response.put("business", key);
			response.put("keys", matchs.get(key));
			response.put("found", true);
			response.put("returnCode", 200);
			break;
		default:
			response.put("businesses", matchs);
			response.put("returnCode", 640);
		}

		return response;
	}

	private Business getBusinessByKey(String key, String value) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		Map<String, Object> response = getBusiness(map);
		if (response.containsKey("business"))
			return (Business) response.get("business");

		return null;
	}

	@Override
	public Business getBusinessByFiscalCode(String fiscalCode) {
		return getBusinessByKey("fiscalCode", fiscalCode);
	}

	@Override
	public Business getBusinessByPartitaIva(String partitaIva) {
		return getBusinessByKey("partitaIva", partitaIva);
	}

	@Override
	public Business getBusinessByName(String name) {

		return getBusinessByKey("name", name);
	}

	@Override
	public Business getBusinessById(String businessId) {
		return getBusinessByKey("businessId", businessId);
	}

	@Override
	public Business getBusinessByUuid(String uuid) {
		return getBusinessByKey("uuid", uuid);
	}

	@Override
	public List<Business> getBusinesses() {
		// dobbiamo leggerli come oggetti json perchè non sappiamo lo schema!!!
		DBCursor cursor = businessCollection.find();
		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(utilsBusiness.toBusiness(cursor.next().toMap()));
		}

		return list;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {

		DBCursor cursor = businessCollection.find(dbObjectBusiness(business));

		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(utilsBusiness.toBusiness(cursor.next().toMap()));
		}

		return list;
	}

	@Override
	public synchronized Map<String, Object> updateBusiness(String uuid, Business business) {
		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
		response = isNotEmptyOrNull(uuid) ? getBusiness(getBusinessByUuid(uuid)):getBusiness(business);
		if ((int) response.get("matched") == 1) {
			// UNSET _ID
			business.set_id(null);
			BasicDBObject updateDocument = new BasicDBObject().append("$set", businessToDBObject(business));
			BasicDBObject searchQuery = new BasicDBObject().append("uuid", uuid);
	       
			@SuppressWarnings("unused")
			WriteResult wr = businessCollection.update(searchQuery, updateDocument);
			
			DBObject updated = businessCollection.findOne(new BasicDBObject("uuid", uuid));
			
			Business updateBusiness = utilsBusiness.toBusiness(updated.toMap());
			if (updateBusiness != null) {
				responseUpdate.put("business", updateBusiness);
				responseUpdate.put("update", "OK");
				responseUpdate.put("returnCode", 200);

			} else {
				responseUpdate.put("update", "ERROR");
				responseUpdate.put("returnCode", 610);
			}
		} else {
			responseUpdate.put("update", "ERROR");
			responseUpdate.put("returnCode", 610);
		}

		return responseUpdate;
	}

	@Override
	public Map<String, Object> updateBusiness(String uuid, Map<String, Object> pars) {
		Map<String, Object> response = new HashMap<String, Object>();
		Business business = getBusinessByUuid(uuid);

		if (business != null) {
			if (pars.containsKey("userUuid")) {
				business.addFollower((String) pars.get("userUuid"));
				response = updateBusiness(uuid, business);
			}
		} else {
			response.put("update", "ERROR");
			response.put("returnCode", 610);
		}

		return response;
	}

	private synchronized Map<String, Object> deleteBusiness(Business business) {
		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseDelete = new TreeMap<String, Object>();
		response = getBusiness(business);
		if ((int) response.get("matched") == 1) {
			String uuid = ((Business) response.get("business")).getUuid();
			BasicDBObject Dbo = new BasicDBObject("uuid", uuid);
			WriteResult wr = businessCollection.remove(Dbo);
			if (wr.getN() == 1) {
				responseDelete.put("business", business);
				responseDelete.put("delete", true);
				responseDelete.put("returnCode", 200);
			} else {
				responseDelete.put("delete", false);
				responseDelete.put("returnCode", 620);
			}
		} else {
			responseDelete.put("delete", false);
			responseDelete.put("returnCode", 680);

		}
		return responseDelete;
	}

	@Override
	public Map<String, Object> deleteBusiness(String uuid) {
		Business business = new Business();
		business.setUuid(uuid);
		
		return deleteBusiness(business);
	}

	@Override
	public String getImplementation() {
		return "Mongo";
	}

	@Override
	public Map<String, Object> follow(String businessUuid, String userUuid) {
		Map<String, Object> response = new HashMap<String, Object>();

		BasicDBObject updatedDocument = new BasicDBObject().append("$addToSet",
				new BasicDBObject().append("followers", userUuid));
		BasicDBObject searchQuery = new BasicDBObject().append("uuid", businessUuid);

		WriteResult wr = businessCollection.update(searchQuery, updatedDocument);

		if (wr.getN() == 0)
			response.put("update", false);
		else
			response.put("update", true);

		return response;
	}

	@Override
	public Map<String, Object> unFollow(String businessUuid, String actual_user_uuid) {
		Map<String, Object> response = new HashMap<String, Object>();
		BasicDBObject searchQuery = new BasicDBObject().append("uuid", businessUuid);
		BasicDBObject update = new BasicDBObject("followers", actual_user_uuid);

		WriteResult wr = businessCollection.update(searchQuery, new BasicDBObject("$pull", update));
		if (wr.getN() == 0)
			response.put("update", false);
		else
			response.put("update", true);

		return response;
	}
	
	@Override
	public List<Business> retrieveOwnedByUser(String uuid) {
		if (uuid != null) {
			DBCursor cursor = businessCollection.find(new BasicDBObject("owner", uuid));

			List<Business> list = new ArrayList<Business>();

			while (cursor.hasNext()) {
				list.add(utilsBusiness.toBusiness(cursor.next().toMap()));
			}
			return list;
		}
		return new ArrayList<Business>();
	}
	
	@Override
	public List<Business> retrieveFollowedByUser(String uuid) {
		if (uuid != null) {
			DBCursor cursor = businessCollection.find(new BasicDBObject("followers", uuid));

			List<Business> list = new ArrayList<Business>();
			while (cursor.hasNext()) {
				list.add(utilsBusiness.toBusiness(cursor.next().toMap()));
			}
			return list;
		}

		return new ArrayList<Business>();
	}

	@Override
	public List<Business> retrieveNotFollowedByUser(String userUuid, String search) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<BasicDBObject> array = new ArrayList<BasicDBObject>();
		array.add(new BasicDBObject("name", new BasicDBObject("$regex", search).append("$options", "$i")));
		array.add(new BasicDBObject("_description", new BasicDBObject("$regex", search).append("$options", "$i")));
		array.add(new BasicDBObject("_longDescription", new BasicDBObject("$regex", search).append("$options", "$i")));
		BasicDBObject substring_query = new BasicDBObject().append("$or", array);
		BasicDBObject not_followed_query = new BasicDBObject("followers", new BasicDBObject("$nin", new String[]{userUuid}));
		
		array = new ArrayList<BasicDBObject>();
		array.add(substring_query);
		array.add(not_followed_query);
		BasicDBObject query = new BasicDBObject().append("$and", array);

		DBCursor dbc = businessCollection.find(query);

		List<Business> list = new ArrayList<Business>();
		if (dbc!=null){
			while (dbc.hasNext()) {
				list.add(utilsBusiness.toBusiness(dbc.next().toMap()));
			}
			response.put("notFollowedBusinesses", list);
		}
		
		return list;
	}
	
	private DBObject businessToDBObject(Business business) {
		Map<String, Object> map = businessToMap(business);
		DBObject db = new BasicDBObject(map);

		return db;
	}
	
	public static Map<String, Object> businessToMap(Business business) {
		Map<String, Object> pars = new HashMap<String, Object>();

		if (!isEmptyOrNull(business.get_id()))
			pars.put("_id", business.get_id());
		if (!isEmptyOrNull(business.getUuid()))
			pars.put("uuid", business.getUuid());
		if (!isEmptyOrNull(business.getName()))
			pars.put("name", business.getName());
		if (!isEmptyOrNull(business.getPIva()))
			pars.put("pIva", business.getPIva());
		if (!isEmptyOrNull(business.getFiscalCode()))
			pars.put("fiscalCode", business.getFiscalCode());
		if (!isEmptyOrNull(business.getAddress()))
			pars.put("address", business.getAddress());
		if (!isEmptyOrNull(business.getCity()))
			pars.put("city", business.getCity());
		if (!isEmptyOrNull(business.getCap()))
			pars.put("cap", business.getCap());
		if (!isEmptyOrNull(business.getNation()))
			pars.put("nation", business.getNation());
		if (!isEmptyOrNull(business.get__Description()))
			pars.put("_description", business.get__Description());
		if (!isEmptyOrNull(business.get__longDescription()))
			pars.put("_longDescription", business.get__longDescription());
		if (!isEmptyOrNull(business.getOwner()))
			pars.put("owner", business.getOwner());
		if (business.getCategories() != null)
			pars.put("categories", business.getCategories());
		if (business.getFollowers()!=null)
			pars.put("followers", business.getFollowers());
		if (business.getPosition()!=null){
			Map <String, Object> pos = new HashMap<String, Object>();
			pos.put("type","Point");
			pos.put("coordinates", business.getPosition().getCoordinates().toArray());
		   
			pars.put("position", pos);}
		if (!isEmptyOrNull(business.getEmail()))
			pars.put("email", business.getEmail());
		if (!isEmptyOrNull(business.getMobile()))
			pars.put("mobile", business.getMobile());
		if (!isEmptyOrNull(business.getPublished()))
			pars.put("published", business.getPublished());
		if (!isEmptyOrNull(business.getTrusted_email()))
			pars.put("trusted_email", business.getTrusted_email());
		if (!isEmptyOrNull(business.getTrusted_mobile()))
			pars.put("trusted_mobile", business.getTrusted_mobile());
		if (!isEmptyOrNull(business.getCauthor()))
			pars.put("cauthor", business.getCauthor());
		if (!isEmptyOrNull(business.getCdate()))
			pars.put("cdate", business.getCdate());
		if (!isEmptyOrNull(business.getMauthor()))
			pars.put("mauthor", business.getMauthor());
		if (!isEmptyOrNull(business.getMdate()))
			pars.put("mdate", business.getMdate());
		if (!isEmptyOrNull(business.getLdate()))
			pars.put("ldate", business.getLdate());
		if (business.getOthers() != null)
			pars.put("others", business.getOthers());

		return pars;
	}

}
