package it.hash.osgi.resource.uuid.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import it.hash.osgi.resource.uuid.api.UUIDService;

public class UUIDServiceImpl implements UUIDService {
	private static final String COLLECTION = "UUID";
	
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	private DBCollection uuidCollection;

	public void start() {
		uuidCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@Override
	public synchronized String createUUID(String type) {
		boolean loop = true;
		int counter = 1;
		while (loop) {
			// RANDOM UUID
			String random_UUID = UUID.randomUUID().toString();
			DBObject found_uuid = uuidCollection.findOne(new BasicDBObject("uuid", random_UUID));
			if (found_uuid == null) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uuid", random_UUID);
				map.put("type", type);
				DBObject newUuid = new BasicDBObject(map);
				
			    //TODO inserire un controllo del salvataggio andato a buon fine
				uuidCollection.save(newUuid);

				loop = false;
				return random_UUID;
			} else
				loop = counter++ <= 10;
		}

		System.out.println("ERROR generating UUID");
		return null;

	}

	@Override
	public Map<String, Object> getTypeUUID(String uuid) {
		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found_uuid = uuidCollection.findOne(new BasicDBObject("uuid", uuid));
		if (found_uuid!=null)
		response.put("type",found_uuid.get("type"));
		else
			response.put("type","notFound");
		return response;
	}

	@Override
	public synchronized Map<String, Object> removeUUID(String uuid) {
		Map<String, Object> response = new HashMap<String, Object>();
		WriteResult wr;
		DBObject found_uuid = uuidCollection.findOne(new BasicDBObject("uuid", uuid));
		if (found_uuid != null) {
			wr = uuidCollection.remove(new BasicDBObject("uuid", uuid));
			if (wr.getN() == 1) {
				response.put("uuid",found_uuid);
				response.put("deleted", true);
				response.put("returnCode", 200);
			} else {
				response.put("deleted", false);
				response.put("returnCode",680);
			}

		}
		else {
			response.put("deleted", false);
			response.put("returnCode",680);
		}
		return response;
	}

	@Override
	public List<String>listUUID(String type) {
	   List<String> list_uuid = new ArrayList<String>();
	   DBObject dbo= new BasicDBObject("type", type);
		DBCursor dbc= uuidCollection.find(dbo);
		while (dbc.hasNext()) {
			list_uuid.add((String)dbc.next().get("uuid"));
		}
		
		return list_uuid;
	}

	
	@Override
	public boolean isUUID(String Uuid) {
		// TODO in java.util.UUID non esiste un metodo per vedere se Ã¨ un UUID
		// quindi la stringa Ã¨ un UUID se Ã¨ contenuta nella collezione
		String regex="[a-zA-z0-9]{1,}-[a-zA-z0-9]{1,}-[a-zA-z0-9]{1,}-[a-zA-z0-9]{1,}-[a-zA-z0-9]{1,}";
		
		if 	(Uuid.matches(regex))
			return true;
		return false;
	}

}
