package it.hash.osgi.security.service;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(immediate=true)
public class SecurityServiceImpl implements SecurityService {

	@Activate
	void activate() {
		doLog("SecurityService activated");
	}
	
	@Override
	public String getToken() {
		return TokenThreadLocal.get();
	}

	@Override
	public void setToken(String token) {
		TokenThreadLocal.set(token);
		
		doLog("Setted JWT in ThreadLocal");
	}

	@Override
	public void unsetToken() {
		TokenThreadLocal.unset();
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }

}
