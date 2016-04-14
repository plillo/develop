package it.hash.osgi.security.jwt.shell;

import java.util.Map;
import java.util.TreeMap;

import org.jose4j.lang.JoseException;

import it.hash.osgi.security.jwt.service.JWTService;

public class Commands {
	private volatile JWTService _jwtservice;

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
