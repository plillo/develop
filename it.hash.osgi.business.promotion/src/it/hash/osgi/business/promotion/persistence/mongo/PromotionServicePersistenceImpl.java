package it.hash.osgi.business.promotion.persistence.mongo;

import static it.hash.osgi.utils.StringUtils.isEmptyOrNull;

import java.text.ParseException;
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
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import com.mongodb.DBObject;

import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.promotion.Promotion;
import it.hash.osgi.business.promotion.PromotionFactory;
import it.hash.osgi.business.promotion.persistence.api.PromotionServicePersistence;
import it.hash.osgi.business.promotion.service.Status;
import it.hash.osgi.utils.StringUtils;

;

/**
 * Implements interface BusinessServicePersistence with MongoDB:
 * DBMS,open-source, document-oriented
 * 
 * @author Montinari Antonella
 */

@SuppressWarnings("unchecked")
@Component(service ={ PromotionServicePersistence.class, EventHandler.class}, immediate=true,
property={EventConstants.EVENT_TOPIC +"=it/hash/osgi/business/category/*"}
)
public class PromotionServicePersistenceImpl implements PromotionServicePersistence, EventHandler {
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

		Promotion promotion_obj = PromotionFactory.getInstance((String) promotion.get("type"));
		if (promotion_obj != null) {

				promotion_obj.setByMap(promotion);
				promotion_obj.setActive(true);
			return addPromotion(promotion_obj);
		}
		return null;
	}

	@Override
	public Map<String, Object> addPromotion(Promotion promotion)  {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Match promotion
		Map<String, Object> result = getPromotion(promotion);
		// If new promotion
		if (result.get("status").equals(Status.NOT_FOUND.getCode())) {
			// Create promotion
			
	/*		BasicDBObject dbupsert = new BasicDBObject()
					.append("$currentDate", new BasicDBObject("cdate",true).append("cdate", true))
					.append("$set", new BasicDBObject(promotion.toMap()));
		//	promotionCollection.update(new BasicDBObject()/*void object*///, dbupsert, true, false);
			
			
			promotionCollection.save(promotionToDBObject(promotion));

			// Get created business without logo
			DBObject created = promotionCollection.findOne(promotionToDBObject(promotion),
					new BasicDBObject("logo", false));

			// Build response
			if (created != null) {

				Promotion created_promotion = PromotionFactory.getInstance(created.toMap());
Map <String,Object> c=created.toMap();
				created_promotion.setByMap(c);

				response.put("status", Status.CREATED.getCode());
				response.put("message", Status.CREATED.getMessage());
				response.put("promotion", created_promotion);
			} else {
				response.put("status", Status.EXISTING_NOT_CREATED.getCode());
				response.put("message", Status.EXISTING_NOT_CREATED.getMessage());
			}
		} else {
			response.put("status", Status.EXISTING_MANY_NOT_CREATED.getCode());
			response.put("message", Status.EXISTING_MANY_NOT_CREATED.getMessage());
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

		private DBObject promotionToDBObject(Promotion promotion) {
		Gson gson = new Gson();
		BasicDBObject obj = (BasicDBObject)JSON.parse(gson.toJson(promotion));
	
		
	//	DBObject db = new BasicDBObject(promotion.toMap());

		return obj;
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
			response.put("status", Status.NOT_FOUND.getCode());
			response.put("message", Status.NOT_FOUND.getMessage());

			break;
		case 1:
			Promotion key = (Promotion) matchs.keySet().toArray()[0];
			response.put("promotion", key);
			response.put("keys", matchs.get(key));
			response.put("status", Status.FOUND.getCode());
			response.put("message", Status.FOUND.getMessage());
			break;
		default:
			response.put("promotion", matchs);
			response.put("status", Status.FOUND_MANY.getCode());
			response.put("message", Status.FOUND_MANY.getMessage());

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
	public Map<String, Object> updateActivate(String uuid, Boolean activated) {
		Map<String, Object> response = new HashMap<String, Object>();
		Promotion promotion = this.getPromotionByUuid(uuid);
		if (promotion != null) {
			promotion.setActive(activated);
			// TODO
			response = this.updatePromotion(promotion);
			if (response.get("status").equals(Status.UPDATE.getCode())) {
				response.put("status", Status.SETACTIVE.getCode());
				response.put("message", Status.SETACTIVE.getMessage());
			} else {

				response.put("status", Status.ERROR_SERVER_UPDATE.getCode());
				response.put("message", Status.ERROR_SERVER_UPDATE.getMessage());

			}

		} else {
			response.put("status", Status.PROMOTION_IS_NULL.getCode());
			response.put("message", Status.PROMOTION_IS_NULL.getMessage());
		}
		return response;
	}

	@Override
	public synchronized Map<String, Object> updatePromotion(Promotion promotion) {

		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
		
		if (promotion != null) {
			promotion.set_id(null);
			BasicDBObject updateDocument = new BasicDBObject()
					.append("$currentDate", new BasicDBObject("mdate",true).append("mdate", true))
					.append("$set", new BasicDBObject(promotion.toMap()));
			BasicDBObject searchQuery = new BasicDBObject().append("uuid", promotion.getUuid());

			promotionCollection.update(searchQuery, updateDocument);

			// Retrieve updated product
			DBObject updated = promotionCollection.findOne(new BasicDBObject("uuid", promotion.getUuid()));
			Promotion updatedPromotion = PromotionFactory.getInstance(updated.toMap());
			updatedPromotion.setByMap(updated.toMap());
			if (updatedPromotion != null) {
				responseUpdate.put("product", updatedPromotion);
				responseUpdate.put("status", Status.UPDATE.getCode());
				responseUpdate.put("message", Status.UPDATE.getMessage());
				return responseUpdate;
			}

			responseUpdate.put("status", Status.ERROR_SERVER_UPDATE.getCode());
			responseUpdate.put("message", Status.ERROR_SERVER_UPDATE.getMessage());
			return responseUpdate;

		}

		responseUpdate.put("status", Status.PROMOTION_IS_NULL.getCode());
		responseUpdate.put("message", Status.PROMOTION_IS_NULL.getMessage());
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
				responseDelete.put("status", Status.DELETE.getCode());
				responseDelete.put("message", Status.DELETE.getMessage());

			} else {
				responseDelete.put("status", Status.ERROR_SERVER_DELETE.getCode());
				responseDelete.put("message", Status.ERROR_SERVER_DELETE.getMessage());

			}
		} else {
			responseDelete.put("status", Status.ERROR_DELETE.getCode());
			responseDelete.put("message", Status.ERROR_DELETE.getMessage());

		}
		return responseDelete;
	}

	@Override
	public String getImplementation() {
		// TODO Auto-generated method stub
		return "MongodbPromotion";
	}

	
	private boolean updatePromotionForDeleteCategory(String uuidCategory){
		List<Promotion> listPromotion = null;
		List<DBObject> list = new ArrayList<DBObject>(); 
	    BasicDBObject regexQuery = null;
		regexQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>(); 
		obj.add(new BasicDBObject("categories", new BasicDBObject("$regex", uuidCategory).append("$options", "$i"))); 
		regexQuery.put("$or", obj);
		System.out.println(regexQuery.toString()); 
		DBCursor cursor = promotionCollection.find(regexQuery); 
		list = cursor.toArray(); 
		if (!list.isEmpty()){ listPromotion= new ArrayList<Promotion>();
		Promotion p;
		
		for(DBObject elem : list) {
			//p = Business.toBusiness(elem.toMap());
			//	listPromotion.add(p); } } 
		}
			
		}
		
		return false;
	}
	@Override
	public void handleEvent(Event event) {
		String topic = event.getTopic();
		switch(topic){
		case "it/hash/osgi/business/category/DELETE":
			updatePromotionForDeleteCategory((String) event.getProperty("uuid"));
			break;
		
		}
		
		
			
		System.out.println(event.getTopic());
	}

}
