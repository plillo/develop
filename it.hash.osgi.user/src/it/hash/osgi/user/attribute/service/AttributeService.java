package it.hash.osgi.user.attribute.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.user.attribute.Attribute;

public interface AttributeService {

	Map<String, Object> createAttribute(Attribute attribute);
	Map<String, Object> updateAttribute(String uuid, Attribute attribute);
	Map<String, Object> deleteAttribute(String uuid);

	List<Attribute> getAttributesByCategories(List<String> categories);
	List<Attribute> getCoreAttributes();
	List<Attribute> getApplicationAttributes(String appid);
	   
	List<Attribute> getAttributes();
	Attribute getAttribute(String uuid);
}
