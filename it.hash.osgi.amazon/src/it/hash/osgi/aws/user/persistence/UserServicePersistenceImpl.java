package it.hash.osgi.aws.user.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import it.hash.osgi.aws.console.Console;
import it.hash.osgi.user.User;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserServicePersistence;
import it.hash.osgi.utils.StringUtils;

public class UserServicePersistenceImpl implements UserServicePersistence{
	public volatile Console _console;
	private volatile Password _passwordService;

	@Override
	public Map<String, Object> addUser(Map<String, Object> user) {
		User user_obj = new User();
		user_obj.setUsername((String)user.get("username"));
		user_obj.setEmail((String)user.get("email"));
		user_obj.setMobile((String)user.get("mobile"));
		user_obj.setFirstName((String)user.get("firstName"));
		user_obj.setLastName((String)user.get("lastName"));
		// ...
		
		return addUser(user_obj);
	}
	
	@Override
	public Map<String, Object> addUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Search for existing user
		Map<String, Object> result = getUser(user);
		
		// not existing: CREATE
		// ====================
		if((int)result.get("matched")==0) {
			AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(_console.getCredentials());
			ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
			DynamoDB dynamoDB = new DynamoDB(ddbClient);
	        Table table = dynamoDB.getTable("Users");
	        
	        // Get UUID
	        /*
	        String uuid = _console.randomUUID("core:user");
	        if(uuid==null){
	            System.out.println("User UUID error");
	            return response;
	        }
	        */
	        String uuid = user.getUuid();
	        
	        // Set item
		    Item item = new Item()
		    		.withPrimaryKey(new PrimaryKey("Id", uuid))
		    		.withLong("cdate", new java.util.Date().getTime());
		    
		    if(StringUtils.isNotEmptyOrNull(user.getUsername()))
		    	item.withString("username", user.getUsername());
		    if(StringUtils.isNotEmptyOrNull(user.getEmail()))
		    	item.withString("email", user.getEmail());
		    if(StringUtils.isNotEmptyOrNull(user.getMobile()))
		    	item.withString("mobile", user.getMobile());		    
		    if(StringUtils.isNotEmptyOrNull(user.getFirstName()))
		    	item.withString("firstName", user.getFirstName());
		    if(StringUtils.isNotEmptyOrNull(user.getLastName()))
		    	item.withString("lastName", user.getLastName());
		    if(StringUtils.isNotEmptyOrNull(user.getSalted_hash_password()))
		    	item.withString("password", user.getSalted_hash_password());
		    
		    // Set specs
	        PutItemSpec putItemSpec = new PutItemSpec()
	                .withItem(item)
	                .withConditionExpression("attribute_not_exists(Id)");
	        
	        // Put item
	        try {
	            table.putItem(putItemSpec);

				User created_user = getUserById(uuid);
				if(created_user!=null) {
					response.put("user", created_user);
					response.put("created", true);
					response.put("returnCode", 100);
				}
	        } catch (ConditionalCheckFailedException e) {
	            System.out.println("PutUser error");
	        } 
	        catch (AmazonServiceException e) {
	            System.out.println(e.toString());
	        }		
		}
		// EXISTING
		else if((int)result.get("matched")==1){
			User existing_user = (User) result.get("user");
			if(existing_user!=null) {
				response.put("user", existing_user);
				response.put("created", false);
				response.put("returnCode", 105);
				response.put("keys", result.get("keys"));
			}
		}
		// EXISTING MANY
		else{
			response.put("created", false);
			response.put("returnCode", 110);
			response.put("users", result.get("users"));
		}
		
		// TODO: Gestire bene la composizione della risposta (deve essere più informativa possibile)

