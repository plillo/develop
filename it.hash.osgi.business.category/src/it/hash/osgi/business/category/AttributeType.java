package it.hash.osgi.business.category;

import java.util.ArrayList;
import java.util.List;

public class AttributeType {
	String definition;
	String text;
	Integer code;
	List<AttributeValue> attValue;

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public List<AttributeValue> getAttValue() {
		return attValue;
	}

	public void setAttValue(List<AttributeValue> attValue) {
		this.attValue = attValue;
	}

	public boolean addAttValue(AttributeValue attValue) {
		if (this.attValue == null)
			this.attValue = new ArrayList<AttributeValue>();
		if (!this.attValue.contains(attValue))
			return this.attValue.add(attValue);

		return false;
	}

	public boolean removeAttValue(AttributeValue attValue) {
		return this.attValue.remove(attValue);
	}

	public AttributeValue getAttValue(Integer code) {
		for (AttributeValue aV : this.attValue) {
			if (aV.getCode() == code)
				return aV;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attValue == null) ? 0 : attValue.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeType other = (AttributeType) obj;
		if (attValue == null) {
			if (other.attValue != null)
				return false;
		} else if (!attValue.equals(other.attValue))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

}
