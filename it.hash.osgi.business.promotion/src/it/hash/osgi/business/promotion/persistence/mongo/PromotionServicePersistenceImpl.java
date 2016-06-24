package it.hash.osgi.business.promotion.persistence.mongo;

import static it.hash.osgi.utils.StringUtils.isEmptyOrNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;
import org.bson.types.ObjectId;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
//import com.mongodb.DBCursor;
import com.mongodb.DBObject;
//import com.mongodb.WriteResult;
import com.mongodb.WriteResult;

import it.hash.osgi.business.promotion.Promotion;
import it.hash.osgi.business.promotion.PromotionFactory;
import it.hash.osgi.business.promotion.persistence.api.PromotionServicePersistence;
import it.hash.osgi.utils.StringUtils;

;

/**
 * Implements interface BusinessServicePersistence with MongoDB:
 * DBMS,open-source, document-oriented
 * 
 * @author Montinari Antonella
 */

@SuppressWarnings("unchecked")
@Component(service = PromotionServicePersistence.class)
public class PromotionServicePersistenceImpl implements PromotionServicePersistence {
	/** Name of the collection */
	private static final String COLLECTION = "promotions";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	// Mongo business collection
	private DBCollection promotionCollection;

	@Reference()
	public void setMongoDBService(MongoDBService service) {
		this.m_mongoDBService = service;
	}

	public void unsetMongoDBService(MongoDBService service) {
		this.m_mongoDBService = null;
	}

