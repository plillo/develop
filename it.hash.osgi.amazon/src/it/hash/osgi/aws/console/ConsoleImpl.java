package it.hash.osgi.aws.console;

import java.util.Dictionary;
import java.util.UUID;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;


public class ConsoleImpl implements Console, ManagedService{
    AWSCredentials credentials = null;
    Dictionary<String, ?> properties = null;
	
	void setCredentials(Dictionary<String, ?> properties){
		if(properties==null){
			System.out.printf("Error accessing AWS credentials");
			return;
		}

		try {
			//
			String access_key_id = (String)properties.get("aws_access_key_id");
			String secret_access_key = (String)properties.get("aws_secret_access_key");
			if("#local".equals(access_key_id))
				credentials = new ProfileCredentialsProvider("elpablito").getCredentials();
			else
				credentials = new BasicAWSCredentials(access_key_id, secret_access_key);
		} catch (Exception e) {
			System.out.printf(e.toString());
			throw new AmazonClientException(""
					+ "Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (C:\\Users\\Paolo\\.aws\\credentials), and is in valid format.", e);
		}
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		this.properties = properties;
		if(properties!=null)
			setCredentials(properties);
		
	}

	@Override
	public AWSCredentials getCredentials() {
		return credentials;
	}

	@Override
	public String randomUUID(String type) {
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		DynamoDB dynamoDB = new DynamoDB(ddbClient);
        Table table = dynamoDB.getTable("Unids");

        boolean loop = true;
        int counter = 1;
        while(loop) {
        	// RANDOM UUID
            String random_UUID = UUID.randomUUID().toString();
            
            // Set item
    	    Item item = new Item()
    	    		.withPrimaryKey(new PrimaryKey("Id", random_UUID))
    	    		.withLong("cdate", new java.util.Date().getTime());
    	    if(type!=null)
    	    	item.withString("type", type);
    	    
    	    // Set specs
            PutItemSpec putItemSpec = new PutItemSpec()
                    .withItem(item)
                    .withConditionExpression("attribute_not_exists(Id)");
            // Put item
	        try {
	            table.putItem(putItemSpec);
	            loop = false;
	            return random_UUID;
	        } catch (ConditionalCheckFailedException e) {
	        	loop = counter++<=10;
	        }
        }
        
        System.out.println("ERROR generating UUID");
        return null;
	}
}
