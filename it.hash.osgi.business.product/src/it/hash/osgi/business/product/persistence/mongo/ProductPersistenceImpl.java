package it.hash.osgi.business.product.persistence.mongo;

import static it.hash.osgi.utils.StringUtils.isEmptyOrNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import it.hash.osgi.business.product.Product;
import it.hash.osgi.business.product.persistence.api.ProductPersistence;
import it.hash.osgi.utils.StringUtils;

public class ProductPersistenceImpl implements ProductPersistence {
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	private static final String COLLECTION = "products";

	private DBCollection productsCollection;

	public void start() {
		productsCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}
 
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> createProduct(Product product) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Look for matching categories
		Map<String, Object> result = getProduct(product);

		// If new product
		if ((int) result.get("matched") == 0) {
			// Create product
			productsCollection.save(new BasicDBObject(Product.toMap(product)));
			
			// Get created product
			DBObject created = productsCollection.findOne(new BasicDBObject(Product.toMap(product)));

			// Build response
			if (created != null) {
				Product created_product = Product.toProduct(created.toMap());
				response.put("product", created_product);
				response.put("created", true);
				response.put("returnCode", 200);
			} else {
				response.put("created", false);
				response.put("returnCode", 630);
			}
		} else {
			response.put("created", false);
			response.put("returnCode", 640);
		}

		return response;
	}

	@Override
	public Map<String, Object> createProduct(Map<String, Object> product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getProduct(Product item) {
		if(item==null)
			return null;

		// Set map
		Map<String, Object> map = new TreeMap<String, Object>();
		if (!isEmptyOrNull(item.get_id()))
			map.put("_id", item.get_id());
		if (!isEmptyOrNull(item.getUuid()))
			map.put("uuid", item.getUuid());

		return getProduct(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getProduct(Map<String, Object> item) {

		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found = null;
		Product found_item = null;
		Map<Product, TreeSet<String>> matchs = new TreeMap<Product, TreeSet<String>>();

		if (item.containsKey("uuid") && item.get("uuid") != null) {
			found = productsCollection.findOne(new BasicDBObject("uuid", item.get("uuid")));

			if (found != null) {
				found_item = Product.toProduct(found.toMap());

				TreeSet<String> list = matchs.get(found_item);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_item, list);
			}
		}

		if (item.containsKey("_id") && item.get("_id") != null) {
			found = productsCollection.findOne(new BasicDBObject("_id", item.get("_id")));

			if (found != null) {
				found_item = Product.toProduct(found.toMap());
				TreeSet<String> list = matchs.get(found_item);
				if (list == null)
					list = new TreeSet<String>();

				list.add("_id");
				matchs.put(found_item, list);
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
			Product key = (Product) matchs.keySet().toArray()[0];
			response.put("product", key);
			response.put("keys", matchs.get(key));
			response.put("found", true);
			response.put("returnCode", 200);
			break;
		default:
			response.put("products", matchs);
			response.put("returnCode", 640);
		}

		return response;
	}
	
	private Product getProductByKey(String key, String value) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		Map<String, Object> response = getProduct(map);
		if (response.containsKey("product"))
			return (Product) response.get("product");

		return null;
	}

	@Override
	public Product getProductByUuid(String productUuid) {
		return getProductByKey("uuid", productUuid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> retrieveProducts(String uuid, String keyword) {
		List<Product> productsList = new ArrayList<Product>();
		BasicDBObject uuidQuery = null;
		BasicDBObject regexQuery = null;
		BasicDBObject query = null;
		DBCursor cursor = null;
		
		if(StringUtils.isNotEON(uuid)){
			uuidQuery = new BasicDBObject("business", uuid);
		}

		if(StringUtils.isNotEON(keyword)){
			regexQuery = new BasicDBObject();
			List<BasicDBObject> objs = new ArrayList<BasicDBObject>();
			objs.add(new BasicDBObject("code", new BasicDBObject("$regex", keyword).append("$options", "$i")));
			objs.add(new BasicDBObject("barcode", new BasicDBObject("$regex", keyword).append("$options", "$i")));
			objs.add(new BasicDBObject("_locDescription", new BasicDBObject("$regex", keyword).append("$options", "$i")));
			objs.add(new BasicDBObject("_locLongDescription", new BasicDBObject("$regex", keyword).append("$options", "$i")));
			
			regexQuery.put("$or", objs);
		}
		
		if(uuidQuery!=null && regexQuery!=null){
			query = new BasicDBObject();
			
			List<BasicDBObject> objs = new ArrayList<BasicDBObject>();
			objs.add(uuidQuery);
			objs.add(regexQuery);

			query.put("$and", objs);
		}
		else if(uuidQuery!=null)
			query = uuidQuery;
		else if(regexQuery!=null)
			query = regexQuery;
		
		// GET CURSOR
		cursor = query==null ? productsCollection.find() : productsCollection.find(query);
		
		// Product list construction
		while(cursor.hasNext())
			productsList.add(Product.toProduct(cursor.next().toMap()));
			
		return productsList;
	}
	
	@Override
	public List<Product> retrieveProducts(String keyword) {
		return retrieveProducts(null, keyword);
	}

	@Override
	public synchronized Map<String, Object> updateProduct(Product item) {
		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
		response = getProduct(item);
		if (response == null) {
			responseUpdate.put("update", "ERROR");
			responseUpdate.put("returnCode", 610);
		} else if ((int) response.get("matched") == 1) {
			// UNSET _ID
			item.set_id(null);
			BasicDBObject updateDocument = new BasicDBObject().append("$set", new BasicDBObject(Product.toMap(item)));
			BasicDBObject searchQuery = new BasicDBObject().append("uuid", item.getUuid());
			
			productsCollection.update(searchQuery, updateDocument);

			// Retrieve updated product
			DBObject updated = productsCollection.findOne(new BasicDBObject("uuid", item.getUuid()));
			
			@SuppressWarnings("unchecked")
			Product updatedProduct = Product.toProduct(updated.toMap());
			if (updatedProduct != null) {
				responseUpdate.put("product", updatedProduct);
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
	public Map<String, Object> updateProduct(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteProduct(String uuid) {
		Map<String, Object> response = new HashMap<String, Object>();
		com.mongodb.WriteResult wr;
		DBObject found_uuid = productsCollection.findOne(new BasicDBObject("uuid", uuid));
		if (found_uuid != null) {
			wr = productsCollection.remove(new BasicDBObject("uuid", uuid));
			if (wr.getN() == 1) {
				response.put("uuid", found_uuid);
				response.put("deleted", true);
				response.put("returnCode", 200);
			} else {
				response.put("deleted", false);
				response.put("returnCode", 680);
			}

		} else {
			response.put("deleted", false);
			response.put("returnCode", 680);
		}
		return response;
	}
	
	@Override
	public Map<String, Object> addPicture(String productUuid, String pictureUuid) {
		Map<String, Object> response = new HashMap<String, Object>();

		BasicDBObject updatedDocument = new BasicDBObject().append("$addToSet",
				new BasicDBObject().append("pictures", pictureUuid));
		BasicDBObject searchQuery = new BasicDBObject().append("uuid", productUuid);

		WriteResult wr = productsCollection.update(searchQuery, updatedDocument);

		if (wr.getN() == 0)
			response.put("update", false);
		else
			response.put("update", true);

		return response;
	}

	@Override
	public String getImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

}