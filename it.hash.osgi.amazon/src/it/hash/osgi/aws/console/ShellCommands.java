package it.hash.osgi.aws.console;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.DomainMetadataRequest;
import com.amazonaws.services.simpledb.model.DomainMetadataResult;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import it.hash.osgi.utils.Random;

public class ShellCommands {
	public volatile Console console;
	
	public void ec2() {
		try {
			AmazonEC2 ec2 = new AmazonEC2Client(console.getCredentials());
	        ec2.setEndpoint("ec2.eu-central-1.amazonaws.com");
	        // Availability zone
	        // -----------------
	        DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
	        System.out.println(""
	        		+ "AVAILABILITY ZONES"
	        		+ "\n------------------"
	        		+ "\nYou have access to " 
	        		+ availabilityZonesResult.getAvailabilityZones().size() 
	        		+ " Availability Zones.");
	        
	        // Instances
	        // ---------
            DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
            List<Reservation> reservations = describeInstancesRequest.getReservations();
            Set<Instance> instances = new HashSet<Instance>();
            for (Reservation reservation : reservations) {
                instances.addAll(reservation.getInstances());
            }
            System.out.println(""
	        		+ "INSTANCES"
	        		+ "\n---------"
            		+ "\nYou have " 
		            + instances.size() 
		            + " Amazon EC2 instance(s) running.");
            // instances states
            if(instances.size()>0)
	            for (Instance instance : instances) {
	                String iid = instance.getInstanceId();
	                String state = instance.getState().getName();
	                System.out.println("Instance " + iid + "- state:"+state);
	            }
		}
		catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public void s3() {
	     /*
         * Amazon S3
         *
         * The AWS S3 client allows you to manage buckets and programmatically
         * put and get objects to those buckets.
         *
         * In this sample, we use an S3 client to iterate over all the buckets
         * owned by the current user, and all the object metadata in each
         * bucket, to obtain a total object and space usage count. This is done
         * without ever actually downloading a single object -- the requests
         * work with object metadata only.
         */
        try {
        	AmazonS3Client s3  = new AmazonS3Client(console.getCredentials());
            List<Bucket> buckets = s3.listBuckets();

            long totalSize  = 0;
            int  totalItems = 0;
            for (Bucket bucket : buckets) {

            	ObjectListing objects = s3.listObjects(bucket.getName());
                do {
                    for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                        totalSize += objectSummary.getSize();
                        totalItems++;
                    }
                    objects = s3.listNextBatchOfObjects(objects);
                } while (objects.isTruncated());
            }

            System.out.println(""
	        		+ "S3 BUCKETS"
	        		+ "\n----------"
            		+ "\nYou have " + buckets.size() + " Amazon S3 bucket(s), " +
                    "containing " + totalItems + " objects with a total size of " + totalSize + " bytes.");
        } catch (AmazonServiceException ase) {
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Error Message: " + ace.getMessage());
        }
	}
	
	public void sdb() {
        /*
         * Amazon SimpleDB
         *
         * The AWS SimpleDB client allows you to query and manage your data
         * stored in SimpleDB domains (similar to tables in a relational DB).
         *
         * In this sample, we use a SimpleDB client to iterate over all the
         * domains owned by the current user, and add up the number of items
         * (similar to rows of data in a relational DB) in each domain.
         */
        try {
        	AmazonSimpleDBClient sdb = new AmazonSimpleDBClient(console.getCredentials());
            
            ListDomainsRequest sdbRequest = new ListDomainsRequest().withMaxNumberOfDomains(100);
            ListDomainsResult sdbResult = sdb.listDomains(sdbRequest);

            int totalItems = 0;
            for (String domainName : sdbResult.getDomainNames()) {
                DomainMetadataRequest metadataRequest = new DomainMetadataRequest().withDomainName(domainName);
                DomainMetadataResult domainMetadata = sdb.domainMetadata(metadataRequest);
                totalItems += domainMetadata.getItemCount();
            }

            System.out.println(""
	        		+ "SimpleDB"
	        		+ "\n--------"
            		+ "\nYou have " + sdbResult.getDomainNames().size() + " Amazon SimpleDB domain(s)" +
                    "containing a total of " + totalItems + " items.");
        } catch (AmazonServiceException ase) {
                System.out.println("Caught Exception: " + ase.getMessage());
                System.out.println("Reponse Status Code: " + ase.getStatusCode());
                System.out.println("Error Code: " + ase.getErrorCode());
                System.out.println("Request ID: " + ase.getRequestId());
        }
	}
	
