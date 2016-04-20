package it.hash.osgi.user.service.api;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Status {
	// INFORMATIVE
	LOGGED(1001, "logged"), 
	FOUND(1002, "found"),
	FOUND_MANY(1003, "foundMany"),
	NOT_FOUND(1004, "notFound"),
	EXISTING_NOT_CREATED(1201, "existingNotCreated"),
	EXISTING_MANY_NOT_CREATED(1202, "existingManyNotCreated"),
	
	// SUCCESS
	CREATED(2001, "created"),
	
	// CLIENT ERROR
	ERROR_NOTVALID_IDENTIFICATOR(4001, "errorNotValidIdentificator"),
	ERROR_UNAUTHORIZED_ACCESS(4002, "errorUnauthorizedAccess"),
	ERROR_FORBIDDEN_ACCESS(4003, "errorForbidenAccess"),
	ERROR_UNMATCHED_USER(4004, "errorUnmatchedUser"),
	ERROR_MISSING_PASSWORD(4005, "errorMissingPassword"),
	ERROR_MISSING_OLD_PASSWORD(4006, "errorMissingOldPassword"),
	ERROR_MISSING_NEW_AND_OLD_PASSWORD(4007, "errorMissingNewAndOldPassword"),
	ERROR_MISMATCHED_PASSWORD(4008, "errorMismatchedPassword"),
	
	// SERVER ERROR
	ERROR_HASHING_PASSWORD(5001, "errorHashingPassword"),
	ERROR_GENERATING_UUID(5002, "errorGeneratingUuid");

	private final int code;
	private final String messageKey;

	Status(int code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		ResourceBundle labels = ResourceBundle.getBundle("it.hash.osgi.user.service.api.codes");
		String message = labels.getString(this.messageKey); 
		
		return message;
	}
	
	public String getMessage(Locale locale) {
		ResourceBundle labels = ResourceBundle.getBundle("it.hash.osgi.user.service.api.codes", locale);
		String message = labels.getString(this.messageKey);
		
		return message;
	}
}

