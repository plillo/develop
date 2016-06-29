package it.hash.osgi.business.promotion.service;


	import java.util.Locale;
	import java.util.ResourceBundle;

	public enum Status {
		// INFORMATIVE
		FOUND(1002, "found"),
		FOUND_MANY(1003, "foundMany"),
		NOT_FOUND(1004, "notFound"),
		EXISTING_NOT_CREATED(1201, "existingNotCreated"),
		EXISTING_MANY_NOT_CREATED(1202, "existingManyNotCreated"),
		PROMOTION_IS_NULL(1203,"promotionisnull"),
		// SUCCESS
		CREATED(2001, "created"),
		
		// CLIENT ERROR
		ERROR_UNMATCHED_PROMOTION(4004, "errorUnmatchedPromotion"),
		
		// SERVER ERROR
		ERROR_GENERATING_UUID(5002, "errorGeneratingUuid"),

		// UPDATE
		UPDATE(2101,"update"),
		SETACTIVE(2102,"setActive"),
	    // UPDATE ERROR
		UNSETACTIVE(4101,"unSetActive"),
		ERROR_UPDATE(4102, "errorUpdate"),
		ERROR_SERVER_UPDATE(4103, "errorUpdate"),
		
		// DELETE
		DELETE (2201,"delete"),
		// DELETE ERROR
		ERROR_DELETE(4202, "errorDelete"),
		ERROR_SERVER_DELETE(4203, "errorServerDelete")
		
		;
		
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
			ResourceBundle labels = ResourceBundle.getBundle("it.hash.osgi.business.promotion.service.codes");
			String message = labels.getString(this.messageKey); 
			
			return message;
		}
		
		public String getMessage(Locale locale) {
			ResourceBundle labels = ResourceBundle.getBundle("it.hash.osgi.business.promotion.service.codes", locale);
			String message = labels.getString(this.messageKey);
			
			return message;
		}
}
