package it.hash.osgi.business.persistence.mock;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.utils.StringUtils;

     
public class BusinessServicePersistenceImpl implements BusinessServicePersistence, ManagedService {
	List<Business> businesses = new ArrayList<Business>();
	@SuppressWarnings("rawtypes")
	Dictionary properties;
    private volatile UUIDService _uuid;
	
    @SuppressWarnings("unchecked")
	private Business createBusiness(Map<String, Object> mapbusiness) {
    	// .. abbiamo detto che in un database Nosql non si ha uno schema fisso
    			// per cui
    			// dobbiamo controllare quali attributi andranno settati!!!
    			// TODO : abbiamo previsto una variabile di istanza come Map in modo da
    			/* poter inserire attributi dell'entità business non previsti al momento della
    			 * progettazione. 
    			 * Se vogliamo aggiungere un nuovo attributo
    			 *    nomeAttributo-valore ( verra aggiunto nella Map)
    			 * Se vogliamo sostituire la Map
    			 *    others-Map
    			 * Se vogliamo aggiungere una categoria di merce alla lista già esistente
    			 *    category-idCategory
    			 *  se vogliamo sostituire la lista delle Categorie
    			 *      categories- List
    			 */
    	
    			Business business = new Business();
    			String attribute = null;
    			Map<String, Object> others = new TreeMap<String, Object>();
    			for (Map.Entry<String, Object> entry : mapbusiness.entrySet()) {
    				attribute = entry.getKey();
    				if (attribute.equals("_id")) {
    					business.set_id(entry.getValue().toString());
    				} else {

    					switch (attribute) {
    					case "uuid":
    						business.setUuid((String) entry.getValue());
    						break;
    					case "name":
    						business.setName((String) entry.getValue());
    						break;
    					case "pIva":
    						business.setPIva((String) entry.getValue());
    						break;
    					case "fiscalCode":
    						business.setFiscalCode((String) entry.getValue());
    						break;
    					case "address":
    						business.setAddress((String) entry.getValue());
    						break;
    					case "city":
    						business.setCity((String) entry.getValue());
    						break;
    					case "cap":
    						business.setCap((String) entry.getValue());
    						break;
    					case "nation":
    						business.setNation((String) entry.getValue());
    						break;
    					case "_description":
    						business.set__Description((String) entry.getValue());
    						break;
    					case "_longDescription":
    						business.set__longDescription((String) entry.getValue());
    						break;
    					case "category":
    						if (business.getCategories() == null)
    							business.setCategories(new ArrayList<String>());
    						business.addCategory((String) entry.getValue());
    						break;
    					case "email":
    						business.setEmail((String) entry.getValue());
    						break;

    					case "mobile":
    						business.setMobile((String) entry.getValue());
    						break;

    					case "published":
    						business.setPublished((String) entry.getValue());
    						break;

    					case "trusted_email":
    						business.setTrusted_email((String) entry.getValue());
    						break;

    					case "trusted_mobile":
    						business.setTrusted_mobile((String) entry.getValue());
    						break;

    					case "cauthor":
    						business.setCauthor((String) entry.getValue());
    						break;
    					case "cdate":
    						business.setCdate((String) entry.getValue());
    						break;
    					case "mauthor":
    						business.setMauthor((String) entry.getValue());
    						break;
    					case "mdate":
    						business.setMdate((String) entry.getValue());
    						break;
    					case "lauthor":
    						business.setLauthor((String) entry.getValue());
    						break;
    					case "ldate":
    						business.setLdate((String) entry.getValue());
    						break;
    					case "categories":
    						business.setCategories((List<String>) entry.getValue());
    						break;
    					case "others":
    						business.setOthers((Map<String, Object>) entry.getValue());
    						break;
    					default:
    						if (business.getOthers() == null)
    							business.setOthers(new HashMap<String, Object>());
    						if (!business.getOthers().containsKey(attribute))
    							others.put(attribute, entry.getValue());

    					}
    				}
    			}
    			return business;

    		}

	@Override
	public Map<String, Object> addBusiness(Business business) {
      String uuid=_uuid.createUUID(business.get_id());
		Map<String, Object> map = new TreeMap<String, Object>();
	
		business.setUuid(uuid);
		map.put("Result", businesses.add(business));
		map.put("created", "true");
		map.put("businessId", business.get_id());
		return map;
	}

