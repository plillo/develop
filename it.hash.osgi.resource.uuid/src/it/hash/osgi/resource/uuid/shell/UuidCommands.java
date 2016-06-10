package it.hash.osgi.resource.uuid.shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import it.hash.osgi.resource.uuid.api.UuidService;


@Component(immediate=true,properties={
		CommandProcessor.COMMAND_SCOPE+":String=uuid",
		CommandProcessor.COMMAND_FUNCTION+":String=create",
		CommandProcessor.COMMAND_FUNCTION+":String=delete",
		CommandProcessor.COMMAND_FUNCTION+":String=list",
		CommandProcessor.COMMAND_FUNCTION+":String=get"})
public class UuidCommands {
	private volatile UuidService serviceUUID;

	@Reference(service=UuidService.class)
	public void setUUIDService(UuidService service){
		serviceUUID = service;
		System.out.println("Reference uuidService");
	}
	
	public void unsetUUIDService(UuidService service){
		serviceUUID = null;
	}
	@Activate
	public void Activate (){
		System.out.println("ShellCommands UUID Activate");
	}
	
	public List<String> list(String type) {
		List<String> response = serviceUUID.listUUID(type);
		return response;
	}

	public String get(String uuid) {
		Map<String, Object> response = serviceUUID.getTypeUUID(uuid);
		System.out.println("UUID  " + uuid);
		System.out.println("Type " + response.get("type"));
		return (String) response.get("type");

	}

	public void create(String type) {
		String uuid = serviceUUID.createUUID(type);
		if (uuid != null) {
			System.out.println("Created UUID" + uuid);
		}
	}

	public void delete(String uuid) {
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", uuid);

		Map<String, Object> ret = serviceUUID.removeUUID(uuid);
		System.out.println("deleted " + ret.get("deleted"));
	}

}
