package it.hash.osgi.user;

import java.util.Map;

public class AttributeValue {
	private String attributeUuid;
	private Map<String, Object> value;
	
	public String getAttributeUuid() {
		return attributeUuid;
	}
	public void setAttributeUuid(String attributeUuid) {
		this.attributeUuid = attributeUuid;
	}
	public Map<String, Object> getValue() {
		return value;
	}
	public void setValue(Map<String, Object> value) {
		this.value = value;
	}

}
