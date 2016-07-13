package it.hash.osgi.business.shell;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessPersistenceService;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.utils.StringUtils;

@Component(
	immediate=true, 
	service = Commands.class, 
	property = {
		CommandProcessor.COMMAND_SCOPE+"=business",
		CommandProcessor.COMMAND_FUNCTION+"=add",
		CommandProcessor.COMMAND_FUNCTION+"=delete",
		CommandProcessor.COMMAND_FUNCTION+"=deleteById",
		CommandProcessor.COMMAND_FUNCTION+"=list",
		CommandProcessor.COMMAND_FUNCTION+"=update",
		CommandProcessor.COMMAND_FUNCTION+"=get",
		CommandProcessor.COMMAND_FUNCTION+"=getByFiscalCode",
		CommandProcessor.COMMAND_FUNCTION+"=notFollowed",
		CommandProcessor.COMMAND_FUNCTION+"=updateBusinessLogo",
		CommandProcessor.COMMAND_FUNCTION+"=followedBusinesses",
		CommandProcessor.COMMAND_FUNCTION+"=followedBusinessesCategories"
	}
)
public class Commands {
	// References
	private BusinessService _businessService;
	private BusinessPersistenceService _businessPersistenceService;

	@Reference(service=BusinessService.class)
	public void setBusinessService(BusinessService service){
		_businessService = service;
		doLog("BusinessService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetBusinessService(BusinessService service){
		doLog("BusinessService: "+(service==null?"NULL":"released"));
		_businessService = null;
	}
	
	@Reference(service=BusinessPersistenceService.class)
	public void setBusinessPersistenceService(BusinessPersistenceService service){
		_businessPersistenceService = service;
		doLog("BusinessPersistenceService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetBusinessPersistenceService(BusinessPersistenceService service){
		doLog("BusinessPersistenceService: "+(service==null?"NULL":"released"));
		_businessPersistenceService = null;
	}
	// === end references


	// SHELL COMMANDS
	// ==============
	
	// getByFiscalCode
	public void getByFiscalCode(String fiscalCode) {
		Business business = _businessPersistenceService.getBusinessByFiscalCode(fiscalCode);
		System.out.println(">> " + business.getFiscalCode());
	}

	// add
	public void add(String name, String fiscalCode, String partitaIva) {
		Business business = new Business();
		business.setName(name);
		business.setFiscalCode(fiscalCode);
		business.setPIva(partitaIva);

		Map<String, Object> ret = _businessService.createBusiness(business);
		business = (Business) ret.get("business");

		System.out.println(String.format(">> %s", (Boolean) ret.get("created") ? business!=null ? business.get_id():"ERROR" :"not created"));
	}

	// update
	public void update(String uuid, String name, String email, String mobile, String category) {
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

	// delete
	public void delete(String uuid) {
		Map<String, Object> ret = _businessService.deleteBusiness(uuid);
		System.out.println("returnCode " + ret.get("returnCode"));
	}
	
	// delete
	public void deleteById(String id) {
		Map<String, Object> ret = _businessService.deleteBusinessById(id);
		System.out.println("returnCode " + ret.get("returnCode"));
	}

	// get
	public void get(String uuid) {
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", uuid);
		Map<String, Object> ret = _businessService.getBusiness(pars);
		Business business = (Business) ret.get("business");
		if (business != null) {
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

	// list
	public void list() {
		List<Business> businesses = _businessService.getBusinesses();
		if (businesses != null) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				System.out.println(String.format("%-20s, %-20s, %-20s", 
						business.get_id(), 
						StringUtils.defaultIfNullOrEmpty(business.getUuid(),"#"), 
						StringUtils.defaultIfNullOrEmpty(business.getName(),"#")));
			}
		}
	}

	public void notFollowed(String uuid, String search) {
		for (Business business : _businessService.retrieveNotFollowedByUser(uuid, search)) {
			System.out.println(String.format("%-20s%-20s", business.getName(), business.getUuid()));
		}
	}
	
	// followedBusinesses
	public void followedBusinesses(String uuid) {
		Collection<String> followed = _businessPersistenceService.retrieveFollowerBusinessesUuids(uuid);
		for(String fuuid:followed)
			System.out.println(">> " + fuuid);
	}

	// followedBusinessesCategories
	public void followedBusinessesCategories(String uuid) {
		Collection<String> followed = _businessPersistenceService.retrieveFollowerBusinessesCategoriesUuids(uuid);
		for(String cuuid:followed)
			System.out.println(">> " + cuuid);
	}
	
	public void updateBusinessLogo(String uuid, String urlLogo) throws Exception {
		Map<String, Object> response = new TreeMap<String, Object>();

		// GET content from HTTP connection
		if (urlLogo.startsWith("http")) {
			URL url = new URL(urlLogo);

			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// Get MIME-TYPE
				Map<String, List<String>> map = httpURLConnection.getHeaderFields();
				List<String> content_type = map.get("Content-Type");
				byte[] bytes = IOUtils.toByteArray(url.openConnection().getInputStream());

				// PUT Base64 encoded content
				response = _businessService.updateBusinessLogo(uuid, content_type.get(0), Base64.encodeBase64(bytes));
			} else
				response.put("returnCode", "KO");
		} else
			response.put("returnCode", "TODO");

		System.out.println("ReturnCode " + response.get("returnCode"));
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
	
	
	
