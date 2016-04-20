package it.hash.osgi.user.service.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.hash.osgi.user.User;

public interface UserService {
	Map<String, Object> login(String username, String password);

	Map<String, Object> login(String username, String password, String appcode);
	
	Map<String, Object> login(Map<String, Object> pars);

	Map<String, Object> loginByOAuth2(Map<String, Object> pars);

	Map<String, Object> validateIdentificatorAndLogin(String identificator, String password, String appcode);

	Map<String, Object> validateUsername(String userId, String username);

	Map<String, Object> validateEMail(String userId, String email);

	Map<String, Object> validateMobile(String userId, String mobile);

	Map<String, Object> validateIdentificator(String identificator);

	Map<String, Object> validateIdentificatorAndGetUser(String identificator);
	
	Map<String, Object> changePassword(String identificator, String oldPassword, String password);

	Map<String, Object> getUser(Map<String, Object> pars);

	Map<String, Object> getUserByUuid(String uuid);

	Map<String, Object> createUser(User user);

	Map<String, Object> deleteUser(Map<String, Object> pars);

	Map<String, Object> updateUser(User user);
	Map<String, Object> updateUser(Map<String, Object> pars);

	List<User> getUserDetails(Map<String, Object> pars);

	List<User> searchUsers(String parameter);

	Map<String, Object> updateAttributes(Map<String, Object> response);

	// List
	// TODO filter list by conditions
	List<User> getUsers();

	// Create by CSV
	void createUsersByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException;

	// Get current UUID by token
	String getUUID();

	// get attributes by Context
	List<UserAttribute> getAttributesByContext(String context);

	// Get ROLES OF user by UUID/pojo or current user by token
	List<String> getRoles(String UUID);

	List<String> getRoles(User user);

	List<String> getRoles();

	// check if user (by UUID/pojo or current user by token) is in 1+ roles
	boolean isUserInRole(String UUID, String... roles);

	boolean isUserInRole(User user, String... roles);

	boolean isUserInRole(String UUID, List<String> roles);

	boolean isUserInRole(User user, List<String> roles);

	boolean isUserInRole(String... roles);

	// ...

}
