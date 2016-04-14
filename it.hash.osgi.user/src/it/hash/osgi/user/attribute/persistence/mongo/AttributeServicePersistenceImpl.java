package it.hash.osgi.user.attribute.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;
import it.hash.osgi.utils.StringUtils;

public class AttributeServicePersistenceImpl implements AttributeServicePersistence {
	private volatile MongoDBService m_mongoDBService;
	private static final String COLLECTION = "attributes";

	private DBCollection attributesCollection;

	public void start() {
		attributesCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Attribute getAttribute(String uuid) {
		DBObject found = attributesCollection.findOne(new BasicDBObject("uuid", uuid));

		if (found != null) {
			return mapToAttribute(found.toMap());
		}
		
		return null;
	}
	
	@Override
	public List<Attribute> getAttributes() {
		DBCursor cursor = attributesCollection.find();
		List<Attribute> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(mapToAttribute(cursor.next().toMap()));
		}
		return list;

	}

	@Override
	public List<Attribute> getAttributesByCategories(List<String> categories) {
		DBObject regexQuery = new BasicDBObject();

		List<Attribute> listAtt = new ArrayList<Attribute>();

		regexQuery = new BasicDBObject();

		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		for (String s : categories) {
			obj.add(new BasicDBObject("context", new BasicDBObject("$regex", s).append("$options", "$i")));
		}
		regexQuery.put("$or", obj);

		System.out.println(regexQuery.toString());
		DBCursor cursor = attributesCollection.find(regexQuery);
		List<DBObject> list = cursor.toArray();
		Attribute b;
		for (DBObject elem : list) {
			b = mapToAttribute(elem.toMap());
			listAtt.add(b);
		}
		return listAtt;
	}

	@Override
	public List<Attribute> getCoreAttributes() {
		List<Attribute> list = new ArrayList<Attribute>();
		DBCursor cursor = attributesCollection.find(new BasicDBObject("applications", new BasicDBObject("$exists", false)));
		for (DBObject elem : cursor.toArray()) {
			list.add(mapToAttribute(elem.toMap()));
		}
		return list;
	}

	@Override
	public List<Attribute> getApplicationAttributes(String appid) {
		List<Attribute> list = new ArrayList<Attribute>();
		DBCursor cursor = attributesCollection.find(new BasicDBObject("applications", new BasicDBObject("$elemMatch", new BasicDBObject("appcode", appid))));
		for (DBObject elem : cursor.toArray()) {
			list.add(mapToAttribute(elem.toMap()));
		}
		return list;
	}

	@Override
	public Map<String, Object> createAttribute(Attribute attribute) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Match attribute
		Map<String, Object> result = getAttribute(attribute);

