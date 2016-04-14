package it.hash.osgi.security.service;

public interface SecurityService {
	String getToken();
	void setToken(String token);
	void unsetToken();
}
