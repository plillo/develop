package it.hash.osgi.user.persistence.mock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.component.annotations.Component;

import it.hash.osgi.user.User;
import it.hash.osgi.user.persistence.api.UserPersistenceService;
import it.hash.osgi.utils.StringUtils;

@Component(immediate=true)
public class UserServicePersistenceImpl implements UserPersistenceService{
	List<User> users = new ArrayList<User>();

	@Override
	public Map<String, Object> addUser(User user) {
		users.add(user);
		
		return new TreeMap<String, Object>();
	}
   
	@Override
	public Map<String, Object> addUser(Map<String, Object> mapuser) {
		User user = new User();
		user.setUsername((String)mapuser.get("username"));
		user.setPassword((String)mapuser.get("password"));
		user.setEmail((String)mapuser.get("email"));
		user.setMobile((String)mapuser.get("mobile"));
		
		return addUser(user);
	}

	@Override
	public Map<String, Object> getUser(User user) {
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> getConstrainedUser(User user) {
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> getUser(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> getConstrainedUser(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return new TreeMap<String, Object>();
	}

	@Override
	public List<User> getUsers() {
		return users;
	}

	@Override
	public User getUserByEmail(String email) {
		if(!StringUtils.isEmptyOrNull(email)){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				if(email.equals(user.getEmail()))
					return user;
			}
		}

		return null;
	}

	@Override
	public User getUserByMobile(String mobile) {
		if(!StringUtils.isEmptyOrNull(mobile)){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				if(mobile.equals(user.getMobile()))
					return user;
			}
		}

		return null;
	}

	@Override
	public User getUserByUsername(String username) {
		if(!StringUtils.isEmptyOrNull(username)){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				if(username.equals(user.getUsername()))
					return user;
			}
		}

		return null;
	}

	@Override
	public User getUserById(String userId) {
		if(!StringUtils.isEmptyOrNull(userId)){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				if(userId.equals(user.get_id()))
					return user;
			}
		}

		return null;
	}

	@Override
	public List<User> getUserDetails(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateUser(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateUsername(String userId, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateEMail(String userId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateMobile(String userId, String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImplementation() {
		return "mocked";
	}

	@Override
	public User getUserByUuid(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateAttribute(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