		// If new attribute
		if ((int) result.get("matched") == 0) {
			attributesCollection.save(attributeToDBObject(attribute));
			DBObject created = attributesCollection.findOne(attributeToDBObject(attribute));

			if (created != null) {
				Attribute created_attribute = mapToAttribute(created.toMap());
				response.put("attribute", created_attribute);
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

		/*
		 * 
		 * WriteResult<Attribute, Object> writeResult =
		 * attributeMap.save(attribute); String savedId =
		 * (String)writeResult.getSavedId(); if
		 * (!StringUtils.isEmptyOrNull(savedId)) { Attribute created_attribute =
		 * attributeMap.findOneById(savedId); if (created_attribute != null) {
		 * response.put("attribute",created_attribute); response.put("created",
		 * true); response.put("returnCode", 200); } else {
		 * response.put("created", false); response.put("returnCode", 630); } }
		 */

		return response;
	}
	
	@Override
	public Map<String, Object> updateAttribute(String uuid, Attribute attribute) {
		Map<String, Object> response = new TreeMap<String, Object>();
		if(StringUtils.isEmptyOrNull(uuid) || attribute==null) {
			response.put("updated", false);
			response.put("returnCode", 401);
			
			return response;
		}
		
		attribute.set_id(null);
		BasicDBObject updateDocument = new BasicDBObject().append("$set", attributeToDBObject(attribute));
		BasicDBObject searchQuery = new BasicDBObject().append("uuid", uuid);
       
		@SuppressWarnings("unused")
		WriteResult wr = attributesCollection.update(searchQuery, updateDocument);
		//TODO verificare l'esito
		
		DBObject updated = attributesCollection.findOne(new BasicDBObject("uuid", uuid));
		
		response.put("attribute", updated.toMap());
		response.put("updated", true);
		response.put("returnCode", 201);
		
		return response;
	}

	@Override
	public Map<String, Object> deleteAttribute(String uuid) {
		Map<String, Object> response = new TreeMap<String, Object>();
		if(StringUtils.isEmptyOrNull(uuid)) {
			response.put("deleted", false);
			response.put("returnCode", 401);
			
			return response;
		}
		
		Attribute attribute = getAttribute(uuid);

		if (attribute!=null) {
			BasicDBObject searchQuery = new BasicDBObject().append("uuid", uuid);
			@SuppressWarnings("unused")
			WriteResult wr = attributesCollection.remove(searchQuery);
			//TODO verificare l'esito
			
			response.put("attribute", attribute);
			response.put("deleted", true);
			response.put("returnCode", 201);
		} else {
			response.put("deleted", false);
			response.put("returnCode", 401);
		}

		return response;
	}

	private DBObject attributeToDBObject(Attribute attribute) {
		Map<String, Object> map = attributeToMap(attribute);
		DBObject db = new BasicDBObject(map);

		return db;
	}

	private Map<String, Object> attributeToMap(Attribute attribute) {
		Map<String, Object> pars = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(attribute.get_id()))
			pars.put("_id", attribute.get_id());

		if (!StringUtils.isEmptyOrNull(attribute.getUuid()))
			pars.put("uuid", attribute.getUuid());
		if (!StringUtils.isEmptyOrNull(attribute.getName()))
			pars.put("name", attribute.getName());
		if (!StringUtils.isEmptyOrNull(attribute.getLabel()))
			pars.put("label", attribute.getLabel());
		if (!StringUtils.isEmptyOrNull(attribute.getType()))
			pars.put("type", attribute.getType());
		if (!StringUtils.isEmptyOrNull(attribute.getUItype()))
			pars.put("UItype", attribute.getUItype());
		if (attribute.getValues() != null)
			pars.put("values", attribute.getValues());
		if (attribute.getApplications() != null)
			pars.put("applications", attribute.getApplications());
		if (!StringUtils.isEmptyOrNull(attribute.getValidator()))
			pars.put("validator", attribute.getValidator());

		pars.put("mandatory", attribute.isMandatory());
		pars.put("multiValued", attribute.isMandatory());

		if (!StringUtils.isEmptyOrNull(attribute.getCauthor()))
			pars.put("cauthor", attribute.getCauthor());
		if (!StringUtils.isEmptyOrNull(attribute.getCdate()))
			pars.put("cdate", attribute.getCdate());
		if (!StringUtils.isEmptyOrNull(attribute.getMauthor()))
			pars.put("mauthor", attribute.getMauthor());
		if (!StringUtils.isEmptyOrNull(attribute.getMdate()))
			pars.put("mdate", attribute.getMdate());
		if (!StringUtils.isEmptyOrNull(attribute.getLauthor()))
			pars.put("lauthor", attribute.getLauthor());
		if (!StringUtils.isEmptyOrNull(attribute.getLdate()))
			pars.put("ldate", attribute.getLdate());
		if (attribute.getOthers() != null)
			pars.put("others", attribute.getOthers());

		return pars;
	}

	private Map<String, Object> getAttribute(Attribute attribute) {
		Map<String, Object> map = new TreeMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(attribute.get_id()))
			map.put("_id", attribute.get_id());
		if (!StringUtils.isEmptyOrNull(attribute.getUuid()))
			map.put("uuid", attribute.getUuid());
		if (!StringUtils.isEmptyOrNull(attribute.getName()))
			map.put("name", attribute.getName());

