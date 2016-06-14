package it.hash.osgi.user.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.user.User;

public interface UserPersistenceService {
	// CREATE
	Map<String, Object> addUser(User user);
	Map<String, Object> addUser(Map<String, Object> user);
	
	// READ
	Map<String, Object> getUser(User user);
	Map<String, Object> getConstrainedUser(User user);
	Map<String, Object> getUser(Map<String, Object> user);
	Map<String, Object> getConstrainedUser(Map<String, Object> user);
	   
	User getUserByEmail(String email);
	User getUserByMobile(String mobile);
	User getUserByUsername(String username);
	User getUserById(String userId);
	User getUserByUuid(String uuid);
	List<User> getUsers();
	List<User> getUserDetails(User user);

	// UPDATE
	Map<String, Object> updateUser(User user);
	Map<String, Object> updateUser(Map<String, Object> user);
	Map<String, Object> updateAttribute(Map<String, Object> pars);
	
	// DELETE
	Map<String, Object> deleteUser(User user);
	
	// LOGIN
	Map<String, Object> login(Map<String, Object> user);
	Map<String, Object> loginByOAuth2(Map<String, Object> user);
	
	// VALIDATE
	Map<String, Object> validateUsername(String userId, String username);
	Map<String, Object> validateEMail(String userId, String email);
	Map<String, Object> validateMobile(String userId, String mobile);
	
	// IMPLEMENTATION
	String getImplementation();
	
	

}
