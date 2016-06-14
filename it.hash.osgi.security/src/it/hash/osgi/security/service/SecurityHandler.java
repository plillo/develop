package it.hash.osgi.security.service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.codec.binary.Base64;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

import it.hash.osgi.security.jwt.service.JWTService;

@Component
public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler {
	String name = "SecurityHandler";
	// List<String> roles;
	
	private JWTService _jwtService;

	@Reference(service=JWTService.class)
	public void setJWTService(JWTService service){
		_jwtService = service;
		doLog("JWTService: "+(service==null?"NULL":"got"));
	}
	
	public void unsetJWTService(JWTService service){
		doLog("JWTService: "+(service==null?"NULL":"released"));
		_jwtService = null;
	}

	// This method will be called before the execution of REST API annotated with @RolesAllowed( ... )
	// The execution of API will be enabled if this method returns TRUE otherwise the requester 
	// will receive a HTTP error 403 "Forbidden"
	@Override
	public boolean isUserInRole(Principal user, String role) {
		if("anonymous".equalsIgnoreCase(role))
			return true;
		
		// Get JWT from principal
		String jwt = user.getName();
		// Get roles from JWT
		List<String> roles = _jwtService.getRoles(jwt);
		
		if(roles==null) 
			return false;

		// RETURN true if user is "root" or in role "role"
		return roles.contains("root") || roles.contains(role);
	}

	@SuppressWarnings("unused")
	@Override
	public Principal authenticate(ContainerRequestContext requestContext) {
		String jwt = null;
		String user_id = "anonymous";
		
		// GET token: 1) from Authorization header (Bearer), 2) from Query string
		String authCredentials = requestContext.getHeaderString("Authorization");
		if(authCredentials!=null) 
			if(authCredentials.startsWith("Bearer"))
				jwt = authCredentials.replaceFirst("Bearer" + " ", "");
			else if(authCredentials.startsWith("Basic")){
				// Header value format "Basic encoded string" for Basic authentication. 
				// Example: "Basic YWRtaW46YWRtaW4="
				final String encoded_IdentificatorAndPassword = authCredentials.replaceFirst("Basic" + " ", "");
				String identificatorAndPassword = null;
				try {
					byte[] decodedBytes = Base64.decodeBase64(identificatorAndPassword);
					identificatorAndPassword = new String(decodedBytes, "UTF-8");
				} catch (IOException e) {
					e.printStackTrace();
				}
				final StringTokenizer tokenizer = new StringTokenizer(identificatorAndPassword, ":");
				final String identificator = tokenizer.nextToken();
				final String password = tokenizer.nextToken();
				
				// TODO: call some UserService/LDAP here with identificator/password parameters
			}
		else // search token within query parameyters ("access-token")
			jwt = requestContext.getUriInfo().getQueryParameters().getFirst("access-token");
		
		if(jwt!=null) {
			//roles = _jwtService.getRoles(jwt);
			//user_id = _jwtService.getUuid(jwt);
			
			TokenThreadLocal.set(jwt);
		}

		return new User(jwt); 
	}

	@Override
	public String getAuthenticationScheme() {
		return null;
	}
	
	
    private void doLog(String message) {
        System.out.println("## [" + this.getClass() + "] " + message);
    }

}
