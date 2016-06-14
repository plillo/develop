package it.hash.osgi.security.jwt.shell;

import java.util.Map;
import java.util.TreeMap;

import org.apache.felix.service.command.CommandProcessor;
import org.jose4j.lang.JoseException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.security.jwt.service.JWTService;

@Component(
		immediate=true, 
		service = Commands.class, 
		property = {
			CommandProcessor.COMMAND_SCOPE+"=jwt",
			CommandProcessor.COMMAND_FUNCTION+"=token",
			CommandProcessor.COMMAND_FUNCTION+"=claims",
			CommandProcessor.COMMAND_FUNCTION+"=issuer"}
	)
public class Commands {
	private JWTService _jwtservice;
	
	@Reference(service=JWTService.class)
	public void setJWTService(JWTService service){
		_jwtservice = service;
		doLog("JWTService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetJWTService(JWTService service){
		doLog("JWTService: "+(service==null?"NULL":"released"));
		_jwtservice = null;
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
	
	// COMMANDS
	// ========
	public void token(String payload) throws JoseException{
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("body", payload);
	    System.out.println(_jwtservice.getToken(map));
	}
	
	public void claims(String jwt){
	    System.out.println(_jwtservice.getClaims(jwt));
	}
	
	public void issuer(String jwt){
	    System.out.println(_jwtservice.getIssuer(jwt));
	}
}
