package it.hash.osgi.user.persistence.shell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.user.User;
import it.hash.osgi.user.persistence.api.UserPersistenceService;
import it.hash.osgi.user.service.api.UserService;
import it.hash.osgi.utils.StringUtils;

@Component(
	immediate=true, 
	service = Commands.class, 
	property = {
		CommandProcessor.COMMAND_SCOPE+"=prsuser",
		CommandProcessor.COMMAND_FUNCTION+"=create",
		CommandProcessor.COMMAND_FUNCTION+"=list",
		CommandProcessor.COMMAND_FUNCTION+"=validate",
		CommandProcessor.COMMAND_FUNCTION+"=retrieveByEMail",
		CommandProcessor.COMMAND_FUNCTION+"=delete",
		CommandProcessor.COMMAND_FUNCTION+"=login",	
		CommandProcessor.COMMAND_FUNCTION+"=loginOA",	
		CommandProcessor.COMMAND_FUNCTION+"=update",	
		CommandProcessor.COMMAND_FUNCTION+"=retrieve",	
		CommandProcessor.COMMAND_FUNCTION+"=implementation"	
	}
)
public class Commands {
	// References
	private UserPersistenceService _persistenceService;
	private UserService _userService;

	@Reference(service=UserPersistenceService.class)
	public void setUserServicePersistence(UserPersistenceService service){
		_persistenceService = service;
		doLog("UserServicePersistence: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUserServicePersistence(UserPersistenceService service){
		doLog("UserServicePersistence: "+(service==null?"NULL":"released"));
		_persistenceService = null;
	}
	
	@Reference(service=UserService.class)
	public void setUserService(UserService service){
		_userService = service;
		doLog("UserService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetUserService(UserService service){
		doLog("UserService: "+(service==null?"NULL":"released"));
		_userService = null;
	}
	// === end references

	// SHELL COMMANDS
	// list
	public void list() {
		List<User> users = _persistenceService.getUsers();
		
		if(users!=null){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				System.out.println(String.format("%-20s%-20s", StringUtils.defaultIfNullOrEmpty(user.getUsername(),"#"), StringUtils.defaultIfNullOrEmpty(user.getEmail(),"#")));
			}
		}
	}
   
	// create
	public void create(String username,  String email, String mobile, String uuidCategories) {
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setMobile(mobile);
		/*
		List<Category> categories= categoryService.getCategory(uuidCategories);
		List<String> cat= new ArrayList<String>();
		for (Category elem:categories){
			cat.add(elem.getUuid());
		}

		List<Attribute> a =  attributeService.getAttributesByCategories(cat);
		int i=3;
		
		for(Attribute att:a){
			List<String> v= new ArrayList<String>();
			String values="TEST"+i;
			i++;
			v.add(values);
			att.setValues(v);
			
		}
	    user.setExtra("Attributes", a);
	    */

		_userService.createUser(user);
	}
	
	// validate
	public void validate(String userId, String username) {
		_persistenceService.validateUsername(userId, username);
	}

	// retrieveByEMail
	public void retrieveByEMail(String email) {
		_persistenceService.getUserByEmail(email);
	}

	//delete
	public void delete(String username, String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setMobile(mobile);;
		user.setEmail(email);
		_persistenceService.deleteUser(user);
	}
	
	// login
	public void login(String username,String password, String email, String mobile) {
		HashMap<String,Object> hm = new HashMap<String,Object> ();
		
		hm.put("username", username);
		hm.put("password", password);
		hm.put("mobile", mobile);
		hm.put("email", email);
		
		_persistenceService.login(hm);
	}
	
	// loginOA
	public void loginOA(String email,String password, String firstName, String lastName) {
		HashMap<String,Object> hm = new HashMap<String,Object> ();
		
		hm.put("firstName", firstName);
		hm.put("password", password);
		hm.put("lastName", lastName);
		hm.put("email", email);
		
		_persistenceService.loginByOAuth2(hm);
	}

	// update
	public void update(String username,String password ,String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setEmail(email);
		_persistenceService.updateUser(user);
	}
	
	// retrieve
	public void retrieve(String username,String password ,String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);;
		user.setEmail(email);
		_persistenceService.getUser(user);
	}

	// implementation
	public void implementation() {
		System.out.println(_persistenceService.getImplementation());
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
