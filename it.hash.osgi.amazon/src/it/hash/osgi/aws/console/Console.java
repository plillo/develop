package it.hash.osgi.aws.console;

import com.amazonaws.auth.AWSCredentials;

public interface Console {
	public AWSCredentials getCredentials();
	public String randomUUID(String type);
}
