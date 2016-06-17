package it.hash.osgi.games.tris.service;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Status {
	// INFORMATIVE
	WAITING(1001, "waiting"), 
	
	// SUCCESS
	MATCHED(2001, "matched"),
	
	// CLIENT ERROR
	ERROR_NOTVALID_IDENTIFICATOR(4001, "genericClientError"),
	
	// SERVER ERROR
	ERROR_HASHING_PASSWORD(5001, "genericServerError");

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
		ResourceBundle labels = ResourceBundle.getBundle("it.hash.osgi.games.tris.service.codes");
		String message = labels.getString(this.messageKey); 
		
		return message;
	}
	
	public String getMessage(Locale locale) {
		ResourceBundle labels = ResourceBundle.getBundle("it.hash.osgi.games.tris.service.codes", locale);
		String message = labels.getString(this.messageKey);
		
		return message;
	}
}
