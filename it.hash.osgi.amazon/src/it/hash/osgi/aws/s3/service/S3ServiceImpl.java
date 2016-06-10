package it.hash.osgi.aws.s3.service;

import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import it.hash.osgi.aws.console.Console;

public class S3ServiceImpl implements S3Service {
	private Console _consoleService;
	
	private AmazonS3Client s3Client;
	private TransferManager transferManager;
	
	public AmazonS3Client getS3Client(){
		if(s3Client==null)
			s3Client = new AmazonS3Client(_consoleService.getCredentials());

		return s3Client;
	}
	
	public TransferManager getTransferManager(){
		if(transferManager==null)
			transferManager = new TransferManager(getS3Client());

		return transferManager;
	}
	
	@Override
	public boolean createBucket(String bucketName, String key, String contentType, InputStream is) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(contentType);
		    
			// Upload via TransferManager
			// ==========================
			TransferManager tm = getTransferManager();
			
			PutObjectRequest putRequest = new PutObjectRequest(bucketName, key, is, metadata);
			putRequest.getRequestClientOptions().setReadLimit(5000000);
			Upload upload = tm.upload(putRequest);
			upload.waitForCompletion();

			return upload.getState().ordinal()==2; // completed
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public void removeBucket(String bucketName, String key) {
		getS3Client().deleteObject(bucketName, key);
	}

	@Override
	public S3Object retrieveBucket(String bucketName, String key) {
		return getS3Client().getObject(bucketName, key);
	}
	
	
	
	public void init() {
		System.out.println("S3ServiceImpl INIT");
	}
	public void start() {
		System.out.println("S3ServiceImpl START");
	}
	public void stop() {
		System.out.println("S3ServiceImpl STOP");
		if(transferManager!=null){
			System.out.print("Shutting down TransferManager...");
			transferManager.shutdownNow(true);
			System.out.println("Done.");
		}

	}
	public void destroy() {
		System.out.println("S3ServiceImpl DESTROY");
	}

}
