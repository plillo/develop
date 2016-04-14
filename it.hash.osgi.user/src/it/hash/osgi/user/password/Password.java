package it.hash.osgi.user.password;

public interface Password {
	public String getSaltedHash(String password) throws Exception;
	public boolean check(String password, String stored) throws Exception;
	public String getRandom();
}