		return response;
	}

	@SuppressWarnings("unused")
	private User getUser(Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getUser(User user) {
		Map<String, Object> map = new TreeMap<String, Object>();
		if(user.get_id()!=null && !"".equals(user))
			map.put("userId", user.get_id());
		if(user.getUsername()!=null && !"".equals(user))
			map.put("username", user.getUsername());
		if(user.getEmail()!=null && !"".equals(user))
			map.put("email", user.getEmail());		
		if(user.getMobile()!=null && !"".equals(user))
			map.put("mobile", user.getMobile());		

		return getUser(map);
	}
	
	@Override
	public Map<String,Object> getConstrainedUser(User user){
		Map<String, Object> map = new TreeMap<String, Object>();
		if(user.get_id()!=null && !"".equals(user))
			map.put("userId", user.get_id());
		if(user.getUsername()!=null && !"".equals(user))
			map.put("username", user.getUsername());
		if(user.getEmail()!=null && !"".equals(user))
			map.put("email", user.getEmail());		
		if(user.getMobile()!=null && !"".equals(user))
			map.put("mobile", user.getMobile());
		
		return getUser(map, true);
	}
	
	@Override
	public Map<String, Object> getUser(Map<String, Object> user) {
		return getUser(user, false);
	}
	
	@Override
	public Map<String, Object> getConstrainedUser(Map<String, Object> user) {
		return getUser(user, true);
	}

	private Map<String, Object> getUser(Map<String, Object> user, boolean constrained) {
		Map<String,Object> response = new HashMap<String,Object>();
		Map<User, TreeSet<String>> matchs = new TreeMap<User, TreeSet<String>>();
		
		// Setup Amazon DB client
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(_console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		
		if(constrained){
			// TODO get User with constraining conditions
		}
		else {
			searchBy(user, matchs, ddbClient, "Id", "userId");
			searchBy(user, matchs, ddbClient, "username", "username");
			searchBy(user, matchs, ddbClient, "email", "email");
			searchBy(user, matchs, ddbClient, "mobile", "mobile");
			
			// Set response: number of matched users
			response.put("matched", matchs.size());

			// Set response details
			switch(matchs.size()){
			case 0:
				response.put("found", false);
				break;
			case 1:
				User key = (User) matchs.keySet().toArray()[0];
				response.put("user", key);
				response.put("keys", matchs.get(key));
				response.put("found", true);
				break;
			default:
				response.put("users", matchs);
				response.put("returnCode", 110);
			}
		}
				
		return response;
	}

	private void searchBy(Map<String, Object> user, Map<User, TreeSet<String>> matchs, AmazonDynamoDBClient ddbClient, String fieldDBName, String fieldName) {
		if(StringUtils.isEmptyOrNull((String)user.get(fieldName)))
			return;
		
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":val", new AttributeValue().withS((String)user.get(fieldName))); 
		ScanRequest scanRequest = new ScanRequest()
	    	    .withTableName("Users")
//	    	    .withLimit(1)
	    	    .withFilterExpression(fieldDBName+" = :val")
	    	    .withExpressionAttributeValues(expressionAttributeValues)
	    	    .withProjectionExpression("Id, lastName, firstName, username, email, mobile, password");

		ScanResult result = ddbClient.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			User user_item = new User();
			if(item.get("Id")!=null)
				user_item.set_id(item.get("Id").getS());
			if(item.get("username")!=null)
				user_item.setUsername(item.get("username").getS());
			if(item.get("email")!=null)
				user_item.setEmail(item.get("email").getS());
			if(item.get("mobile")!=null)
				user_item.setMobile(item.get("mobile").getS());
			if(item.get("firstName")!=null)
				user_item.setFirstName(item.get("firstName").getS());
			if(item.get("lastName")!=null)
				user_item.setLastName(item.get("lastName").getS());
			if(item.get("password")!=null)
				user_item.setPassword(item.get("password").getS());
			
			TreeSet<String> list = matchs.get(user_item);
			if(list==null)
				list = new TreeSet<String>();
			    
			list.add("userId");
			matchs.put(user_item, list);
		}
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(_console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");

        ScanRequest scanRequest = new ScanRequest()
        	    .withTableName("Users")
        	    .withProjectionExpression("Id, lastName, firstName, username, email, mobile");

    	ScanResult result = ddbClient.scan(scanRequest);
    	for (Map<String, AttributeValue> item : result.getItems()) {
    		User user = new User();
    		if(item.get("Id")!=null)
    			user.set_id(item.get("Id").getS());
    		if(item.get("username")!=null)
    			user.setUsername(item.get("username").getS());
    		if(item.get("email")!=null)
    			user.setEmail(item.get("email").getS());
    		if(item.get("mobile")!=null)
    			user.setMobile(item.get("mobile").getS());
    		if(item.get("lastName")!=null)
    			user.setFirstName(item.get("lastName").getS());
    		if(item.get("lastName")!=null)
    			user.setLastName(item.get("lastName").getS());
    		
    		users.add(user);
    	}
    	
    	return users;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByMobile(String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(String uuid) {
		User user = new User();
		
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(_console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":val", new AttributeValue().withS(uuid)); 
		ScanRequest scanRequest = new ScanRequest()
	    	    .withTableName("Users")
	    	    //.withLimit(1)
	    	    .withFilterExpression("Id = :val")
	    	    .withExpressionAttributeValues(expressionAttributeValues)
	    	    .withProjectionExpression("Id, lastName, firstName, username, email, mobile, password");

		ScanResult result = ddbClient.scan(scanRequest);
    	for (Map<String, AttributeValue> item : result.getItems()) {
    		if(item.get("Id")!=null)
    			user.set_id(item.get("Id").getS());
    		if(item.get("username")!=null)
    			user.setUsername(item.get("username").getS());
    		if(item.get("email")!=null)
    			user.setEmail(item.get("email").getS());
    		if(item.get("mobile")!=null)
    			user.setMobile(item.get("mobile").getS());
    		if(item.get("lastName")!=null)
    			user.setFirstName(item.get("lastName").getS());
    		if(item.get("lastName")!=null)
    			user.setLastName(item.get("lastName").getS());
    	}
		
		return user;
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
		Map<String, Object> response = new HashMap<String, Object>();
		String password = (String) user.get("password");
		
		// Return ERROR if missing password
		if (password == null || "".equals(password)) {
			response.put("returnCode", 101); // 101: missing password
			return response;
		}
		
		// Search and get user
		Map<String, Object> result = getUser(user);
		
		// Get reference to user (if found)
		User userFound = (User) result.get("user");
		if (userFound != null) {
			try {
				if (_passwordService.check(password, userFound.getPassword())) {
					response.put("user", userFound);
					response.put("returnCode", 100);
					response.put("logged", true);
				} else
					response.put("returnCode", 102); // 102: mismatched password

			} catch (Exception e) {
				response.put("returnCode", 103); // 103: exception
				response.put("logged", false);
			}
		} else if (result.containsKey("users")){
			response.put("returnCode", 110); 
			response.put("logged", false);
			response.put("users",result.get("users"));
		} else if((int) result.get("matched") == 0){
			response.put("logged", false);
			response.put("returnCode",115);
		}
		
		return response;
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
		return "AWS";
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
