package it.hash.osgi.aws.s3.service;

import java.io.InputStream;

import com.amazonaws.services.s3.model.S3Object;

public interface S3Service {
	public String createBucket(String bucketName, String key, InputStream input);
	public void removeBucket(String bucketName, String key);
	public S3Object retrieveBucket(String bucketName, String key);
}
