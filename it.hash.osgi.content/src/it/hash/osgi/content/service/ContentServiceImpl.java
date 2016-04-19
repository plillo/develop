package it.hash.osgi.content.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import static it.hash.osgi.utils.StringUtils.*;

public class ContentServiceImpl implements ContentService {
	private static final String COLLECTION = "contents";
	private volatile MongoDBService m_mongoDBService;

	// Mongo User collection
	private DBCollection contentCollection;
	
	public void start() {
		// Initialize user collection
		contentCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}
	
	@Override
	public Map<String, Object> getContent(String name, String lang, String type) {
		Map<String, Object> searchContent =  new HashMap<String, Object>();
		
		name = name.replaceAll("/", ".");
		
		if(isNotEON(name))
			searchContent.put("name", name);
		if(isNotEON(lang))
			searchContent.put("lang", lang);
		if(isNotEON(type))
			searchContent.put("type", type);
		
		return getContent(searchContent);
	}

	@Override
	public Map<String, Object> getContent(String name, String lang) {

		return getContent(name, lang, null);
	}

	@Override
	public Map<String, Object> getContent(String name) {

		return getContent(name, "IT/IT", null);
	}

	public Map<String, Object> getContent(Map<String, Object> searchContent) {
		JacksonDBCollection<Content, String> contents = JacksonDBCollection.wrap(contentCollection, Content.class, String.class);
		Map<String, Object> response = new HashMap<String, Object>();
		Content found_content = null;
		
		if(searchContent.containsKey("name") && searchContent.get("name")!=null) {
			found_content = contents.findOne(new BasicDBObject("name", searchContent.get("name")));
			if(found_content!=null){
				response.put("matched", 1);
				response.put("content", found_content);
			}
			else{
				response.put("matched", 0);
			}
		}
			
		return response;
	}
	
	public Map<String, Object> getContent(Content content) {
		Map<String, Object> map = new TreeMap<String, Object>();
		if(content.getName()!=null && !"".equals(content.getName()))
			map.put("name", content.getName());

		return getContent(map);
	}


	@Override
	public Map<String, Object> setContent(String name, String lang, String type, byte[] content) {
		
		Content cnt = new Content();
		cnt.setName(name);
		cnt.setLang(lang);
		cnt.setType(type);
		cnt.setContent(content);
		
		return setContent(cnt);
	}
	
	
	// CREATE
	// ======
	public Map<String, Object> setContent(Content content) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<Content, String> contents = JacksonDBCollection.wrap(contentCollection, Content.class, String.class);
		
		// Match content
		Map<String, Object> result = getContent(content);

		// If new content
		if((int)result.get("matched")==0) {
			
			String savedId = contents.save(content).getSavedId();
			if(savedId!=null) {
				Content created_content = contents.findOneById(savedId);
				if(created_content!=null) {
					response.put("content", created_content);
					response.put("created", true);
					response.put("returnCode", 100);
				}
			}
		}
		// If existing content
		else if((int)result.get("matched")==1){
			Content existing_content = (Content) result.get("content");
			if(existing_content!=null) {
				content.set_previousId(existing_content.get_id());
				String savedId = contents.save(content).getSavedId();
				if(savedId!=null) {
					Content created_content = contents.findOneById(savedId);
					if(created_content!=null) {
						response.put("content", created_content);
						response.put("created", false);
						response.put("returnCode", 105);
					}
				}
			}
		}
		// If existing many contents
		else{
			response.put("created", new Boolean(false));
			response.put("returnCode", 110);
			response.put("contents", result.get("contents"));
		}

		return response;
	}


}
