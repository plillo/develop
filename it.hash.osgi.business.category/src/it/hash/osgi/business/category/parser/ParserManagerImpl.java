package it.hash.osgi.business.category.parser;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(immediate=true)
public class ParserManagerImpl implements ParserManager {
	Map<String, ParserService> applications = new HashMap<String, ParserService>();

	@Reference(cardinality=ReferenceCardinality.AT_LEAST_ONE, policy=ReferencePolicy.DYNAMIC)
	void addParserService(ParserService parserService) {
		applications.put(parserService.getParserCode(), parserService);
		doLog("ParserService n."+applications.size());
	}
	
	void removeParserService(ParserService parserService) {
		applications.remove(parserService);
		doLog("ParserService n."+applications.size());
	}
	
/*	
	public void add(ParserService parserService){
		System.out.println("Registering in whiteboard: "+parserService.getParserCode());
		applications.put(parserService.getParserCode(), parserService);
	}
	
	public void remove(ParserService parserService){
		System.out.println("Deleting from whiteboard: "+ parserService.getParserCode());
		applications.remove(parserService);
	}
*/
	
	@Override
	public boolean addCategoriesByUrl(String type, String url) {
		ParserService service = applications.get(type);
		if(service!=null)
		return 	service.addCategoriesByUrl(url);
		
		return false;
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
