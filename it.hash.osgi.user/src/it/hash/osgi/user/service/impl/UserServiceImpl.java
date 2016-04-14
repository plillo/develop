package it.hash.osgi.user.service.impl;

import static it.hash.osgi.utils.MapTools.merge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.application.service.ApplicationManager;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.security.jwt.service.JWTService;
import it.hash.osgi.user.User;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserServicePersistence;
import it.hash.osgi.user.service.api.Status;
import it.hash.osgi.user.service.api.UserAttribute;
import it.hash.osgi.user.service.api.UserService;
import it.hash.osgi.utils.StringUtils;

public class UserServiceImpl implements UserService, ManagedService {
	@SuppressWarnings({ "unused", "rawtypes" })
	private Dictionary properties;
	private volatile UserServicePersistence _userPersistenceService;
	private volatile Password _passwordService;
	private volatile EventAdmin _eventAdminService;
	private volatile JWTService _jwtService;
	private volatile UUIDService _UUIDService;
	private volatile AttributeService _attributeService;
	private volatile ApplicationManager _applicationManagerService;

	private Validator validator = new Validator();

	@Override
	public Map<String, Object> login(String identificator, String password) {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("identificator", identificator);
		pars.put("password", password);

		return login(pars);
	}
	
	@Override
	public Map<String, Object> login(String identificator, String password, String appcode) {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("identificator", identificator);
		pars.put("password", password);
		pars.put("appcode", appcode);

		return login(pars);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> login(Map<String, Object> pars) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// GET user
		Map<String, Object> userSearch = _userPersistenceService.getUser(pars);
		boolean matched = false;

		// Build the response
		if ((int) userSearch.get("matched") > 0) {
			User user = (User) userSearch.get("user");

			// MATCHED user
			String password = (String) pars.get("password");
			try {
				// CHECK password
				matched = _passwordService.check(password, user.getSalted_hash_password());

				if (!matched) {
					// PUT status UNAUTHORIZED_ACCESS
					response.put("status", Status.ERROR_UNAUTHORIZED_ACCESS.getCode());
					response.put("message", Status.ERROR_UNAUTHORIZED_ACCESS.getMessage());

					return response;
				}

				// GET ROLES
				// TODO: get user's roles from system
				// ==================================
				String roles = "reguser, admin, root, business.busadmin";
				String appcode = (String) pars.get("appcode");

				// Create a JWT (JSON Web Token)
				Map<String, Object> map = new TreeMap<String, Object>();
				map.put("subject", "userId");
				map.put("uuid", user.getUuid());
				map.put("username", user.getUsername());
				map.put("email", user.getEmail());
				map.put("mobile", user.getMobile());
				map.put("firstName", user.getFirstName());
				map.put("lastName", user.getLastName());
				
				// Setting APPCODE
				if(appcode!=null)
					map.put("appcode", appcode);
				// Setting user's ROLES
				map.put("roles", roles);
				// Setting body
				map.put("body", "this is an access token");
				
				// Setting user's ATTRIBUTE TYPES
				// --- START
				List<JSONObject> attribute_types = new ArrayList<JSONObject>();
				
				// Retrieving of CORE user attributes
				List<Attribute> attributes = _attributeService.getCoreAttributes();
				
				// Retrieving of application user attributes
				List<Attribute> application_attributes = _attributeService.getApplicationAttributes(appcode);
				
				// Filtering of application user attributes
				_applicationManagerService.filterAttributes(appcode, application_attributes);

				// merge user attributes
				attributes.addAll(application_attributes);
				
				for(Attribute attribute: attributes){
					Map<String, Object> mapobj = new TreeMap<String, Object>();
					mapobj.put("type", attribute.getUItype());
					mapobj.put("name", attribute.getName());
					mapobj.put("label", attribute.getLabel());
					if(attribute.getValues()!=null) {
						JSONArray jsa = new JSONArray();
						for(Map<String, Object> value_map: attribute.getValues()) {
							JSONObject jso = new JSONObject();
							
							Object value = value_map.get("value")!=null?value_map.get("value"):"#missing value#";
							jso.put("value", value);
							jso.put("label", value_map.get("label")!=null?value_map.get("label"):value);
					
							jsa.add(jso);
						}
						mapobj.put("values", jsa);
					}
					attribute_types.add(new JSONObject(mapobj));
				}

				map.put("attributeTypes", attribute_types.toArray(new JSONObject[]{}));
				// --- END
				
				// CREATE JWT
				String token = _jwtService.getToken(map);

				// PUT JWT "token"
				response.put("token", token);

				// TRIGGERING system event "user/login"
				Map<String, Object> event_props = new HashMap<>();
				event_props.put("token", token);
				event_props.put("uuid", user.getUuid());
				event_props.put("verified", matched);
				_eventAdminService.sendEvent(new Event("user/login", event_props));

				// PUT status LOGGED
				response.put("status", Status.LOGGED.getCode());
				response.put("message", Status.LOGGED.getMessage());
			} catch (Exception e) {
				// PUT status UNAUTHORIZED_ACCESS
				response.put("status", Status.ERROR_UNAUTHORIZED_ACCESS.getCode());
				response.put("message", Status.ERROR_UNAUTHORIZED_ACCESS.getMessage());
				e.printStackTrace();
			}
		} else {
			// PUT status ERROR_UNMATCHED_USER
			response.put("status", Status.ERROR_UNMATCHED_USER.getCode());
			response.put("message", Status.ERROR_UNMATCHED_USER.getMessage());
		}

		return response;
	}

	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateIdentificatorAndLogin(String identificator, String password, String appcode) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Check and identify the type of identificator (username/email/mobile)
		Map<String, Object> validate = validateIdentificator(identificator);

