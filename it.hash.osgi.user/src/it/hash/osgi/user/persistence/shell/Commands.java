package it.hash.osgi.user.persistence.shell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import it.hash.osgi.user.User;
import it.hash.osgi.user.persistence.api.UserServicePersistence;
import it.hash.osgi.user.service.api.UserService;
import it.hash.osgi.utils.StringUtils;

public class Commands {
	
	private volatile UserServicePersistence persistence;
	private volatile UserService userService;
	//private volatile AttributeService attributeService;

	public void list() {
		List<User> users = persistence.getUsers();
		
		if(users!=null){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				System.out.println(String.format("%-20s%-20s", StringUtils.defaultIfNullOrEmpty(user.getUsername(),"#"), StringUtils.defaultIfNullOrEmpty(user.getEmail(),"#")));
			}
		}
	}
   
	public void adduser(String username,  String email, String mobile, String uuidCategories) {
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

		userService.createUser(user);
	}
	
	public void validate(String userId,String username) {
		persistence.validateUsername(userId, username);
	}

	public void getusermail(String email) {
		persistence.getUserByEmail(email);
	}

	public void removeuser(String username, String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setMobile(mobile);;
		user.setEmail(email);
		persistence.deleteUser(user);
	}
	
	public void login(String username,String password, String email, String mobile) {
		HashMap<String,Object> hm = new HashMap<String,Object> ();
		
		hm.put("username", username);
		hm.put("password", password);
		hm.put("mobile", mobile);
		hm.put("email", email);
		
		persistence.login(hm);
	}
	
	public void loginOA(String email,String password, String firstName, String lastName) {
		HashMap<String,Object> hm = new HashMap<String,Object> ();
		
		hm.put("firstName", firstName);
		hm.put("password", password);
		hm.put("lastName", lastName);
		hm.put("email", email);
		
		persistence.loginByOAuth2(hm);
	}

	public void updateuser(String username,String password ,String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setEmail(email);
		persistence.updateUser(user);
	}
	
	public void getuser(String username,String password ,String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);;
		user.setEmail(email);
		persistence.getUser(user);
	}

	public void impl() {
		System.out.println(persistence.getImplementation());
	}
}
