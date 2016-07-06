package it.hash.osgi.business.persistence.mongo;

import static it.hash.osgi.utils.StringUtils.isEmptyOrNull;
import static it.hash.osgi.utils.StringUtils.isNotEmptyOrNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;
import org.bson.types.ObjectId;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessPersistenceService;

/**
 * Implements interface BusinessServicePersistence with MongoDB:
 * DBMS,open-source, document-oriented
 * 
 * @author Montinari Antonella
 */

@SuppressWarnings("unchecked")
@Component(immediate=true)
public class BusinessPersistenceServiceImpl implements BusinessPersistenceService {
	private static final String COLLECTION = "businesses";
	private DBCollection businessCollection;
	
	// References
	private MongoDBService m_mongoDBService;
	
	@Reference(service=MongoDBService.class)
	public void setMongoDBService(MongoDBService service){
		m_mongoDBService = service;
		doLog("MongoDBService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetMongoDBService(MongoDBService service){
		doLog("MongoDBService: "+(service==null?"NULL":"released"));
		m_mongoDBService = null;
	}

	@Activate
	void activate() {
		businessCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@Override
	public Map<String, Object> addBusiness(Map<String, Object> business) {
		Business business_obj = Business.toBusiness(business);

		return addBusiness(business_obj);
	}

	@Override
	public Map<String, Object> addBusiness(Business business) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Match business
		Map<String, Object> result = getBusiness(business);
		// If new business
		if ((int) result.get("matched") == 0) {
			// Create business
			businessCollection.save(businessToDBObject(business));
			
			// Get created business without logo
			DBObject created = businessCollection.findOne(businessToDBObject(business), new BasicDBObject("logo", false));

			// Build response
			if (created != null) {
				Business created_business = Business.toBusiness(created.toMap());
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
	public Map<String, Object> getBusiness(Business business, boolean withLogo) {
		if(business==null)
			return null;

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

		return getBusiness(map, withLogo);
	}
	
	@Override
	public Map<String, Object> getBusiness(Business business) {
		return getBusiness(business, false);
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
			b = Business.toBusiness(elem.toMap());
			listB.add(b);
		}
		}
		return listB;
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business, boolean withLogo) {

		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found = null;
		Business found_business = null;
		Map<Business, TreeSet<String>> matchs = new TreeMap<Business, TreeSet<String>>();

		if (business.containsKey("uuid") && business.get("uuid") != null) {
			if(withLogo)
				found = businessCollection.findOne(new BasicDBObject("uuid", business.get("uuid")));
			else
				found = businessCollection.findOne(new BasicDBObject("uuid", business.get("uuid")), new BasicDBObject("logo", false));

			if (found != null) {
				found_business = Business.toBusiness(found.toMap());

				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_business, list);
			}
		}

		if (business.containsKey("_id") && business.get("_id") != null) {
			if(withLogo)
				found = businessCollection.findOne(new BasicDBObject("_id", new ObjectId((String)business.get("_id"))));
			else
				found = businessCollection.findOne(new BasicDBObject("_id", new ObjectId((String)business.get("_id"))), new BasicDBObject("logo", false));

			if (found != null) {
				found_business = Business.toBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("_id");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("fiscalCode") && business.get("fiscalCode") != null) {
			if(withLogo)
				found = businessCollection.findOne(new BasicDBObject("fiscalCode", business.get("fiscalCode")));
			else
				found = businessCollection.findOne(new BasicDBObject("fiscalCode", business.get("fiscalCode")), new BasicDBObject("logo", false));
			
			if (found != null) {
				found_business = Business.toBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("fiscalCode");
				matchs.put(found_business, list);
			}
		}

		if (business.containsKey("name") && business.get("name") != null) {
			if(withLogo)
				found = businessCollection.findOne(new BasicDBObject("name", business.get("name")));
			else
				found = businessCollection.findOne(new BasicDBObject("name", business.get("name")), new BasicDBObject("logo", false));

			if (found != null) {
				found_business = Business.toBusiness(found.toMap());

				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("name");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("pIva") && business.get("pIva") != null) {
			if(withLogo)
				found = businessCollection.findOne(new BasicDBObject("pIva", business.get("pIva")));
			else
				found = businessCollection.findOne(new BasicDBObject("pIva", business.get("pIva")), new BasicDBObject("logo", false));

			if (found != null) {
				found_business = Business.toBusiness(found.toMap());
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
	
	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {
		return getBusiness(business, false);
	}

	private Business getBusinessByKey(String key, String value, boolean withLogo) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		Map<String, Object> response = getBusiness(map, withLogo);
		if (response.containsKey("business"))
			return (Business) response.get("business");

		return null;
	}
	
	private Business getBusinessByKey(String key, String value) {
		return getBusinessByKey(key, value, false);
	}

	@Override
	public Business getBusinessByFiscalCode(String fiscalCode, boolean withLogo) {
		return getBusinessByKey("fiscalCode", fiscalCode, withLogo);
	}
	
	@Override
	public Business getBusinessByFiscalCode(String fiscalCode) {
		return getBusinessByFiscalCode(fiscalCode, false);
	}

	@Override
	public Business getBusinessByPartitaIva(String partitaIva, boolean withLogo) {
		return getBusinessByKey("partitaIva", partitaIva, withLogo);
	}
	
	@Override
	public Business getBusinessByPartitaIva(String partitaIva) {
		return getBusinessByPartitaIva(partitaIva, false);
	}

	@Override
	public Business getBusinessByName(String name, boolean withLogo) {
		return getBusinessByKey("name", name, withLogo);
	}
	
	@Override
	public Business getBusinessByName(String name) {
		return getBusinessByName(name, false);
	}

	@Override
	public Business getBusinessById(String businessId, boolean withLogo) {
		return getBusinessByKey("businessId", businessId, withLogo);
	}
	
	@Override
	public Business getBusinessById(String businessId) {
		return getBusinessById(businessId, false);
	}
	
	@Override
	public Business getBusinessByUuid(String uuid, boolean withLogo) {
		if(uuid==null)
			return null;
		
		return getBusinessByKey("uuid", uuid, withLogo);
	}

	@Override
	public Business getBusinessByUuid(String uuid) {
		if(uuid==null)
			return null;
		
		return getBusinessByKey("uuid", uuid);
	}

	@Override
	public List<Business> getBusinesses() {
		DBCursor cursor = businessCollection.find();
		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(Business.toBusiness(cursor.next().toMap()));
		}

		return list;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {

		DBCursor cursor = businessCollection.find(businessToDBObject(business));

		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(Business.toBusiness(cursor.next().toMap()));
		}

		return list;
	}
	
	@Override
	public Map<String, Object> retrieveSubscriptionRules(String businessUuid, String userUuid) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("matched", false);
		
		BasicDBObject query = new BasicDBObject("uuid", businessUuid).append("followers.user", userUuid);
		DBObject business = businessCollection.findOne(query, new BasicDBObject("logo", false));
		if (business!=null) {
			response.put("matched", true);
			response.put("name", (String)business.get("name"));
			BasicDBList followers = (BasicDBList)business.get("followers");
			for( Iterator<Object> it = followers.iterator(); it.hasNext(); ){
				BasicDBObject dbo = (BasicDBObject) it.next();
				String user = (String)dbo.get("user");
				if(userUuid.equals(user))
					response.put("rules", ((BasicDBObject)dbo.get("rules")).toMap());
			}
		}

		return response;
	}

	@Override
	public Map<String, Object> setSubscriptionRule(String businessUuid, String userUuid, String rule, Boolean set) {
		Map<String, Object> response = new HashMap<String, Object>();

		// condizione in AND: uuid='' AND ...
		BasicDBObject searchQuery = new BasicDBObject().append("uuid", businessUuid).append("followers.user", userUuid);

		BasicDBObject updatedDocument = new BasicDBObject().append("$set",
				new BasicDBObject().append("followers.$.rules."+rule, set));

		WriteResult wr = businessCollection.update(searchQuery, updatedDocument);

		if (wr.getN() == 0)
			response.put("setted", false);
		else{
			response.put("setted", true);
			response.put("rule", rule);
			response.put("status", set);
		}


		return response;
	}

	@SuppressWarnings("unused")
	@Override
	public synchronized Map<String, Object> updateBusiness(String uuid, Business business) {
		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
		response = isNotEmptyOrNull(uuid) ? getBusiness(getBusinessByUuid(uuid)):getBusiness(business);
		if (response == null) {
			responseUpdate.put("update", "ERROR");
			responseUpdate.put("returnCode", 610);
		} else if ((int) response.get("matched") == 1) {
			// UNSET _ID
			business.set_id(null);
			BasicDBObject updateDocument = new BasicDBObject().append("$set", businessToDBObject(business));
			BasicDBObject searchQuery = new BasicDBObject().append("uuid", uuid);

			WriteResult wr = businessCollection.update(searchQuery, updateDocument);

			// Retrieve updated business (without logo)
			DBObject updated = businessCollection.findOne(new BasicDBObject("uuid", uuid), new BasicDBObject("logo", false));
			Business updatedBusiness = Business.toBusiness(updated.toMap());
			if (updatedBusiness != null) {
				responseUpdate.put("business", updatedBusiness);
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
	
	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> updateBusinessLogo(String uuid, String type, byte[] encodeBase64) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("logoType", type);
		map.put("logo", encodeBase64);
		BasicDBObject updateDocument = new BasicDBObject().append("$set", new BasicDBObject(map));
		BasicDBObject searchQuery = new BasicDBObject().append("uuid", uuid);
		
		// update
		WriteResult wr = businessCollection.update(searchQuery, updateDocument);
		
		// retrieve
		DBObject updated = businessCollection.findOne(new BasicDBObject("uuid", uuid));
		
		// response
		Business updateBusiness = Business.toBusiness(updated.toMap());
		if (updateBusiness != null) {
			response.put("business", updateBusiness);
			response.put("update", "OK");
			response.put("returnCode", 200);

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
			String _id = ((Business) response.get("business")).get_id();
			
			WriteResult wr = businessCollection.remove(new BasicDBObject("_id", new ObjectId(_id)));
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
	public Map<String, Object> deleteBusinessById(String id) {
		Business business = new Business();
		business.set_id(id);
		
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
				new BasicDBObject().append("followers", new BasicDBObject().append("user", userUuid).append("rules", new BasicDBObject().append("active", true))));
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
		BasicDBObject update = new BasicDBObject("followers", new BasicDBObject("user", actual_user_uuid));

		WriteResult wr = businessCollection.update(searchQuery, new BasicDBObject("$pull", update));
		if (wr.getN() == 0)
			response.put("update", false);
		else
			response.put("update", true);

		return response;
	}
	
	@Override
	public List<Business> retrieveOwnedByUser(String uuid, boolean withLogo) {
		if (uuid != null) {
			DBCursor cursor;
			if(withLogo)
				cursor = businessCollection.find(new BasicDBObject("owner", uuid));
			else
				cursor = businessCollection.find(new BasicDBObject("owner", uuid), new BasicDBObject("logo", false));
			
			List<Business> list = new ArrayList<Business>();

			while (cursor.hasNext()) {
				list.add(Business.toBusiness(cursor.next().toMap()));
			}
			return list;
		}
		return new ArrayList<Business>();
	}
	
	@Override
	public List<Business> retrieveOwnedByUser(String uuid) {
		return retrieveOwnedByUser(uuid, false);
	}
	
	@Override
	public List<Business> retrieveFollowedByUser(String uuid, boolean withLogo) {
		if (uuid != null) {
			DBCursor cursor;
			if(withLogo)
				cursor = businessCollection.find(new BasicDBObject("followers.user", uuid));
			else
				cursor = businessCollection.find(new BasicDBObject("followers.user", uuid), new BasicDBObject("logo", false));

			List<Business> list = new ArrayList<Business>();
			while (cursor.hasNext()) {
				try {
					list.add(Business.toBusiness(cursor.next().toMap()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return list;
		}

		return new ArrayList<Business>();
	}

	@Override
	public List<Business> retrieveFollowedByUser(String uuid) {
		return retrieveFollowedByUser(uuid, false);
	}
	
	@Override
	public List<Business> retrieveNotFollowedByUser(String userUuid, String search, boolean withLogo) {
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
		
		DBCursor cursor;
		if(withLogo)
			cursor = businessCollection.find(query);
		else
			cursor = businessCollection.find(query, new BasicDBObject("logo", false));

		List<Business> list = new ArrayList<Business>();
		if (cursor!=null){
			while (cursor.hasNext()) {
				list.add(Business.toBusiness(cursor.next().toMap()));
			}
			response.put("notFollowedBusinesses", list);
		}
		
		return list;
	}
	
	@Override
	public List<Business> retrieveNotFollowedByUser(String userUuid, String search) {
		return retrieveNotFollowedByUser(userUuid, search, false);
	}
	
	private DBObject businessToDBObject(Business business) {
		DBObject db = new BasicDBObject(Business.toMap(business));

		return db;
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