		return getAttribute(map);
	}

	private Map<String, Object> getAttribute(Map<String, Object> map) {
		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found = null;
		Attribute found_attribute = null;
		Map<Attribute, TreeSet<String>> matchs = new TreeMap<Attribute, TreeSet<String>>();

		if (map.containsKey("uuid") && map.get("uuid") != null) {
			found = attributesCollection.findOne(new BasicDBObject("uuid", map.get("uuid")));

			if (found != null) {
				found_attribute = mapToAttribute(found.toMap());

				TreeSet<String> list = matchs.get(found_attribute);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_attribute, list);
			}
		}
		if (map.containsKey("_id") && map.get("_id") != null) {
			found = attributesCollection.findOne(new BasicDBObject("_id", map.get("_id")));

			if (found != null) {
				found_attribute = mapToAttribute(found.toMap());
				TreeSet<String> list = matchs.get(found_attribute);
				if (list == null)
					list = new TreeSet<String>();

				list.add("_id");
				matchs.put(found_attribute, list);
			}
		}

		if (map.containsKey("name") && map.get("name") != null) {

			found = attributesCollection.findOne(new BasicDBObject("name", map.get("name")));

			if (found != null) {
				found_attribute =  AttributeServicePersistenceImpl.mapToAttribute(found.toMap());

				TreeSet<String> list = matchs.get(found_attribute);
				if (list == null)
					list = new TreeSet<String>();
   
				list.add("name");
				matchs.put(found_attribute, list);
			}
		} // Set response: number of matched attributes
		response.put("matched", matchs.size());

		// Set response details
		switch (matchs.size()) {
		case 0:
			response.put("found", false);
			response.put("returnCode", 650);
			break;
		case 1:
			Attribute key = (Attribute) matchs.keySet().toArray()[0];
			response.put("attribute", key);
			response.put("keys", matchs.get(key));
			response.put("found", true);
			response.put("returnCode", 200);
			break;
		default:
			response.put("attributes", matchs);
			response.put("returnCode", 640);
		}

		return response;
	}
	
	public static Attribute mapToAttribute(Map<String, Object> map){
		Attribute attribute = new Attribute();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			switch (entry.getKey()) {
			case "_id":
				attribute.set_id(entry.getValue().toString());
				break;
			case "uuid":
				attribute.setUuid((String) entry.getValue());
				break;
			case "name":
				attribute.setName((String) entry.getValue());
				break;
			case "label":
				attribute.setLabel((String) entry.getValue());
				break;
			case "type":
				attribute.setType((String) entry.getValue());
				break;
			case "UItype":
				attribute.setUItype((String) entry.getValue());
				break;
			case "values":
				if(entry.getValue() instanceof BasicDBList){
					BasicDBList bd = (BasicDBList)entry.getValue();
					Map mapValues = bd.toMap();
					for(Iterator<String> itr = mapValues.keySet().iterator(); itr.hasNext();) {
						 try {
							 BasicDBObject bdbo = (BasicDBObject)mapValues.get(itr.next());
							 attribute.addValues(bdbo.toMap()); 
						 }
						 catch(Exception e){
						 }
					}
				}
				break;
			case "mandatory":
				attribute.setMandatory((boolean) entry.getValue());
				break;
			case "multiValued":
				attribute.setMultiValued((boolean) entry.getValue());
				break;
			case "validator":
				attribute.setValidator((String) entry.getValue());
				break;
			case "applications":
				if(entry.getValue() instanceof BasicDBList){
					BasicDBList bd = (BasicDBList)entry.getValue();
					Map mapApplications = bd.toMap();
					for(Iterator<String> itr = mapApplications.keySet().iterator(); itr.hasNext();) {
						try {
							BasicDBObject bdbo = (BasicDBObject)mapApplications.get(itr.next());
							attribute.addApplications(bdbo.toMap());
						}
						catch(Exception e){
						}
					}
				}
				break;
			case "cauthor":
				attribute.setCauthor((String) entry.getValue());
				break;
			case "cdate":
				attribute.setCdate((String) entry.getValue());
				break;
			case "mauthor":
				attribute.setMauthor((String) entry.getValue());
				break;
			case "mdate":
				attribute.setMdate((String) entry.getValue());
				break;
			case "lauthor":
				attribute.setLauthor((String) entry.getValue());
				break;
			case "ldate":
				attribute.setLdate((String) entry.getValue());
				break;
			}
		}

		return attribute;
	}
}
