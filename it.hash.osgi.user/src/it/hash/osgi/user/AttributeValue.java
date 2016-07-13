package it.hash.osgi.user;

import com.google.gson.JsonElement;

public class AttributeValue {
	private String attributeUuid;
	private JsonElement value;
	
	public String getAttributeUuid() {
		return attributeUuid;
	}
	public void setAttributeUuid(String attributeUuid) {
		this.attributeUuid = attributeUuid;
	}
	public JsonElement getValue() {
		return value;
	}
	public void setValue(JsonElement value) {
		this.value = value;
	}

}
