package it.hash.osgi.resource.uuid.shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.hash.osgi.resource.uuid.api.UUIDService;

public class uuidCommands {
	private volatile UUIDService _uuidService;

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
