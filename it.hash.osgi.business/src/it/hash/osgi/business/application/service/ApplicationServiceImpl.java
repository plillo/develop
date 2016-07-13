package it.hash.osgi.business.application.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.application.service.ApplicationService;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.service.api.UserService;

@Component(immediate=true)
public class ApplicationServiceImpl implements ApplicationService{

	// References
	private UserService _userService;
	private BusinessService _businessService;
	
	@Reference(service=BusinessService.class)
	public void setBusinessService(BusinessService service){
		_businessService = service;
		doLog("BusinessService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetBusinessService(BusinessService service){
		doLog("BusinessService: "+(service==null?"NULL":"released"));
		_businessService = null;
	}
	
	@Reference(service=UserService.class)
	public void setUserService(UserService service){
		_userService = service;
		doLog("UserService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUserService(UserService service){
		doLog("UserService: "+(service==null?"NULL":"released"));
		_userService = null;
	}
	// === end references
	
	@Override
	public String getCode() {
		return "bsnss-v1.0";
	}

	@Override
	public List<Attribute> filterAttributes(List<Attribute> attributes) {
		List<Attribute> filteredList = new ArrayList<Attribute>();
		Collection<String> ctgs = _businessService.retrieveUserBusinessesCategoriesUuids(_userService.getUUID());
		
		for(Attribute attribute : attributes) {
			List<Map<String,Object>> applications = attribute.getApplications();
			for(Map<String,Object> application : applications) {
				if(getCode().equals(application.get("appcode"))) {
					Map<String,Object> context = (Map<String,Object>)application.get("context");
					ArrayList<String> categories = (ArrayList<String>)context.get("categories");
					
					// Pass attribute if match categories
					for(String cuuid : ctgs) 
						if(categories.contains(cuuid)) {
							filteredList.add(attribute);
							break;
						}
				}
			}
		}
			
		System.out.println("Business application service: filtering attributes...");
		
		return filteredList;
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }

}
