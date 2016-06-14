package it.hash.osgi.user.attribute.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.resource.uuid.api.UuidService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;
import it.hash.osgi.utils.StringUtils;

@Component(immediate=true)
public class AttributeServiceImpl implements AttributeService {
	// References
	private AttributeServicePersistence _persistenceService;
	private UuidService _uuidService;
	
	@Reference(service=AttributeServicePersistence.class)
	public void setAttributeService(AttributeServicePersistence service){
		_persistenceService = service;
		doLog("AttributeServicePersistence: "+(service==null?"NULL":"got"));
	}
	
	public void unsetAttributeServicePersistence(AttributeServicePersistence service){
		doLog("AttributeServicePersistence: "+(service==null?"NULL":"released"));
		_persistenceService = null;
	}
	
	@Reference(service=UuidService.class)
	public void setUuidService(UuidService service){
		_uuidService = service;
		doLog("UuidService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUuidService(UuidService service){
		doLog("UuidService: "+(service==null?"NULL":"released"));
		_uuidService = null;
	}
	// === end references

	@Override
	public List<Attribute> getAttributesByCategories(List<String> categories) {
		return _persistenceService.getAttributesByCategories(categories);
	}
	
	@Override
	public List<Attribute> getCoreAttributes() {
		return _persistenceService.getCoreAttributes();
	}

	@Override
	public List<Attribute> getApplicationAttributes(String appid) {
		return _persistenceService.getApplicationAttributes(appid);
	}
   
	@Override
	public Map<String, Object> createAttribute(Attribute attribute) {
		String uuid = _uuidService.createUUID("app/user/attribute");
		Map<String, Object> response = new HashMap<String, Object>();
		
		if (!StringUtils.isEmptyOrNull(uuid)) {
			attribute.setUuid(uuid);
			response= _persistenceService.createAttribute(attribute);
			if((Boolean)response.get("created")==false)
				_uuidService.removeUUID(uuid);
		} else {
			response.put("created", false);
			response.put("returnCode", 630);
		}
		return response;
	}

	@Override
	public Map<String, Object> updateAttribute(String uuid, Attribute attribute) {
		return _persistenceService.updateAttribute(uuid, attribute);
	}

	@Override
	public Map<String, Object> deleteAttribute(String uuid) {
		return _persistenceService.deleteAttribute(uuid);
	}

	@Override
	public List<Attribute> getAttributes() {
		return _persistenceService.getAttributes();
	}

	@Override
	public Attribute getAttribute(String uuid) {
		return _persistenceService.getAttribute(uuid);
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
