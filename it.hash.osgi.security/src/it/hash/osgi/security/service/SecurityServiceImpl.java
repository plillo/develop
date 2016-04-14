package it.hash.osgi.security.service;

public class SecurityServiceImpl implements SecurityService {

	@Override
	public String getToken() {
		return TokenThreadLocal.get();
	}

	@Override
	public void setToken(String token) {
		TokenThreadLocal.set(token);
		
		System.out.println("Setted JWT in ThreadLocal");
	}

	@Override
	public void unsetToken() {
		TokenThreadLocal.unset();
	}

}
