package it.hash.osgi.user.shell;

import static it.hash.osgi.utils.StringUtils.isEON;
import static it.hash.osgi.utils.StringUtils.defaultIfNullOrEmpty;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.felix.service.command.CommandProcessor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.user.User;
import it.hash.osgi.user.service.api.Status;
import it.hash.osgi.user.service.api.UserService;

@Component(
	immediate=true, 
	service = Commands.class, 
	property = {
		CommandProcessor.COMMAND_SCOPE+"=user",
		CommandProcessor.COMMAND_FUNCTION+"=login",
		CommandProcessor.COMMAND_FUNCTION+"=create",
		CommandProcessor.COMMAND_FUNCTION+"=number",
		CommandProcessor.COMMAND_FUNCTION+"=list",
		CommandProcessor.COMMAND_FUNCTION+"=identify",
		CommandProcessor.COMMAND_FUNCTION+"=chapw",	
		CommandProcessor.COMMAND_FUNCTION+"=UUID"
	}
)
public class Commands {
	// References
	private UserService _userService;
	
	@Reference(service=UserService.class)
	public void setUserService(UserService service){
		_userService = service;
		doLog("UserService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUserService(UserService service){
		doLog("UserService: "+(service==null?"NULL":"released"));
		_userService = null;
	}

	public void login(String identificator, String password) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		if(isEON(password)) {
			response.put("isLogged", false);
			response.put("status", Status.ERROR_MISSING_PASSWORD);
			response.put("message", Status.ERROR_MISSING_PASSWORD.getMessage());
			println(response);
			
			return;
		}
		else 
			println(_userService.validateIdentificatorAndLogin(identificator, password, null));
	}
	
	// Change password
	public void chapwd(String identificator, String oldPassword, String password) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		if(isEON(password) || isEON(oldPassword)) {
			response.put("isLogged", false);
			if(isEON(password) && isEON(oldPassword)) {
				response.put("status", Status.ERROR_MISSING_NEW_AND_OLD_PASSWORD);
				response.put("message", Status.ERROR_MISSING_NEW_AND_OLD_PASSWORD.getMessage());
			}
			else 
				if(isEON(password)) {
					response.put("status", Status.ERROR_MISSING_PASSWORD);
					response.put("message", Status.ERROR_MISSING_PASSWORD.getMessage());
				}
				else {
					response.put("status", Status.ERROR_MISSING_OLD_PASSWORD);
					response.put("message", Status.ERROR_MISSING_OLD_PASSWORD.getMessage());
				}

			println(response);
			
			return;
		}
		else 
			println(_userService.changePassword(identificator, oldPassword, password));
	}
	
	public void create(String identificator, String firstname, String lastname, String password) {
		User user = new User();
		
		Map<String, Object> map = _userService.validateIdentificator(identificator);
		String identificator_type = (String)map.get("identificatorType");
		
		if((Boolean)map.get("isValid")) {
			// Get the user (if any) matching the identificator
			// ================================================
			map = new TreeMap<String, Object>();

			if("username".equals(identificator_type))
				user.setUsername(identificator);
			else if("email".equals(identificator_type))
				user.setEmail(identificator);
			else if("mobile".equals(identificator_type))
				user.setMobile(identificator);

			user.setFirstName(firstname);
			user.setLastName(lastname);
			user.setPassword(password);
		
			Map<String, Object> response = _userService.createUser(user);
			
			System.out.println("shell command 'user:create'");
			System.out.println(String.format("-> %s [%d]", response.get("message"), (Integer) response.get("status")));
		}
		else
			System.out.println("shell command 'user:create' - "+identificator+": not a valid identificator.");
	}

	public void number() {
		System.out.println("users number: "+_userService.getUsers().size());
	}
	
	public void list() {
		List<User> users = _userService.getUsers();
		
		if(users!=null){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				System.out.println(String.format("%-20s|%-20s|%-20s|%-20s", defaultIfNullOrEmpty(user.get_id(),"#"), defaultIfNullOrEmpty(user.getUuid(),"#"), defaultIfNullOrEmpty(user.getLastName(),"#"), defaultIfNullOrEmpty(user.getFirstName(),"#")));
			}
		}
	}
	
	public void UUID() {
		String UUID = _userService.getUUID();
		System.out.println(String.format("Current user UUID: %s",UUID==null?"NO ONE":UUID));
	}
	
	public void identify(String identificator) {
		Map<String, Object> response = _userService.validateIdentificatorAndGetUser(identificator);
		
		ObjectMapper om = new ObjectMapper();
		String json;
		try {
			json = om.writerWithDefaultPrettyPrinter().writeValueAsString(response);
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
	
	private void println(Object object){
		ObjectMapper om = new ObjectMapper();
		
		String json;
		try {
			json = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
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
