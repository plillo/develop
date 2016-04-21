package it.hash.osgi.user.attribute.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;
import it.hash.osgi.utils.StringUtils;

public class AttributeServiceImpl implements AttributeService{
	private volatile AttributeServicePersistence _persistence;
	private volatile UUIDService _uuid;

	@Override
	public List<Attribute> getAttributesByCategories(List<String> categories) {
		return _persistence.getAttributesByCategories(categories);
	}
	
	@Override
	public List<Attribute> getCoreAttributes() {
		return _persistence.getCoreAttributes();
	}

	@Override
	public List<Attribute> getApplicationAttributes(String appid) {
		return _persistence.getApplicationAttributes(appid);
	}
   
	@Override
	public Map<String, Object> createAttribute(Attribute attribute) {
		String uuid = _uuid.createUUID("app/user/attribute");
		Map<String, Object> response = new HashMap<String, Object>();
		
		if (!StringUtils.isEmptyOrNull(uuid)) {
			attribute.setUuid(uuid);
			response= _persistence.createAttribute(attribute);
			if((Boolean)response.get("created")==false)
				_uuid.removeUUID(uuid);
		} else {
			response.put("created", false);
			response.put("returnCode", 630);
		}
		return response;
	}

	@Override
	public Map<String, Object> updateAttribute(String uuid, Attribute attribute) {
		return _persistence.updateAttribute(uuid, attribute);
	}

	@Override
	public Map<String, Object> deleteAttribute(String uuid) {
		return _persistence.deleteAttribute(uuid);
	}

	@Override
	public List<Attribute> getAttributes() {
		return _persistence.getAttributes();
	}

	@Override
	public Attribute getAttribute(String uuid) {
		return _persistence.getAttribute(uuid);
	}
}
