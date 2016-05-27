package it.hash.osgi.aws.s3.service;

import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import it.hash.osgi.aws.console.Console;

public class S3ServiceImpl implements S3Service {
	private Console _consoleService;
	private AmazonS3Client s3Client;
	
	public AmazonS3Client getS3Client(){
		if(s3Client==null)
			s3Client = new AmazonS3Client(_consoleService.getCredentials());

		return s3Client;
	}
	
	@Override
	public String createBucket(String bucketName, String key, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		PutObjectResult por = getS3Client().putObject(bucketName, key, input, metadata);
		return por.getVersionId();
	}

	@Override
	public void removeBucket(String bucketName, String key) {
		getS3Client().deleteObject(bucketName, key);
	}

	@Override
	public S3Object retrieveBucket(String bucketName, String key) {
		return getS3Client().getObject(bucketName, key);
	}

}
