package it.hash.osgi.business.shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.BusinessService;

public class businessCommands {
	private volatile BusinessService _businessService;
	private volatile BusinessServicePersistence _bsi;
	public void getByCodiceFiscale(String fiscalCode){
	Business business=_bsi.getBusinessByFiscalCode(fiscalCode);
	System.out.println( "TROVATO : "+business.getFiscalCode());
}  
	// name
	// email,
	// mobile
	// Category
public void addBusiness(String name, String fiscalCode, String partitaIva) {
//public void addBusiness(String name, String fiscalCode, String partitaIva, String Category) {
		Business business = new Business();
		business.setName(name);
		business.setFiscalCode(fiscalCode);;
		business.setPIva(partitaIva);
	//	List<String> categories = new ArrayList<String>();
	//	categories.add(Category);
	//	business.setCategories(categories);
    //  longitudine e latitudine
		business.setPosition(48.32222, 32.222222);

		Map<String, Object> ret = _businessService.createBusiness(business);
		business = null;
		business = (Business) ret.get("business");
		if (business != null) {
			System.out.println("ADD business_Id" + business.get_id());
		}
		for (Map.Entry<String, Object> entry : ret.entrySet())
			System.out.println(entry.getKey() + " - " + entry.getValue().toString());

		System.out.println("called shell command 'createBusiness' - created: " + (Boolean) ret.get("created"));
	}

	public void updateBusiness(String uuid, String name, String email, String mobile, String category) {
		Map<String, Object> pars = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Business business = new Business();
		business.setName(name);
		business.setEmail(email);
		business.setMobile(mobile);
		business.setUuid(uuid);
		List<String> categories = new ArrayList<String>();
		categories.add(category);
		business.setCategories(categories);
		pars.put("business", business);
		pars.put("uuid", uuid);
		response = _businessService.updateBusiness(uuid, pars);
		System.out.println("ReturnCode " + response.get("returnCode"));
	}

	public void deleteBusiness(String uuid) {
		Map<String, Object> ret = _businessService.deleteBusiness(uuid);
		System.out.println("returnCode " + ret.get("returnCode"));
	}

	public void getBusiness(String uuid) {
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", uuid);
		Map<String, Object> ret = _businessService.getBusiness(pars);
		Business business = (Business) ret.get("business");
		if (business!=null){
			System.out.println(String.format("%-20s%-20s%-20s%-20s", business.getName(), business.getEmail(),
					business.getMobile(), business.getUuid()));
			List<String> cat = business.getCategories();
			if (cat != null) {
				for (String id : cat) {
					System.out.println(" Category: " + id);
				}
			}
		}
		
	}

	public void listBusiness() {
		List<Business> businesses = _businessService.getBusinesses();
		if (businesses != null) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				System.out.println(String.format("%-20s%-20s%-20s%-20s", business.getName(),
						business.getEmail(), business.getMobile(), business.getUuid()));
				List<String> cat = business.getCategories();
				if (cat != null) {
					for (String id : cat) {
						System.out.println(" Category: " + id);
					}
				}
			}
		}

	}

	public void notFollowed(String uuid, String search) {
		for(Business business:_businessService.retrieveNotFollowedByUser(uuid, search)){
			System.out.println(String.format("%-20s%-20s", business.getName(), business.getUuid()));
		}
	}
}
	
	
	
