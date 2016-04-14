package it.hash.osgi.user.attribute.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

public class Commands {
	private volatile AttributeService _attributeService;
	
	public void createAttribute(String name, String label, String context){
		Attribute attribute = new Attribute();
		attribute.setName(name);
		attribute.setLabel(label);
		attribute.setApplications(new ArrayList<Map<String,Object>>());
		_attributeService.createAttribute(attribute);
	}
	
	public void getAttributesByCategories(String categories){
		List<String> listS= new ArrayList<String>();
		String[] cat = categories.split(",");
		for(String cat1: cat){
			listS.add(cat1);
		}
		List<Attribute> list = _attributeService.getAttributesByCategories(listS);
		
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
}