		if ((Boolean) validate.get("isValid")) {
			response.put("password", password);
			if(appcode!=null)
				response.put("appcode", appcode);

			String identificator_type = (String) validate.get("identificatorType");
			if ("username".equals(identificator_type))
				response.put("username", identificator);
			else if ("email".equals(identificator_type))
				response.put("email", identificator);
			else if ("mobile".equals(identificator_type))
				response.put("mobile", identificator);

			return login(response);
		}
		response.put("status", Status.ERROR_NOTVALID_IDENTIFICATOR.getCode());

		return response;
	}

	@Override
	public List<User> getUsers() {
		return _userPersistenceService.getUsers();
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
	public Map<String, Object> validateIdentificator(String identificator) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("identificatorType", "unmatched");
		map.put("isValid", true);

		if (validator.isValidEmail(identificator))
			map.put("identificatorType", "email");
		else if (validator.isValidMobile(identificator))
			map.put("identificatorType", "mobile");
		else if (validator.isValidUsername(identificator))
			map.put("identificatorType", "username");
		else
			map.put("isValid", false);

		return map;
	}

	@Override
	public Map<String, Object> validateIdentificatorAndGetUser(String identificator) {
		Map<String, Object> response = new TreeMap<String, Object>();
		response.put("validatingItem", identificator);

		// Check and identify the type of identificator (username/email/mobile)
		Map<String, Object> map = validateIdentificator(identificator);
		String identificator_type = (String) map.get("identificatorType");
		response.put("identificatorType", identificator_type);
		response.put("isValid", (Boolean) map.get("isValid"));

		if ((Boolean) map.get("isValid")) {
			// Get the user (if any) matching the identificator
			// ================================================
			map = new TreeMap<String, Object>();

			if ("username".equals(identificator_type))
				map.put("username", identificator);
			else if ("email".equals(identificator_type))
				map.put("email", identificator);
			else if ("mobile".equals(identificator_type))
				map.put("mobile", identificator);
			Map<String, Object> userMap = getUser(map);
			// TODO controllare se ci sono stati errori, nel qual caso terminare
			// restituendo una risposta adeguata

			// Build the response
			if ((int) userMap.get("matched") > 0) {
				// MATCHED user
				response.put("matched", true);
				response.put("message", "Matched user with \"" + identificator + "\" identificator");
			} else {
				response.put("matched", false);
				response.put("message", "The identificator \"" + identificator + "\" is available");
			}
		} else {
			// Not a valid identificator
			response.put("message", "\"" + identificator + "\" is not a valid identificator");
		}

		return response;
	}

	@Override
	public Map<String, Object> deleteUser(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateUser(User user) {
		return _userPersistenceService.updateUser(user);
	}
	
	@Override
	public Map<String, Object> updateUser(Map<String, Object> mapUser) {
		return updateUser(User.toUser(mapUser));
	}

	@Override
	public Map<String, Object> getUser(Map<String, Object> pars) {
		Map<String, Object> response = _userPersistenceService.getUser(pars);

		return response;
	}

	@Override
	public Map<String, Object> getUserByUuid(String uuid) {
		Map<String, Object> user = new TreeMap<String, Object>();
		user.put("uuid", uuid);

		return getUser(user);
	}

	@Override
	public List<User> searchUsers(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		this.properties = properties;

		validator.setProperties(properties);
	}

	@Override
	public Map<String, Object> createUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// If missing, generate a random password
		String password = user.getPassword();
		if (StringUtils.isEmptyOrNull(user.getPassword())) {
			password = _passwordService.getRandom();
			user.setPassword(password);
			response.put("generatedRandomPassword", true);
		}

		try {
			user.setSalted_hash_password(_passwordService.getSaltedHash(password));
		} catch (Exception e) {
			response.put("status", Status.ERROR_HASHING_PASSWORD.getCode());
			response.put("message", Status.ERROR_HASHING_PASSWORD.getMessage());
			return response;
		}

		// Get and Set UUID
		String uuid = _UUIDService.createUUID("core:user");
		if (uuid == null) {
			response.put("status", Status.ERROR_GENERATING_UUID.getCode());
			response.put("message", Status.ERROR_GENERATING_UUID.getMessage());
			return response;
		}
		user.setUuid(uuid);

		return merge(_userPersistenceService.addUser(user), response);
	}

	@Override
	public void createUsersByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<User> getUserDetails(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUUID() {
		return _jwtService.getUuid();
	}

	// getRoles METHODS
	// ................
	@Override
	public List<String> getRoles(String UUID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRoles(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRoles() {
		return _jwtService.getRoles();
	}

	// isUserInRole METHODS
	// ....................
	@Override
	public boolean isUserInRole(String UUID, String... roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(User user, String... roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String UUID, List<String> roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(User user, List<String> roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String... roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserAttribute> getAttributesByContext(String context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Map<String, Object> updateAttributes(Map<String, Object> pars) {
		Map<String, Object> response = new HashMap<String, Object>();
   
		
		response=_userPersistenceService.updateAttribute(pars);
		
		return response;
	}
}
