package it.hash.osgi.user.attribute.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

@Component(
	immediate=true, 
	service = Commands.class, 
	property = {
		CommandProcessor.COMMAND_SCOPE+"=userattr",
		CommandProcessor.COMMAND_FUNCTION+"=create",
		CommandProcessor.COMMAND_FUNCTION+"=retrieveByCategories"
	}
)
public class Commands {
	// References
	private AttributeService _attributeService;
	
	@Reference(service=AttributeService.class)
	public void setAttributeService(AttributeService service){
		_attributeService = service;
		doLog("AttributeService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetAttributeService(AttributeService service){
		doLog("AttributeService: "+(service==null?"NULL":"released"));
		_attributeService = null;
	}
	// === end references
	
	
	// SHELL COMMANDS
	// ==============
	// create
	public void create(String name, String label, String context){
		Attribute attribute = new Attribute();
		attribute.setName(name);
		attribute.setLabel(label);
		attribute.setApplications(new ArrayList<Map<String,Object>>());
		_attributeService.createAttribute(attribute);
	}
	
	// retrieveByCategories
	public void retrieveByCategories(String categories){
		List<String> categoriesList = new ArrayList<String>();
		
		if(!"all".equalsIgnoreCase(categories)) {
			String[] arrctg = categories.split(",");
			for(String cat: arrctg){
				categoriesList.add(cat);
			}
		}
		
		List<Attribute> list = _attributeService.getAttributesByCategories(categoriesList);
		
		ObjectMapper om = new ObjectMapper();
		String json;
		try {
			json = om.writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			json = e.toString();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			json = e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			json = e.toString();
		}
		
		System.out.println(json);
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
