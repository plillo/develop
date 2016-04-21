package it.hash.osgi.user.rest;

import static it.hash.osgi.utils.StringUtils.isEON;

import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import it.hash.osgi.user.User;
import it.hash.osgi.user.service.api.UserService;

@Api
@Path("users/1.0")
public class Resources {
	private volatile UserService _userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "list", notes = "...")
	// @RolesAllowed("root")
	public Response list() {
		//GenericEntity<List<User>> entity = new GenericEntity<List<User>>(_userService.getUsers()) {};
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_userService.getUsers()).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "createAndLogin", notes = "...")
	public Response createAndLogin(@QueryParam("identificator") String identificator, @QueryParam("password") String password, @QueryParam("appcode") String appcode) {
		Map<String, Object> response = new TreeMap<String, Object>();
		User user = new User();
		
		Map<String, Object> map = _userService.validateIdentificator(identificator);
		String identificator_type = (String)map.get("identificatorType");
		
		if((Boolean)map.get("isValid")) {
			// Get the user (if any) matching the identificator
			// ================================================
			map = new TreeMap<String, Object>();

			if("username".equals(identificator_type))
				user.setUsername(identificator);
			else if("email".equals(identificator_type))
				user.setEmail(identificator);
			else if("mobile".equals(identificator_type))
				user.setMobile(identificator);

			user.setPassword(password);
		
			response = _userService.createUser(user);
			boolean created = (boolean)response.get("created");
			if(created)
				return login(identificator, password, appcode);
		}
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	@Path("/{Uuid}/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "update", notes = "...")
	public Response update(@PathParam("Uuid") String uuid, User user) {
		Map<String, Object> response = new TreeMap<String, Object>();

		response = _userService.updateUser(user);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateIdentificator")
    @io.swagger.annotations.ApiOperation(value = "validateIdentificator", notes = "...")
	public Response validateIdentificator(@QueryParam("value") String identificator) {
		Map<String, Object> response = _userService.validateIdentificatorAndGetUser(identificator);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
    @io.swagger.annotations.ApiOperation(value = "login", notes = "...")
	public Response login(@QueryParam("identificator") String identificator, @QueryParam("password") String password, @QueryParam("appcode") String appcode) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Check for password: ERROR if missing password
		if (isEON(password)) {
			response.put("isLogged", false);
			response.put("status", it.hash.osgi.user.service.api.Status.ERROR_MISSING_PASSWORD);
			response.put("message", it.hash.osgi.user.service.api.Status.ERROR_MISSING_PASSWORD.getMessage());

			return Response.status(Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity(response)
					.status(403).build();
		}

		// LOGIN
		Map<String, Object> loginResponse = _userService.validateIdentificatorAndLogin(identificator, password, appcode);

		if ((int) loginResponse.get("status") == it.hash.osgi.user.service.api.Status.LOGGED.getCode()) {
			// LOGGED
			return Response.status(Status.OK).header("Access-Control-Allow-Origin", "*").entity(loginResponse).build();
		} else {
			// NOT LOGGED
			String errorMessage = (String) loginResponse.get("message");
			System.out.println("Not logged user: " + errorMessage);
			return Response.status(Status.UNAUTHORIZED).entity(loginResponse).header("Access-Control-Allow-Origin", "*")
					.build();
		}
	}
}
