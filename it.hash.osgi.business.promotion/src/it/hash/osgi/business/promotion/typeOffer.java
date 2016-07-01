package it.hash.osgi.business.promotion;

public enum typeOffer {

	SpecialOffer("SpecialOffer", "SPO"), LastMinute("LastMinute", "LMT");

	private final String code;
	private final String messageKey;

	typeOffer(String code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return messageKey;
	}

}
