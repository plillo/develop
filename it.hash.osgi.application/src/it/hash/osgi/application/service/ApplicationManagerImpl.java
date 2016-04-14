package it.hash.osgi.application.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.hash.osgi.user.attribute.Attribute;

public class ApplicationManagerImpl implements ApplicationManager{
	
	Map<String, ApplicationService> applications = new HashMap<String, ApplicationService>();

	@Override
	public void filterAttributes(String appCode, List<Attribute> attributes) {
		ApplicationService service = applications.get(appCode);
		if(service!=null)
			service.filterAttributes(attributes);
	}

	public void add(ApplicationService application){
		System.out.println("Registering in whiteboard: "+application.getCode());
		applications.put(application.getCode(), application);
	}
	
	public void remove(ApplicationService application){
		System.out.println("Deleting from whiteboard: "+application.getCode());
		applications.remove(application);
	}
}