	@Override
	public Map<String, Object> addBusiness(Map<String, Object> mapbusiness) {
		System.out.println("Add Business for cfg: ");
		return addBusiness(createBusiness(mapbusiness));
	}

	
	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {


		Business businessObj = new Business();

		if (StringUtils.isNotEmptyOrNull((String) business.get("businessId")))
			businessObj.set_id((String) business.get("businessId"));
		if (StringUtils.isNotEmptyOrNull((String) business.get("email")))
			businessObj.setEmail((String) business.get("email"));
		if (StringUtils.isNotEmptyOrNull((String) business.get("mobile")))
			businessObj.setMobile((String) business.get("mobile"));
	    
		return getBusiness(businessObj);
	}
@Override
	public Map<String,Object>  getBusiness(Business business){
		Map<Business, TreeSet<String>> matchs = new TreeMap<Business, TreeSet<String>>();
		Map<String,Object> response = new HashMap<String,Object>();
		Business found_business = null;
		
			if(business.get_id()!=null) {
			found_business = getBusinessById(business.get_id());

			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("businessId");
				matchs.put(found_business, list);
			}
		}
		
		if(business.getName()!=null) {
			found_business = getBusinessByName(business.getName());

			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("busienssname");
				matchs.put(found_business, list);
			}
		}
		
		if(business.getPIva()!=null) {
			found_business= getBusinessByPartitaIva(business.getPIva());
			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("partitaIva");
				matchs.put(found_business, list);
			}
		}
		if(business.getFiscalCode()!=null) {
			found_business= getBusinessByFiscalCode(business.getFiscalCode());
			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("fiscalCode");
				matchs.put(found_business, list);
			}
		}
		// Set response: number of matched users
					response.put("matched", matchs.size());

					// Set response details
					switch(matchs.size()){
					case 0:
						response.put("found", false);
						response.put("returnCode",400);
						break;
					case 1:
						Business key = (Business) matchs.keySet().toArray()[0];
						response.put("business", key);
						response.put("keys", matchs.get(key));
						response.put("found", true);
						response.put("returnCode",200);
						break;
					default:
						response.put("businsess", matchs);
						response.put("returnCode", 110);
					}
				
						
				return response;
			}
	@Override
	public List<Business> getBusinesses() {
			System.out.println(this.getImplementation());
		return businesses;
	}

	@Override
	public Business getBusinessByPartitaIva(String partitaIva) {
		if (!StringUtils.isEmptyOrNull(partitaIva)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (partitaIva.equals(business.getPIva()))
					return business;
			}
		}
		return null;
	}

	@Override
	public Business getBusinessByFiscalCode(String fiscalCode) {
		if (!StringUtils.isEmptyOrNull(fiscalCode)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (fiscalCode.equals(business.getFiscalCode()))
					return business;
			}
		}

		return null;
	}

	@Override
	public Business getBusinessByName(String name) {
		System.out.println();
		if (!StringUtils.isEmptyOrNull(name)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (name.equals(business.getName()))
					return business;
			}
		}

		return null;
	}

	@Override
	public Business getBusinessById(String businessId) {
		if (!StringUtils.isEmptyOrNull(businessId)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (businessId.equals(business.get_id()))
					return business;
			}
		}

		return null;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, Object> updateBusiness(String uuid, Business business) {

		Map<String, Object> result = new TreeMap<String, Object>();
		if (businesses.contains(business)) {
			businesses.remove(business);
			businesses.add(business);
			result.put("result", "true");
			result.put("Update", business.get_id());
		} else
			result.put("result", "false");

		return result;
	}

	@Override
	public Map<String, Object> updateBusiness(String uuid, Map<String, Object> par) {

		System.out.println("Update Business for cfg: ");
		
		Business business = createBusiness(par);

		return updateBusiness(uuid, business);
	}
	
	@Override
	public Map<String, Object> updateBusinessLogo(String uuid, String type, byte[] encodeBase64) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImplementation() {
		return "mocked";
	}

	private Business createBusiness(String b) {
		Business business = new Business();
		String[] company = b.split("&");
		for (String s : company) {
			String attribute[] = s.split("=");
			if (attribute[0].equals("_id"))
				business.set_id(attribute[1]);
			if (attribute[0].equals("username"))
				business.setName(attribute[1]);
			if (attribute[0].equals("email"))
				business.setEmail(attribute[1]);
			if (attribute[0].equals("mobile"))
				business.setMobile(attribute[1]);
		}

		return business;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties != null) {

			businesses.add(createBusiness(((String) properties.get("Business_01"))));
			businesses.add(createBusiness(((String) properties.get("Business_02"))));
			this.properties = properties;
		}
	}

	@Override
	public Business getBusinessByUuid(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> retrieveBusinesses(String criterion, String search) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> follow(String businessUuid, String actual_user_uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> retrieveFollowedByUser(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> retrieveOwnedByUser(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> retrieveNotFollowedByUser(String userUuid, String search) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> unFollow(String businessUuid, String userUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteBusiness(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> retrieveFollowedByUser(String uuid, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> retrieveOwnedByUser(String uuid, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> retrieveNotFollowedByUser(String userUuid, String search, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getBusiness(Business business, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessByFiscalCode(String fiscalCode, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessByPartitaIva(String partitaIva, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessByName(String Name, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessById(String businessId, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessByUuid(String uuid, boolean withLogo) {
		// TODO Auto-generated method stub
		return null;
	}
}




