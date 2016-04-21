package it.hash.osgi.business.category.parser;

import java.util.HashMap;
import java.util.Map;

public class ParserManagerImpl implements ParserManager {
	Map<String, ParserService> applications = new HashMap<String, ParserService>();

	public void add(ParserService parserService){
		System.out.println("Registering in whiteboard: "+parserService.getAppCode());
		applications.put(parserService.getAppCode(), parserService);
	}
	
	public void remove(ParserService parserService){
		System.out.println("Deleting from whiteboard: "+ parserService.getAppCode());
		applications.remove(parserService);
	}

	@Override
	public boolean addCategoriesByUrl(String type, String url) {
		ParserService service = applications.get(type);
		if(service!=null)
		return 	service.addCategoriesByUrl(url);
		
		return false;
	}
}