	public void ses() {
	    final String FROM = "paolo.lillo@hash.it";  // Replace with your "From" address. This address must be verified.
	    final String TO = "paolo.lillo@unisalento.it"; // Replace with a "To" address. If you have not yet requested
	                                                   // production access, this address must be verified.
	    final String BODY = "This email was sent through Amazon SES by using the AWS SDK for Java.";
	    final String SUBJECT = "Amazon SES test (AWS SDK for Java)";
		
        // Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(new String[]{TO});

        // Create the subject and body of the message.
        Content subject = new Content().withData(SUBJECT);
        Content textBody = new Content().withData(BODY);
        Body body = new Body().withText(textBody);

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);

        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);

        try {
            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            // Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(console.getCredentials());

            Region REGION = Region.getRegion(Regions.EU_WEST_1);
            client.setRegion(REGION);

            // Send the email.
            client.sendEmail(request);
            System.out.println("Email sent!");

        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
	}
	
	public void sqs(){
        AmazonSQS sqs = new AmazonSQSClient(console.getCredentials());
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called MyQueue.\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
            String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();

            // Send a message
            System.out.println("Sending a message to MyQueue.\n");
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));

            // Receive messages
            System.out.println("Receiving messages from MyQueue.\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<com.amazonaws.services.sqs.model.Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (com.amazonaws.services.sqs.model.Message message : messages) {
                System.out.println("  Message");
                System.out.println("    MessageId:     " + message.getMessageId());
                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("    Body:          " + message.getBody());
                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                    System.out.println("  Attribute");
                    System.out.println("    Name:  " + entry.getKey());
                    System.out.println("    Value: " + entry.getValue());
                }
            }
            System.out.println();

            // Delete a message
            System.out.println("Deleting a message.\n");
            String messageRecieptHandle = messages.get(0).getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageRecieptHandle));

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
	}

	public void dyndb(){
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		DynamoDB dynamoDB = new DynamoDB(ddbClient);
		
		TableCollection<ListTablesResult> tables = dynamoDB.listTables();
		Iterator<Table> iterator = tables.iterator();
		while (iterator.hasNext()) {
			Table table = iterator.next();
            getTableInformation(dynamoDB, table.getTableName());
		}
	}
	
	public void createtable(String tableName) {
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		DynamoDB dynamoDB = new DynamoDB(ddbClient);
		
        try {
            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("Id")
                .withAttributeType("N"));
            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement()
                .withAttributeName("Id")
                .withKeyType(KeyType.HASH)); //Partition key
            CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                    .withReadCapacityUnits(5L)
                    .withWriteCapacityUnits(6L));
            System.out.println("Issuing CreateTable request for " + tableName);
            Table table = dynamoDB.createTable(request);
            System.out.println("Waiting for " + tableName
                + " to be created...this may take a while...");
            table.waitForActive();
            getTableInformation(dynamoDB, tableName);
        } catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
	
	public String newid(String type) {
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		DynamoDB dynamoDB = new DynamoDB(ddbClient);
        Table table = dynamoDB.getTable("Unids");

        boolean loop = true;
        int counter = 1;
        while(loop) {
        	// RANDOM ID
            String random_key = Random.getRandomKey(32);
            
            // Set item
    	    Item item = new Item()
    	    		.withPrimaryKey(new PrimaryKey("Id", random_key))
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
	            return random_key;
	        } catch (ConditionalCheckFailedException e) {
	        	loop = counter++<=10;
	        }
        }
        System.out.println("New ID error");
        return null;
	}
	
	public void putuser(String name, String surname){
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		DynamoDB dynamoDB = new DynamoDB(ddbClient);
        Table table = dynamoDB.getTable("Users");
        
        // Get UID
        String uid = newid("core:user");
        if(uid==null){
            System.out.println("User ID error");
            return;
        }
        
        // Set item
	    Item item = new Item()
	    		.withPrimaryKey(new PrimaryKey("Id", uid))
	    		.withLong("cdate", new java.util.Date().getTime())
	    		.withString("firstName", name)
	    		.withString("lastName", surname);
	    
	    // Set specs
        PutItemSpec putItemSpec = new PutItemSpec()
                .withItem(item)
                .withConditionExpression("attribute_not_exists(Id)");
        
        // Put item
        try {
            table.putItem(putItemSpec);
        } catch (ConditionalCheckFailedException e) {
            System.out.println("PutUser error");
        } 
        catch (AmazonServiceException e) {
            System.out.println(e.toString());
        }
	}
	
	public void listusers(){
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");

        ScanRequest scanRequest = new ScanRequest()
        	    .withTableName("Users")
        	    .withProjectionExpression("Id, lastName, firstName");

    	ScanResult result = ddbClient.scan(scanRequest);
    	for (Map<String, AttributeValue> item : result.getItems()) {
    		System.out.println(String.format("%-20s %-20s [%s]",item.get("lastName").getS(),item.get("firstName").getS(),item.get("Id").getS()));
    	}
	}
	
	void getTableInformation(DynamoDB dynamoDB, String tableName) {
        TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
        System.out.format("\n>>> Table: %s\n" + "Status: %s \nItems number: %d \n"
                + "Provisioned Throughput (read capacity units/sec): %d \n"
                + "Provisioned Throughput (write capacity units/sec): %d \n",
        tableDescription.getTableName(), 
        tableDescription.getTableStatus(),
        tableDescription.getItemCount(),
        tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
        tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
	}
}
