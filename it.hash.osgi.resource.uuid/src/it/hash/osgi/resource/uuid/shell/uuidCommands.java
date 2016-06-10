package it.hash.osgi.resource.uuid.shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import it.hash.osgi.resource.uuid.api.UUIDService;

@Component(properties={CommandProcessor.COMMAND_SCOPE+":String=uuid",
		CommandProcessor.COMMAND_FUNCTION+":String=create",
		CommandProcessor.COMMAND_FUNCTION+":String=delete",
		CommandProcessor.COMMAND_FUNCTION+":String=list",
		CommandProcessor.COMMAND_FUNCTION+":String=get"})
public class uuidCommands {
	private volatile UUIDService _uuidService;

	@Reference(service=UUIDService.class)
	public void setUUIDService(UUIDService service){
		_uuidService = service;
	}
	
	public void unsetMongoDBService(UUIDService service){
		_uuidService = null;
	}
	
	public List<String> list(String type) {
		List<String> response = _uuidService.listUUID(type);
		return response;
	}

	public String get(String uuid) {
		Map<String, Object> response = _uuidService.getTypeUUID(uuid);
		System.out.println("UUID  " + uuid);
		System.out.println("Type " + response.get("type"));
		return (String) response.get("type");

	}

	public void create(String type) {
		String uuid = _uuidService.createUUID(type);
		if (uuid != null) {
			System.out.println("Created UUID" + uuid);
		}
	}

	public void delete(String uuid) {
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", uuid);

		Map<String, Object> ret = _uuidService.removeUUID(uuid);
		System.out.println("deleted " + ret.get("deleted"));
	}

}
