package it.hash.osgi.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.hash.osgi.geojson.Point;
import it.hash.osgi.utils.StringUtils;

public class utilsBusiness {


	@SuppressWarnings("unchecked")
	public static Business toBusiness(Map<?, ?> mapBusiness) {
		
		Business business = new Business();
		String attribute = null;
	//	Map<String, Object> others = new TreeMap<String, Object>();
		Set<?> entry = mapBusiness.keySet();
		for (Object elem : entry) {
			attribute = (String) elem;
			if (attribute.equals("_id")) {
				Object o = mapBusiness.get(attribute).toString();
				business.set_id(o.toString());
			} else {

				switch (attribute) {
				case "uuid":
					business.setUuid((String) mapBusiness.get(attribute));
					break;
				case "name":
					business.setName((String) mapBusiness.get(attribute));
					break;
				case "pIva":
					business.setPIva((String) mapBusiness.get(attribute));
					break;
				case "fiscalCode":
					business.setFiscalCode((String) mapBusiness.get(attribute));
					break;
				case "address":
					business.setAddress((String) mapBusiness.get(attribute));
					break;
				case "city":
					business.setCity((String) mapBusiness.get(attribute));
					break;
				case "cap":
					business.setCap((String) mapBusiness.get(attribute));
					break;
				case "nation":
					business.setNation((String) mapBusiness.get(attribute));
					break;
				case "_description":
					business.set__Description((String) mapBusiness.get(elem));
					break;
				case "_longDescription":
					business.set__longDescription((String) mapBusiness.get(elem));
					break;
		
				
				case "category":
					   business.addCategory((String) mapBusiness.get(elem));
					break;
				case "categories":
					business.setCategories((List<String>) mapBusiness.get(elem));
					break;
				
				case "followers":
					business.setFollower((List<String>) mapBusiness.get(elem));
					break;
					
				case "position":
					Map<String, Object> position = (Map<String, Object>)mapBusiness.get("position");
					List<Double> coordinates = (List<Double>)position.get("coordinates");
					business.setPosition(new Point(coordinates.get(0).doubleValue(), coordinates.get(1).doubleValue()));
				    break;
				    
				case "email":
					business.setEmail((String) mapBusiness.get(elem));
					break;
				case "owner":
					business.setOwner((String) mapBusiness.get(elem));
					break;
				case "mobile":
					business.setMobile((String) mapBusiness.get(elem));
					break;

				case "published":
					business.setPublished((String) mapBusiness.get(elem));
					break;

				case "trusted_email":
					business.setTrusted_email((String) mapBusiness.get(elem));
					break;

				case "trusted_mobile":
					business.setTrusted_mobile((String) mapBusiness.get(elem));
					break;

				case "cauthor":
					business.setCauthor((String) mapBusiness.get(elem));
					break;
				case "cdate":
					business.setCdate((String) mapBusiness.get(elem));
					break;
				case "mauthor":
					business.setMauthor((String) mapBusiness.get(elem));
					break;
				case "mdate":
					business.setMdate((String) mapBusiness.get(elem));
					break;
				case "lauthor":
					business.setLauthor((String) mapBusiness.get(elem));
					break;
				case "ldate":
					business.setLdate((String) mapBusiness.get(elem));
					break;
				case "others":
					if (mapBusiness.get(elem) instanceof Map)
						business.setOthers((Map<String, Object>) mapBusiness.get(elem));
					break;
				default:
					
					if (business.getOthers() == null)
						business.setOthers(new HashMap<String, Object>());
					if (!business.getOthers().containsKey(attribute))
						business.getOthers().put(attribute, mapBusiness.get(elem));

				}
			}
		}
		return business;

	}
public static Map<String, Object> createPars(Business business) {

		Map<String, Object> pars = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(business.get_id()))
			pars.put("_id", business.get_id());
		if (!StringUtils.isEmptyOrNull(business.getUuid()))
			pars.put("uuid", business.getUuid());
		if (!StringUtils.isEmptyOrNull(business.getName()))
			pars.put("name", business.getName());
		if (!StringUtils.isEmptyOrNull(business.getPIva()))
			pars.put("pIva", business.getPIva());
		if (!StringUtils.isEmptyOrNull(business.getFiscalCode()))
			pars.put("fiscalCode", business.getFiscalCode());
		if (!StringUtils.isEmptyOrNull(business.getAddress()))
			pars.put("address", business.getAddress());
		if (!StringUtils.isEmptyOrNull(business.getCity()))
			pars.put("city", business.getCity());
		if (!StringUtils.isEmptyOrNull(business.getCap()))
			pars.put("cap", business.getCap());
		if (!StringUtils.isEmptyOrNull(business.getNation()))
			pars.put("nation", business.getNation());
		if (!StringUtils.isEmptyOrNull(business.get__Description()))
			pars.put("_description", business.get__Description());
		if (!StringUtils.isEmptyOrNull(business.get__longDescription()))
			pars.put("_longDescription", business.get__longDescription());
		if (!StringUtils.isEmptyOrNull(business.getOwner()))
			pars.put("owner", business.getOwner());
		if (business.getCategories() != null)
			pars.put("categories", business.getCategories());
		if (business.getFollowers()!=null)
			pars.put("followers", business.getFollowers());
		if (business.getPosition()!=null){
			Map <String,Object >pos = new HashMap<String,Object>();
			pos.put("type",business.getPosition().getType());
			pos.put("coordinates", business.getPosition().toString());
			pars.put("position", pos);}
		if (!StringUtils.isEmptyOrNull(business.getEmail()))
			pars.put("email", business.getEmail());
		if (!StringUtils.isEmptyOrNull(business.getMobile()))
			pars.put("mobile", business.getMobile());
		if (!StringUtils.isEmptyOrNull(business.getPublished()))
			pars.put("published", business.getPublished());
		if (!StringUtils.isEmptyOrNull(business.getTrusted_email()))
			pars.put("trusted_email", business.getTrusted_email());
		if (!StringUtils.isEmptyOrNull(business.getTrusted_mobile()))
			pars.put("trusted_mobile", business.getTrusted_mobile());
		if (!StringUtils.isEmptyOrNull(business.getCauthor()))
			pars.put("cauthor", business.getCauthor());
		if (!StringUtils.isEmptyOrNull(business.getCdate()))
			pars.put("cdate", business.getCdate());
		if (!StringUtils.isEmptyOrNull(business.getMauthor()))
			pars.put("mauthor", business.getMauthor());
		if (!StringUtils.isEmptyOrNull(business.getMdate()))
			pars.put("mdate", business.getMdate());
		if (!StringUtils.isEmptyOrNull(business.getLdate()))
			pars.put("ldate", business.getLdate());
		if (business.getOthers() != null)
			pars.put("others", business.getOthers());

		return pars;
	}


}
