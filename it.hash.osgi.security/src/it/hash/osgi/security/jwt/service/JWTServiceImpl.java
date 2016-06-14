package it.hash.osgi.security.jwt.service;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.lang.JoseException;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.security.service.SecurityService;

import static it.hash.osgi.utils.StringUtils.*;

import static it.hash.osgi.utils.Parser.*;

@Component(property={Constants.SERVICE_PID+"=it.hash.osgi.security.jwt.service"})
public class JWTServiceImpl implements JWTService, ManagedService {
	@SuppressWarnings("rawtypes")
	private Dictionary properties = new Hashtable();
	RsaJsonWebKey rsaJsonWebKey;
	
	private SecurityService _securityService;
	
	@Reference(service=SecurityService.class)
	public void setSecurityService(SecurityService service){
		_securityService = service;
		doLog("SecurityService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetSecurityService(SecurityService service){
		doLog("SecurityService: "+(service==null?"NULL":"released"));
		_securityService = null;
	}
	
	@Activate
	void activate() {
		doLog("JWTService activated");
	}
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
	
	public JWTServiceImpl(){
		try {
		    // Generate an RSA key pair, which will be used for signing and verification
			// of the JWT, wrapped in a JWK
			rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);

		    // Give the JWK a Key ID (kid), which is just the polite thing to do
		    rsaJsonWebKey.setKeyId("k1");
		} catch (JoseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public RsaJsonWebKey getRSA() {
		return rsaJsonWebKey;
	}
	
	@Override
	public String getToken(Map<String, Object> map) {
	    String jwt = null;
	    
	    // Completion of the map with config/defaults values
	    if(!map.containsKey("issuer"))
	    	map.put("issuer", defaultIfNull((String)properties.get("issuer"),"jwt-service"));
	    if(!map.containsKey("audience"))
	    	map.put("audience", defaultIfNull((String)properties.get("audience"),"jwt-audience"));
	    if(!map.containsKey("setExpirationTimeMinutesInTheFuture"))
	    	map.put("setExpirationTimeMinutesInTheFuture", defaultIfNull((String)properties.get("setExpirationTimeMinutesInTheFuture"),"10"));
	    if(!map.containsKey("notBeforeMinutesInThePast"))
	    	map.put("notBeforeMinutesInThePast", defaultIfNull((String)properties.get("notBeforeMinutesInThePast"),"2"));
	    if(!map.containsKey("subject"))
	    	map.put("subject", defaultIfNull((String)properties.get("subject"),""));
	    if(!map.containsKey("uuid"))
	    	map.put("uuid", (String)properties.get("uuid"));
	    if(!map.containsKey("body"))
	    	map.put("body", (String)properties.get("body"));
	    if(!map.containsKey("email"))
	    	map.put("email", (String)properties.get("email"));
	    if(!map.containsKey("roles"))
	    	map.put("roles", "guest"); 
	    
		// Create the Claims, which will be the con(String)properties.get("Issuer")tent of the JWT
		JwtClaims claims = new JwtClaims();
		// set ISSUER
		claims.setIssuer((String)map.get("issuer"));  // who creates the token and signs it
		// set AUDIENCE
		claims.setAudience((String)map.get("audience")); // to whom the token is intended to be sent
		// set EXPIRATION TIME IN THE FUTURE
		claims.setExpirationTimeMinutesInTheFuture(parseFloat((String)map.get("setExpirationTimeMinutesInTheFuture"),10)); // time when the token will expire (default: 10 minutes from now)
		// set ID
		claims.setGeneratedJwtId(); // a unique identifier forExpirationTimeMinutesInTheFuture the token
		// set ISSUED AT NOW
		claims.setIssuedAtToNow();  // when the token was issued/created (now)
		// set NOT BEFORE MINUTES IN THE PAST
		claims.setNotBeforeMinutesInThePast(parseFloat((String)map.get("notBeforeMinutesInThePast"), 2)); // time before which the token is not yet valid (default: 2 minutes ago)
		// set SUBJECT
		claims.setSubject((String)map.get("subject")); // the subject/principal is whom the token is about
		// Additional claims about the subject
		// ===================================
		// set UID
		if(isNotEmptyOrNull((String)map.get("uuid")))
			claims.setClaim("uuid",map.get("uuid"));
		// set USERNAME
		if(isNotEmptyOrNull((String)map.get("username")))
			claims.setClaim("username",map.get("username"));
		// set EMAIL
		if(isNotEmptyOrNull((String)map.get("email")))
			claims.setClaim("email",map.get("email"));
		// set MOBILE
		if(isNotEmptyOrNull((String)map.get("mobile")))
			claims.setClaim("mobile",map.get("mobile"));
		// set FIRSTNAME
		if(isNotEmptyOrNull((String)map.get("firstName")))
			claims.setClaim("firstName",map.get("firstName"));
		// set LASTNAME
		if(isNotEmptyOrNull((String)map.get("lastName")))
			claims.setClaim("lastName",map.get("lastName"));
		// set ROLES
		claims.setStringListClaim("roles", Arrays.asList(splitAndTrim((String)map.get("roles")))); // multi-valued claims work too and will end up as a JSON array
		// set ATTRIBUTES
		/*
		if(map.get("attributes")!=null)
			claims.setStringListClaim("attributes", (String[])map.get("attributes")); // multi-valued claims work too and will end up as a JSON array
		*/
		// set ATTRIBUTE TYPES
		if(map.get("attributeTypes")!=null)
			claims.setClaim("attributeTypes", map.get("attributeTypes")); // multi-valued claims work too and will end up as a JSON array
		
		// set BODY
		if(isNotEmptyOrNull((String)map.get("body")))
			claims.setClaim("body",map.get("body"));

		// A JWT is a JWS and/or a JWE with JSON claims as the payload.
		// In this example it is a JWS so we create a JsonWebSignature object.
		JsonWebSignature jws = new JsonWebSignature();

		// The payload of the JWS is JSON content of the JWT Claims
		jws.setPayload(claims.toJson());

		// The JWT is signed using the private key
		jws.setKey(rsaJsonWebKey.getPrivateKey());

		// Set the Key ID (kid) header because it's just the polite thing to do.
		// We only have one key here but a using a Key ID helps facilitate a smooth key rollover process
		jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

		// Set the signature algorithm on the JWT/JWS that will integrity protect the claims
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

		// Sign the JWS and produce the compact serialization or the complete JWT/JWS
		// representation, which is a string consisting of three dot ('.') separated
		// base64url-encoded parts in the form Header.Payload.Signature
		// If you wanted to encrypt it, you can simply set this jwt as the payload
		// of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
		try {
			jwt = jws.getCompactSerialization();
		    return jwt;
		} catch (JoseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return jwt;
	}

	@Override
	public JwtClaims getClaims(String jwt) {
		if(jwt==null)
			return null;
   
	    // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
	    // be used to validate and process the JWT.
	    // The specific validation requirements for a JWT are context dependent, however,
	    // it typically advisable to require a expiration time, a trusted issuer, and
	    // and audience that identifies your system as the intended recipient.
	    // If the JWT is encrypted too, you need only provide a decryption key or
	    // decryption key resolver to the builder.
	    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
	            .setRequireExpirationTime() // the JWT must have an expiration time
	            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
	            .setRequireSubject() // the JWT must have a subject claim
	            .setExpectedIssuer((String)properties.get("issuer")) // whom the JWT needs to have been issued by
	            .setExpectedAudience("Audience") // to whom the JWT is intended for
	            .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
	            .build(); // create the JwtConsumer instance

	    try
	    {
	        //  Validate the JWT and process it to the Claims
	        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
	        return jwtClaims;
	    }
	    catch (InvalidJwtException e)
	    {
	        System.out.println("Invalid JWT! " + e);
	    }
	    
        return null;
	}

	@Override
	public JwtClaims getClaims() {
		String token = _securityService.getToken();
		
		return getClaims(token);
	}

	@Override
	public String getIssuer(String jwt) {
		if(jwt==null)
			return null;
		
		// Build a JwtConsumer that doesn't check signatures or do any validation.
	    JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
	            .setSkipAllValidators()
	            .setDisableRequireSignature()
	            .setSkipSignatureVerification()
	            .build();

	    //The first JwtConsumer is basically just used to parse the JWT into a JwtContext object.
	    JwtContext jwtContext;
		try {
			jwtContext = firstPassJwtConsumer.process(jwt);
			
		    // From the JwtContext we can get the issuer, or whatever else we might need,
		    // to lookup or figure out the kind of validation policy to apply
			return jwtContext.getJwtClaims().getIssuer();
		} catch (InvalidJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedClaimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
        return null;
	}

	@Override
	public String getIssuer() {
		String token = _securityService.getToken();
		
		return getIssuer(token);
	}
	
	@Override
	public List<String> getRoles(String jwt) {
		if(jwt==null)
			return null;

	    JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
	            .setSkipAllValidators()
	            .setDisableRequireSignature()
	            .setSkipSignatureVerification()
	            .build();

	    JwtContext jwtContext;
		try {
			jwtContext = firstPassJwtConsumer.process(jwt);

			return jwtContext.getJwtClaims().getStringListClaimValue("roles");
		} catch (InvalidJwtException e) {
			e.printStackTrace();
		} catch (MalformedClaimException e) {
			e.printStackTrace();
		}
	    
        return null;
	}
	
	@Override
	public List<String> getRoles() {
		String token = _securityService.getToken();
		
		return getRoles(token);
	}

	@Override
	public String getUuid(String jwt) {
		if(jwt==null)
			return null;

	    JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
	            .setSkipAllValidators()
	            .setDisableRequireSignature()
	            .setSkipSignatureVerification()
	            .build();

	    JwtContext jwtContext;
		try {
			jwtContext = firstPassJwtConsumer.process(jwt);

			return jwtContext.getJwtClaims().getStringClaimValue("uuid");
		} catch (InvalidJwtException e) {
			e.printStackTrace();
		} catch (MalformedClaimException e) {
			e.printStackTrace();
		}
	    
        return null;
	}
	
	@Override
	public String getUuid() {
		String token = _securityService.getToken();
		
		return getUuid(token);
	}
	
	@Override
	public String getAppcode(String jwt) {
		if(jwt==null)
			return null;

	    JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
	            .setSkipAllValidators()
	            .setDisableRequireSignature()
	            .setSkipSignatureVerification()
	            .build();

	    JwtContext jwtContext;
		try {
			jwtContext = firstPassJwtConsumer.process(jwt);

			return jwtContext.getJwtClaims().getStringClaimValue("appcode");
		} catch (InvalidJwtException e) {
			e.printStackTrace();
		} catch (MalformedClaimException e) {
			e.printStackTrace();
		}
	    
        return null;
	}
	
	@Override
	public String getAppcode() {
		String token = _securityService.getToken();
		
		return getUuid(token);
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		if(properties!=null)
			this.properties = properties;
	}

}