	@Activate
	public void start() {
		// Initialize business collection
		promotionCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@Override
	public Map<String, Object> addPromotion(Map<String, Object> promotion) {

		Promotion promotion_obj = PromotionFactory.getInstance(promotion);
		promotion_obj.setByMap(promotion);

		return addPromotion(promotion_obj);

	}

	@Override
	public Map<String, Object> addPromotion(Promotion promotion) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Match business
		Map<String, Object> result = getPromotion(promotion);
		// If new business
		if ((int) result.get("matched") == 0) {
			// Create business

			promotionCollection.save(promotionToDBObject(promotion));

			// Get created business without logo
			DBObject created = promotionCollection.findOne(promotionToDBObject(promotion),
					new BasicDBObject("logo", false));

			// Build response
			if (created != null) {

				Promotion created_promotion = PromotionFactory.getInstance(created.toMap());

				created_promotion.setByMap(created.toMap());
				response.put("business", created_promotion);
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

	private Promotion getPromotionByKey(String key, String value) {
		return getPromotionByKey(key, value, false);
	}

	//
	private Promotion getPromotionByKey(String key, String value, boolean withLogo) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		Map<String, Object> response = getPromotion(map);
		if (response.containsKey("promotion"))
			return (Promotion) response.get("promotion");

		return null;
	}

	@Override
	public Promotion getPromotionByUuid(String uuid) {
		if (uuid == null)
			return null;

		return getPromotionByKey("uuid", uuid);
	}

	/*
	 * @Override public Map<String, Object> getBusiness(Business business,
	 * boolean withLogo) { if(business==null) return null;
	 * 
	 * Map<String, Object> map = new TreeMap<String, Object>();
	 * 
	 * if (!isEmptyOrNull(business.get_id())) map.put("_id", business.get_id());
	 * if (!isEmptyOrNull(business.getUuid())) map.put("uuid",
	 * business.getUuid()); if (!isEmptyOrNull(business.getName()))
	 * map.put("name", business.getName()); if
	 * (!isEmptyOrNull(business.getPIva())) map.put("pIva", business.getPIva());
	 * if (!isEmptyOrNull(business.getFiscalCode())) map.put("fiscalCode",
	 * business.getFiscalCode());
	 * 
	 * return getBusiness(map, withLogo); }
	 * 
	 * @Override public Map<String, Object> getBusiness(Business business) {
	 * return getBusiness(business, false); }
	 * 
	 * @Override public List<Business> retrieveBusinesses(String criterion,
	 * String search) {
	 * 
	 * List<DBObject> list = new ArrayList<DBObject>(); List<Business> listB =
	 * null ; BasicDBObject regexQuery = null;
	 * 
	 * regexQuery = new BasicDBObject(); List<BasicDBObject> obj = new
	 * ArrayList<BasicDBObject>(); obj.add(new BasicDBObject("name", new
	 * BasicDBObject("$regex", search).append("$options", "$i"))); obj.add(new
	 * BasicDBObject("_description", new BasicDBObject("$regex",
	 * search).append("$options", "$i"))); regexQuery.put("$or", obj);
	 * 
	 * System.out.println(regexQuery.toString()); DBCursor cursor =
	 * promotionCollection.find(regexQuery); list = cursor.toArray(); if
	 * (!list.isEmpty()){ listB= new ArrayList<Business>(); Business b; for
	 * (DBObject elem : list) { b = Business.toBusiness(elem.toMap());
	 * listB.add(b); } } return listB; }
	 */
	/*
	 * @Override public Map<String, Object> getBusiness(Map<String, Object>
	 * business, boolean withLogo) {
	 * 
	 * Map<String, Object> response = new HashMap<String, Object>(); DBObject
	 * found = null; Business found_business = null; Map<Business,
	 * TreeSet<String>> matchs = new TreeMap<Business, TreeSet<String>>();
	 * 
	 * if (business.containsKey("uuid") && business.get("uuid") != null) {
	 * if(withLogo) found = promotionCollection.findOne(new
	 * BasicDBObject("uuid", business.get("uuid"))); else found =
	 * promotionCollection.findOne(new BasicDBObject("uuid",
	 * business.get("uuid")), new BasicDBObject("logo", false));
	 * 
	 * if (found != null) { found_business = Business.toBusiness(found.toMap());
	 * 
	 * TreeSet<String> list = matchs.get(found_business); if (list == null) list
	 * = new TreeSet<String>();
	 * 
	 * list.add("uuid"); matchs.put(found_business, list); } }
	 * 
	 * if (business.containsKey("_id") && business.get("_id") != null) {
	 * if(withLogo) found = promotionCollection.findOne(new BasicDBObject("_id",
	 * business.get("_id"))); else found = promotionCollection.findOne(new
	 * BasicDBObject("_id", business.get("_id")), new BasicDBObject("logo",
	 * false));
	 * 
	 * if (found != null) { found_business = Business.toBusiness(found.toMap());
	 * TreeSet<String> list = matchs.get(found_business); if (list == null) list
	 * = new TreeSet<String>();
	 * 
	 * list.add("_id"); matchs.put(found_business, list); } } if
	 * (business.containsKey("fiscalCode") && business.get("fiscalCode") !=
	 * null) { if(withLogo) found = promotionCollection.findOne(new
	 * BasicDBObject("fiscalCode", business.get("fiscalCode"))); else found =
	 * promotionCollection.findOne(new BasicDBObject("fiscalCode",
	 * business.get("fiscalCode")), new BasicDBObject("logo", false));
	 * 
	 * if (found != null) { found_business = Business.toBusiness(found.toMap());
	 * TreeSet<String> list = matchs.get(found_business); if (list == null) list
	 * = new TreeSet<String>();
	 * 
	 * list.add("fiscalCode"); matchs.put(found_business, list); } }
	 * 
	 * if (business.containsKey("name") && business.get("name") != null) {
	 * if(withLogo) found = promotionCollection.findOne(new
	 * BasicDBObject("name", business.get("name"))); else found =
	 * promotionCollection.findOne(new BasicDBObject("name",
	 * business.get("name")), new BasicDBObject("logo", false));
	 * 
	 * if (found != null) { found_business = Business.toBusiness(found.toMap());
	 * 
	 * TreeSet<String> list = matchs.get(found_business); if (list == null) list
	 * = new TreeSet<String>();
	 * 
	 * list.add("name"); matchs.put(found_business, list); } } if
	 * (business.containsKey("pIva") && business.get("pIva") != null) {
	 * if(withLogo) found = promotionCollection.findOne(new
	 * BasicDBObject("pIva", business.get("pIva"))); else found =
	 * promotionCollection.findOne(new BasicDBObject("pIva",
	 * business.get("pIva")), new BasicDBObject("logo", false));
	 * 
	 * if (found != null) { found_business = Business.toBusiness(found.toMap());
	 * TreeSet<String> list = matchs.get(found_business); if (list == null) list
	 * = new TreeSet<String>();
	 * 
	 * list.add("partitaIva"); matchs.put(found_business, list); } }
	 * 
	 * // Set response: number of matched businesses response.put("matched",
	 * matchs.size());
	 * 
	 * // Set response details switch (matchs.size()) { case 0:
	 * response.put("found", false); response.put("returnCode", 650); break;
	 * case 1: Business key = (Business) matchs.keySet().toArray()[0];
	 * response.put("business", key); response.put("keys", matchs.get(key));
	 * response.put("found", true); response.put("returnCode", 200); break;
	 * default: response.put("businesses", matchs); response.put("returnCode",
	 * 640); }
	 * 
	 * return response; }
	 */
	/*
	 * @Override public Map<String, Object> getBusiness(Map<String, Object>
	 * business) { return getBusiness(business, false); }
	 * 
	 * private Business getBusinessByKey(String key, String value, boolean
	 * withLogo) { Map<String, Object> map = new TreeMap<String, Object>();
	 * map.put(key, value); Map<String, Object> response = getBusiness(map,
	 * withLogo); if (response.containsKey("business")) return (Business)
	 * response.get("business");
	 * 
	 * return null; }
	 * 
	 * private Business getBusinessByKey(String key, String value) { return
	 * getBusinessByKey(key, value, false); }
	 * 
	 * @Override public Business getBusinessByFiscalCode(String fiscalCode,
	 * boolean withLogo) { return getBusinessByKey("fiscalCode", fiscalCode,
	 * withLogo); }
	 * 
	 * @Override public Business getBusinessByFiscalCode(String fiscalCode) {
	 * return getBusinessByFiscalCode(fiscalCode, false); }
	 * 
	 * @Override public Business getBusinessByPartitaIva(String partitaIva,
	 * boolean withLogo) { return getBusinessByKey("partitaIva", partitaIva,
	 * withLogo); }
	 * 
	 * @Override public Business getBusinessByPartitaIva(String partitaIva) {
	 * return getBusinessByPartitaIva(partitaIva, false); }
	 * 
	 * @Override public Business getBusinessByName(String name, boolean
	 * withLogo) { return getBusinessByKey("name", name, withLogo); }
	 * 
	 * @Override public Business getBusinessByName(String name) { return
	 * getBusinessByName(name, false); }
	 * 
	 * @Override public Business getBusinessById(String businessId, boolean
	 * withLogo) { return getBusinessByKey("businessId", businessId, withLogo);
	 * }
	 * 
	 * @Override public Business getBusinessById(String businessId) { return
	 * getBusinessById(businessId, false); }
	 * 
	 * @Override public Business getBusinessByUuid(String uuid, boolean
	 * withLogo) { if(uuid==null) return null;
	 * 
	 * return getBusinessByKey("uuid", uuid, withLogo); }
	 * 
	 * @Override public Business getBusinessByUuid(String uuid) { if(uuid==null)
	 * return null;
	 * 
	 * return getBusinessByKey("uuid", uuid); }
	 * 
	 * @Override public List<Business> getBusinesses() { DBCursor cursor =
	 * promotionCollection.find(); List<Business> list = new ArrayList<>();
	 * while (cursor.hasNext()) {
	 * list.add(Business.toBusiness(cursor.next().toMap())); }
	 * 
	 * return list; }
	 * 
	 * @Override public List<Business> getBusinessDetails(Business business) {
	 * 
	 * DBCursor cursor = promotionCollection.find(businessToDBObject(business));
	 * 
	 * List<Business> list = new ArrayList<>(); while (cursor.hasNext()) {
	 * list.add(Business.toBusiness(cursor.next().toMap())); }
	 * 
	 * return list; }
	 */
	/*
	 * @SuppressWarnings("unused")
	 * 
	 * @Override public synchronized Map<String, Object> updateBusiness(String
	 * uuid, Business business) { Map<String, Object> response = new
	 * TreeMap<String, Object>(); Map<String, Object> responseUpdate = new
	 * TreeMap<String, Object>(); response = isNotEmptyOrNull(uuid) ?
	 * getBusiness(getBusinessByUuid(uuid)):getBusiness(business); if (response
	 * == null) { responseUpdate.put("update", "ERROR");
	 * responseUpdate.put("returnCode", 610); } else if ((int)
	 * response.get("matched") == 1) { // UNSET _ID business.set_id(null);
	 * BasicDBObject updateDocument = new BasicDBObject().append("$set",
	 * businessToDBObject(business)); BasicDBObject searchQuery = new
	 * BasicDBObject().append("uuid", uuid);
	 * 
	 * WriteResult wr = promotionCollection.update(searchQuery, updateDocument);
	 * 
	 * // Retrieve updated business (without logo) DBObject updated =
	 * promotionCollection.findOne(new BasicDBObject("uuid", uuid), new
	 * BasicDBObject("logo", false)); Business updatedBusiness =
	 * Business.toBusiness(updated.toMap()); if (updatedBusiness != null) {
	 * responseUpdate.put("business", updatedBusiness);
	 * responseUpdate.put("update", "OK"); responseUpdate.put("returnCode",
	 * 200);
	 * 
	 * } else { responseUpdate.put("update", "ERROR");
	 * responseUpdate.put("returnCode", 610); } } else {
	 * responseUpdate.put("update", "ERROR"); responseUpdate.put("returnCode",
	 * 610); }
	 * 
	 * return responseUpdate; }
	 * 
	 * @Override public Map<String, Object> updateBusiness(String uuid,
	 * Map<String, Object> pars) { Map<String, Object> response = new
	 * HashMap<String, Object>(); Business business = getBusinessByUuid(uuid);
	 * 
	 * if (business != null) { if (pars.containsKey("userUuid")) {
	 * business.addFollower((String) pars.get("userUuid")); response =
	 * updateBusiness(uuid, business); } } else { response.put("update",
	 * "ERROR"); response.put("returnCode", 610); }
	 * 
	 * return response; }
	 * 
	 * @SuppressWarnings("unused")
	 * 
	 * @Override public Map<String, Object> updateBusinessLogo(String uuid,
	 * String type, byte[] encodeBase64) { Map<String, Object> response = new
	 * HashMap<String, Object>(); Map<String, Object> map = new HashMap<String,
	 * Object>(); map.put("logoType", type); map.put("logo", encodeBase64);
	 * BasicDBObject updateDocument = new BasicDBObject().append("$set", new
	 * BasicDBObject(map)); BasicDBObject searchQuery = new
	 * BasicDBObject().append("uuid", uuid);
	 * 
	 * // update WriteResult wr = promotionCollection.update(searchQuery,
	 * updateDocument);
	 * 
	 * // retrieve DBObject updated = promotionCollection.findOne(new
	 * BasicDBObject("uuid", uuid));
	 * 
	 * // response Business updateBusiness =
	 * Business.toBusiness(updated.toMap()); if (updateBusiness != null) {
	 * response.put("business", updateBusiness); response.put("update", "OK");
	 * response.put("returnCode", 200);
	 * 
	 * } else { response.put("update", "ERROR"); response.put("returnCode",
	 * 610); }
	 * 
	 * return response; }
	 * 
	 * private synchronized Map<String, Object> deleteBusiness(Business
	 * business) { Map<String, Object> response = new TreeMap<String, Object>();
	 * Map<String, Object> responseDelete = new TreeMap<String, Object>();
	 * response = getBusiness(business); if ((int) response.get("matched") == 1)
	 * { String uuid = ((Business) response.get("business")).getUuid();
	 * BasicDBObject Dbo = new BasicDBObject("uuid", uuid); WriteResult wr =
	 * promotionCollection.remove(Dbo); if (wr.getN() == 1) {
	 * responseDelete.put("business", business); responseDelete.put("delete",
	 * true); responseDelete.put("returnCode", 200); } else {
	 * responseDelete.put("delete", false); responseDelete.put("returnCode",
	 * 620); } } else { responseDelete.put("delete", false);
	 * responseDelete.put("returnCode", 680);
	 * 
	 * } return responseDelete; }
	 * 
	 * @Override public Map<String, Object> deleteBusiness(String uuid) {
	 * Business business = new Business(); business.setUuid(uuid);
	 * 
	 * return deleteBusiness(business); }
	 * 
	 * @Override public String getImplementation() { return "Mongo"; }
	 * 
	 * @Override public Map<String, Object> follow(String businessUuid, String
	 * userUuid) { Map<String, Object> response = new HashMap<String, Object>();
	 * 
	 * BasicDBObject updatedDocument = new BasicDBObject().append("$addToSet",
	 * new BasicDBObject().append("followers", userUuid)); BasicDBObject
	 * searchQuery = new BasicDBObject().append("uuid", businessUuid);
	 * 
	 * WriteResult wr = promotionCollection.update(searchQuery,
	 * updatedDocument);
	 * 
	 * if (wr.getN() == 0) response.put("update", false); else
	 * response.put("update", true);
	 * 
	 * return response; }
	 * 
	 * @Override public Map<String, Object> unFollow(String businessUuid, String
	 * actual_user_uuid) { Map<String, Object> response = new HashMap<String,
	 * Object>(); BasicDBObject searchQuery = new BasicDBObject().append("uuid",
	 * businessUuid); BasicDBObject update = new BasicDBObject("followers",
	 * actual_user_uuid);
	 * 
	 * WriteResult wr = promotionCollection.update(searchQuery, new
	 * BasicDBObject("$pull", update)); if (wr.getN() == 0)
	 * response.put("update", false); else response.put("update", true);
	 * 
	 * return response; }
	 * 
	 * @Override public List<Business> retrieveOwnedByUser(String uuid, boolean
	 * withLogo) { if (uuid != null) { DBCursor cursor; if(withLogo) cursor =
	 * promotionCollection.find(new BasicDBObject("owner", uuid)); else cursor =
	 * promotionCollection.find(new BasicDBObject("owner", uuid), new
	 * BasicDBObject("logo", false));
	 * 
	 * List<Business> list = new ArrayList<Business>();
	 * 
	 * while (cursor.hasNext()) {
	 * list.add(Business.toBusiness(cursor.next().toMap())); } return list; }
	 * return new ArrayList<Business>(); }
	 * 
	 * @Override public List<Business> retrieveOwnedByUser(String uuid) { return
	 * retrieveOwnedByUser(uuid, false); }
	 * 
	 * @Override public List<Business> retrieveFollowedByUser(String uuid,
	 * boolean withLogo) { if (uuid != null) { DBCursor cursor; if(withLogo)
	 * cursor = promotionCollection.find(new BasicDBObject("followers", uuid));
	 * else cursor = promotionCollection.find(new BasicDBObject("followers",
	 * uuid), new BasicDBObject("logo", false));
	 * 
	 * List<Business> list = new ArrayList<Business>(); while (cursor.hasNext())
	 * { list.add(Business.toBusiness(cursor.next().toMap())); } return list; }
	 * 
	 * return new ArrayList<Business>(); }
	 * 
	 * @Override public List<Business> retrieveFollowedByUser(String uuid) {
	 * return retrieveFollowedByUser(uuid, false); }
	 * 
	 * @Override public List<Business> retrieveNotFollowedByUser(String
	 * userUuid, String search, boolean withLogo) { Map<String, Object> response
	 * = new HashMap<String, Object>();
	 * 
	 * List<BasicDBObject> array = new ArrayList<BasicDBObject>(); array.add(new
	 * BasicDBObject("name", new BasicDBObject("$regex",
	 * search).append("$options", "$i"))); array.add(new
	 * BasicDBObject("_description", new BasicDBObject("$regex",
	 * search).append("$options", "$i"))); array.add(new
	 * BasicDBObject("_longDescription", new BasicDBObject("$regex",
	 * search).append("$options", "$i"))); BasicDBObject substring_query = new
	 * BasicDBObject().append("$or", array); BasicDBObject not_followed_query =
	 * new BasicDBObject("followers", new BasicDBObject("$nin", new
	 * String[]{userUuid}));
	 * 
	 * array = new ArrayList<BasicDBObject>(); array.add(substring_query);
	 * array.add(not_followed_query); BasicDBObject query = new
	 * BasicDBObject().append("$and", array);
	 * 
	 * DBCursor cursor; if(withLogo) cursor = promotionCollection.find(query);
	 * else cursor = promotionCollection.find(query, new BasicDBObject("logo",
	 * false));
	 * 
	 * List<Business> list = new ArrayList<Business>(); if (cursor!=null){ while
	 * (cursor.hasNext()) {
	 * list.add(Business.toBusiness(cursor.next().toMap())); }
	 * response.put("notFollowedBusinesses", list); }
	 * 
	 * return list; }
	 * 
	 * @Override public List<Business> retrieveNotFollowedByUser(String
	 * userUuid, String search) { return retrieveNotFollowedByUser(userUuid,
	 * search, false); }
	 */
	private DBObject promotionToDBObject(Promotion promotion) {

		DBObject db = new BasicDBObject(promotion.toMap());

		return db;
	}

	@Override
	public Map<String, Object> getPromotion(Promotion promotion) {
		if (promotion == null)
			return null;

		Map<String, Object> map = new TreeMap<String, Object>();

		if (!isEmptyOrNull(promotion.get_id()))
			map.put("_id", promotion.get_id());
		if (!isEmptyOrNull(promotion.getUuid()))
			map.put("uuid", promotion.getUuid());

		return getPromotion(map);

	}

	@Override
	public Map<String, Object> getPromotion(Map<String, Object> promotion) {
		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found = null;
		Promotion found_promotion = null;
		Map<Promotion, TreeSet<String>> matchs = new TreeMap<Promotion, TreeSet<String>>();

		if (promotion.containsKey("uuid") && promotion.get("uuid") != null) {
			/* if(withLogo) */
			found = promotionCollection.findOne(new BasicDBObject("uuid", promotion.get("uuid")));
			/*
			 * else found = promotionCollection.findOne(new
			 * BasicDBObject("uuid", promotion.get("uuid")), new
			 * BasicDBObject("logo", false));
			 */
			if (found != null) {
				String type = (String) found.get("type");

				found_promotion = PromotionFactory.getInstance(type);
				found_promotion.setByMap(found.toMap());

				TreeSet<String> list = matchs.get(found_promotion);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_promotion, list);
			}
		}

		if (promotion.containsKey("_id") && promotion.get("_id") != null) {
			// if(withLogo)
			found = promotionCollection.findOne(new BasicDBObject("_id", promotion.get("_id")));
			// else
			// found = businessCollection.findOne(new BasicDBObject("_id",
			// business.get("_id")), new BasicDBObject("logo", false));

			if (found != null) {
				String type = (String) found.get("type");
				found_promotion = PromotionFactory.getInstance(type);
				found_promotion.setByMap(found.toMap());
				TreeSet<String> list = matchs.get(found_promotion);
				if (list == null)
					list = new TreeSet<String>();

				list.add("_id");
				matchs.put(found_promotion, list);
			}
		}

		if (promotion.containsKey("name") && promotion.get("name") != null) {
			// if(withLogo)
			found = promotionCollection.findOne(new BasicDBObject("name", promotion.get("name")));
			// else
			// found = businessCollection.findOne(new BasicDBObject("_id",
			// business.get("_id")), new BasicDBObject("logo", false));

			if (found != null) {
				String type = (String) found.get("type");
				found_promotion = PromotionFactory.getInstance(type);
				found_promotion.setByMap(found.toMap());
				TreeSet<String> list = matchs.get(found_promotion);
				if (list == null)
					list = new TreeSet<String>();

				list.add("name");
				matchs.put(found_promotion, list);
			}
		}

		// Set response: number of matched promotion
		response.put("matched", matchs.size());

		// Set response details
		switch (matchs.size()) {
		case 0:
			response.put("found", false);
			response.put("returnCode", 650);
			break;
		case 1:
			Promotion key = (Promotion) matchs.keySet().toArray()[0];
			response.put("promotion", key);
			response.put("keys", matchs.get(key));
			response.put("found", true);
			response.put("returnCode", 200);
			break;
		default:
			response.put("promotion", matchs);
			response.put("returnCode", 640);
		}

		return response;

	}

	public List<Promotion> getPromotionByBusinessUuid(String businessUuid) {

		DBCursor found = null;

		found = promotionCollection.find(new BasicDBObject("businessUuid", businessUuid));
		return getList(found);
	}

	@Override
	public Map<String, Object> getPromotion(Promotion promotion, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Promotion> getPromotionByBusinessFiscalCode(String BusinessFiscalCode) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Promotion> getPromotionByBusinessPartitaIva(String partitaIva) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Promotion> getPromotionByBusinessName(String businessName) {
		List<Promotion> promotionList = new ArrayList<Promotion>();
		BasicDBObject businessQuery = null;

		DBCursor cursor = null;

		businessQuery = new BasicDBObject("businessName", businessName);

		// GET CURSOR
		cursor = promotionCollection.find(businessQuery);

		// Product list construction
		promotionList = getList(cursor);
		return promotionList;

	}

	private List<Promotion> getPromotionByBusinessName(String Name, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Promotion> getPromotionByBusinessId(String businessId) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Promotion> getPromotionByBusinessId(String businessId, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Promotion> getPromotionByBusinessUuid(String uuid, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Promotion> getPromotionByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> getPromotions() {
		DBCursor cursor = promotionCollection.find();

		return getList(cursor);
	}

	private List<Promotion> getList(DBCursor cursor) {
		List<Promotion> list = new ArrayList<>();
		while (cursor.hasNext()) {
			Map<String, Object> item = cursor.next().toMap();
			Promotion promotion = PromotionFactory.getInstance(item);
			promotion.setByMap(item);
			list.add(promotion);
		}

		return list;

	}

	@Override
	public List<Promotion> retrievePromotions(Map<String, Object> mapKeywords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Promotion> retrievePromotions(String keyword) {

		return retrievePromotions(null, keyword);

	}

	public List<Promotion> retrievePromotions(String criterion, String keyword) {
		List<Promotion> promotionList = null;

		if (StringUtils.isNotEON(criterion) && criterion.equals("businessName"))
			promotionList = this.getPromotionByBusinessName(keyword);
		else {
			BasicDBObject uuidQuery = null;
			BasicDBObject regexQuery = null;
			BasicDBObject query = null;
			DBCursor cursor = null;

			if (StringUtils.isNotEON(criterion)) {
				uuidQuery = new BasicDBObject("businessUuid", criterion);
			}

			if (StringUtils.isNotEON(keyword)) {
				regexQuery = new BasicDBObject();
				List<BasicDBObject> objs = new ArrayList<BasicDBObject>();
				objs.add(new BasicDBObject("uuid", new BasicDBObject("$regex", keyword).append("$options", "$i")));
				objs.add(new BasicDBObject("_id", new BasicDBObject("$regex", keyword).append("$options", "$i")));
				objs.add(new BasicDBObject("type", new BasicDBObject("$regex", keyword).append("$options", "$i")));

				regexQuery.put("$or", objs);
			}

			if (uuidQuery != null && regexQuery != null) {
				query = new BasicDBObject();

				List<BasicDBObject> objs = new ArrayList<BasicDBObject>();
				objs.add(uuidQuery);
				objs.add(regexQuery);

				query.put("$and", objs);
			} else if (uuidQuery != null)
				query = uuidQuery;
			else if (regexQuery != null)
				query = regexQuery;

			// GET CURSOR
			cursor = query == null ? promotionCollection.find() : promotionCollection.find(query);

			// Product list construction
			promotionList = getList(cursor);

		}

		return promotionList;

	}

	@Override
	public Map<String, Object> updatePromotion(Promotion promotion) {

		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
		
		if(promotion!=null){
			Map<String, Object> response = new TreeMap<String, Object>();
			response = getPromotion(promotion);
			if (response!= null) {
				if ((int) response.get("matched") == 1){
					// UNSET _ID
					promotion.set_id(null);
					BasicDBObject updateDocument = new BasicDBObject().append("$set",
							new BasicDBObject(promotion.toMap()));
					BasicDBObject searchQuery = new BasicDBObject().append("uuid", promotion.getUuid());

					promotionCollection.update(searchQuery, updateDocument);

					// Retrieve updated product
					DBObject updated = promotionCollection.findOne(new BasicDBObject("uuid", promotion.getUuid()));
					Promotion updatedPromotion = PromotionFactory.getInstance(updated.toMap());
					updatedPromotion.setByMap(updated.toMap());
					if (updatedPromotion != null) {
						responseUpdate.put("product", updatedPromotion);
						responseUpdate.put("update", "OK");
						responseUpdate.put("returnCode", 200);
						return responseUpdate;
					} 
				}
			}
		}
		
			responseUpdate.put("update", "ERROR");
			responseUpdate.put("returnCode", 610);
			return responseUpdate;
	
				
				
	

	}

	@Override
	public Map<String, Object> updatePromotion(String uuid, Map<String, Object> promotion) {
		String type = null;
		Promotion found_promotion = null;
		if (promotion.containsKey("type")) {
			type = (String) promotion.get("type");
			found_promotion = PromotionFactory.getInstance(type);
		}
		return this.updatePromotion(found_promotion);
	}

	public synchronized Map<String, Object> deletePromotion(String uuid) {
		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseDelete = new TreeMap<String, Object>();
		Map<String, Object> delete = new TreeMap<String, Object>();
		delete.put("uuid", uuid);
		response = this.getPromotion(delete);
		if ((int) response.get("matched") == 1) {
			String _id = ((Promotion) response.get("promotion")).get_id();

			WriteResult wr = promotionCollection.remove(new BasicDBObject("_id", new ObjectId(_id)));
			if (wr.getN() == 1) {
				responseDelete.put("promotion", (Promotion) response.get("promotion"));
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
	public String getImplementation() {
		// TODO Auto-generated method stub
		return "MongodbPromotion";
	}

}
